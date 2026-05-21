package com.worklink.paymentsystem.Pagos.service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.stripe.model.PaymentIntent;
import com.worklink.paymentsystem.Pagos.Exceptions.PagoFallidoException;
import com.worklink.paymentsystem.Pagos.Exceptions.TokenInvalidoException;
import com.worklink.paymentsystem.Pagos.dto.Request.PagoRequest;
import com.worklink.paymentsystem.Pagos.dto.Response.PagoResponse;
import com.worklink.paymentsystem.Pagos.enums.EstadoPago;
import com.worklink.paymentsystem.Pagos.mapper.PagoMapper;
import com.worklink.paymentsystem.Pagos.model.Pago;
import com.worklink.paymentsystem.Pagos.model.TransferenciaPendiente;
import com.worklink.paymentsystem.Pagos.repository.PagoRepository;
import com.worklink.paymentsystem.Pagos.repository.TransferenciaPendienteRepository;
import com.worklink.paymentsystem.integrations.Response.ProveedorBancarioResponse;
import com.worklink.paymentsystem.integrations.Response.ServiceResponse;
import com.worklink.paymentsystem.integrations.service.PerfilProveedor;
import com.worklink.paymentsystem.integrations.service.StripeService;

@Service
public class ServicioPagos {

    private static final Logger log = LoggerFactory.getLogger(ServicioPagos.class);

    private final StripeService stripeService;
    private final PagoValidator pagoValidator;
    private final PagoRepository pagoRepository;
    private final PerfilProveedor perfilProveedor;
    private final TransferenciaPendienteRepository transferenciaRepository;

    public ServicioPagos(
        StripeService stripeService,
        PagoValidator pagoValidator,
        PagoRepository pagoRepository,
        TransferenciaPendienteRepository transferenciaRepository,
        PerfilProveedor perfilProveedor
    ) {
        this.stripeService = stripeService;
        this.pagoValidator = pagoValidator;
        this.pagoRepository = pagoRepository;
        this.transferenciaRepository = transferenciaRepository;
        this.perfilProveedor = perfilProveedor;
    }

    // ─────────────────────────────────────────
    // PROCESAR PAGO
    // ─────────────────────────────────────────
    public PagoResponse procesarPago(PagoRequest pagoRequest) {
        log.info(
            "Iniciando procesamiento de pago para cliente {} y servicio {}",
            pagoRequest.getClienteID(), pagoRequest.getServicioID()
        );

        //Validar servicio y monto
        ServiceResponse servicio = pagoValidator.validarPago(pagoRequest);

        BigDecimal comision = pagoRequest.getMonto().multiply(new BigDecimal("0.10"));
        BigDecimal montoNeto = pagoRequest.getMonto().subtract(comision);

        Pago pago = new Pago();
        pago.setClienteID(
            pagoRequest.getClienteID()
        );
        pago.setProveedorID(
            servicio.getIdProveedor()
        );
        pago.setServicioID(
            pagoRequest.getServicioID()
        );
        pago.setMonto(
            pagoRequest.getMonto()
        );
        pago.setMetodoPago(
            pagoRequest.getMetodoPago()
        );

        pago.setComision(comision);
        pago.setMontoNeto(montoNeto);
        pago.setMoneda("COP");
        pago.setEstadoPago(EstadoPago.PENDIENTE);

        pago.setTokenConfirmacion(
            generarTokenConfirmacion()
        );
        pago.setTokenUsado(false);

        //Persistir en PENDIENTE
        pago = pagoRepository.save(pago);

        try {
            procesarSegunMetodo(pago, pagoRequest);
            pago.setEstadoPago(EstadoPago.RETENIDO); // Retenemos el pago hasta que el cliente confirme el servicio
            pago.setProcessedAt(LocalDateTime.now());
            log.info("Pago {} retenido exitosamente", pago.getId());

        } catch (Exception e) {
            pago.setEstadoPago(EstadoPago.ERROR);
            pago.setMotivoRechazo(e.getMessage());
            pagoRepository.save(pago);
            log.error(
                "Error al procesar el pago {}: {}", pago.getId(), e.getMessage()
            );
            throw new PagoFallidoException("El pago no pudo ser procesado: " + e.getMessage());
        }

        pago = pagoRepository.save(pago);

        return PagoMapper.pagoToResponse(pago,
            "Pago retenido. Entrega el token al proveedor cuando complete el servicio: "
            + servicio.getNombreServicio()
        );
    }

    // ─────────────────────────────────────────
    // CONFIRMAR CON TOKEN
    // ─────────────────────────────────────────
    public PagoResponse confirmarConToken(String token, Long prestadorID) {

        Optional<Pago> pagoOpt = pagoRepository.findByTokenConfirmacion(token);

        if (!pagoOpt.isPresent()) {
            throw new TokenInvalidoException("Token de confirmación inválido");
        }
        Pago pago = pagoOpt.get();
        if (pago.getTokenUsado()) {
            throw new TokenInvalidoException("Este token ya fue utilizado");
        }

        if (pago.getEstadoPago() != EstadoPago.RETENIDO) {
            throw new PagoFallidoException("El pago no está en estado de retención");
        }

        //Obtener cuenta bancaria del proveedor
        ProveedorBancarioResponse cuenta = perfilProveedor.obtenerCuentaBancaria(prestadorID);

        if (cuenta == null || cuenta.getNumeroCuenta() == null) {
            throw new RuntimeException(
                "El proveedor " + prestadorID + " no tiene cuenta bancaria registrada"
            );
        }
        
        //Capturar el dinero en Stripe (queda en cuenta de Worklink)
        stripeService.capturarPago(
            pago.getStripePaymentIntentId()
        );

        TransferenciaPendiente transferencia = new TransferenciaPendiente();
        transferencia.setProveedorID(
            prestadorID
        );
        transferencia.setPagoID(
            pago.getId()
        );
        transferencia.setMonto(
            pago.getMontoNeto()
        );
        transferencia.setTitular(cuenta.getTitular());
        transferencia.setBanco(cuenta.getBanco());
        transferencia.setTipoCuenta(cuenta.getTipoCuenta());
        transferencia.setNumeroCuenta(cuenta.getNumeroCuenta());
        transferencia.setDocumento(cuenta.getDocumento());
        transferenciaRepository.save(transferencia);

        //Se actualiza el pago a EXITOSO y se marca el token como usado
        pago.setEstadoPago(EstadoPago.EXITOSO);
        pago.setTokenUsado(true);
        pago.setReleasedAt(LocalDateTime.now());
        pagoRepository.save(pago);

        log.info(
            "Pago {} confirmado. Transferencia pendiente creada para proveedor {}",
            pago.getId(), prestadorID
        );

        return PagoMapper.pagoToResponse(pago,
            "Servicio confirmado. El pago será transferido al proveedor en breve."
        );
    }

    // ─────────────────────────────────────────
    // OBTENER PAGO POR TOKEN
    // ─────────────────────────────────────────
    public PagoResponse obtenerPagoPorToken(String token) {
        Optional<Pago> pagoOptional = pagoRepository.findByTokenConfirmacion(token);

        if (pagoOptional.isEmpty()) {
            PagoResponse response = new PagoResponse();
            response.setMensaje("Pago no encontrado para el token proporcionado");
            return response;
        }

        return PagoMapper.pagoToResponse(pagoOptional.get(), "Pago encontrado exitosamente");
    }

    // ─────────────────────────────────────────
    // MÉTODOS PRIVADOS
    // ─────────────────────────────────────────
    private void procesarSegunMetodo(Pago pago, PagoRequest pagoRequest) {
        switch (pago.getMetodoPago()) {
            case TARJETA -> procesarTarjeta(pago, pagoRequest);
            case PSE     -> procesarPSE(pago);
            case EFECTIVO -> procesarEfectivo(pago);
        }
    }

    private void procesarTarjeta(Pago pago, PagoRequest pagoRequest) {
        // Asignar token de Stripe antes de cobrar
        pago.setTokenPasarela(
            pagoRequest.getTarjeta().getTokenTarjeta()
        );

        PaymentIntent intent = stripeService.cobrar(pago);

        pago.setTransactionID(
            intent.getId()
        );
        pago.setStripePaymentIntentId(
            intent.getId()
        );

        log.info("Tarjeta procesada. IntentID: {}", intent.getId());
    }

    private void procesarPSE(Pago pago) {
        // Pendiente de implementación
        log.info(
            "Procesando PSE para pago {}", pago.getId()
        );
    }

    private void procesarEfectivo(Pago pago) {
        // Pendiente de implementación
        log.info(
            "Procesando efectivo para pago {}", pago.getId()
        );
    }

    private String generarTokenConfirmacion() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder token = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 8; i++) {
            token.append(caracteres.charAt(
                random.nextInt(
                    caracteres.length()
                )
            )
            );
        }
        return token.toString(); // Ej: "K7M2P9XQ"
    }
}