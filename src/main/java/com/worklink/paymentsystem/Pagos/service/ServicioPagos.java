package com.worklink.paymentsystem.Pagos.service;

import org.slf4j.Logger;
import java.util.Optional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.security.SecureRandom;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import com.worklink.paymentsystem.Pagos.Exceptions.PagoFallidoException;
import com.worklink.paymentsystem.Pagos.dto.Request.PagoRequest;
import com.worklink.paymentsystem.Pagos.dto.Response.PagoResponse;
import com.worklink.paymentsystem.Pagos.enums.EstadoPago;
import com.worklink.paymentsystem.Pagos.mapper.PagoMapper;
import com.worklink.paymentsystem.Pagos.model.Pago;
import com.worklink.paymentsystem.Pagos.repository.PagoRepository;
import com.worklink.paymentsystem.integrations.Response.ServiceResponse;
import com.worklink.paymentsystem.integrations.service.StripeService;


@Service
@RequiredArgsConstructor
public class ServicioPagos {

    private final StripeService stripeService;
    private final PagoValidator pagoValidator;
    private final PagoRepository pagoRepository;
    private static final Logger log = LoggerFactory.getLogger(ServicioPagos.class);

    @Value("${worklink.comision.porcentaje:0.10}")
    private BigDecimal comisionPorcentaje;
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

        BigDecimal comision = pagoRequest.getMonto().multiply(comisionPorcentaje).setScale(2, RoundingMode.HALF_UP);
        BigDecimal montoNeto = pagoRequest.getMonto().subtract(comision);

        Pago pago = new Pago();
        pago.setClienteID(
            pagoRequest.getClienteID()
        );
        pago.setProveedorID(
            servicio.getProveedorId()
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
            + servicio.getTitulo()
        );
    }

    // ─────────────────────────────────────────
    // OBTENER PAGO POR TOKEN
    // ─────────────────────────────────────────
    public PagoResponse obtenerPagoPorToken(String token) {
        Optional<Pago> pagoOptional = pagoRepository.findByTokenConfirmacionForUpdate(token);

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
        
        pago.setStripePaymentIntentId(
            intent.getId()
        );

        log.info("Tarjeta procesada. IntentID: {}", intent.getId());
    }

    private void procesarPSE(Pago pago) {
        // Pendiente de implementación
        throw new UnsupportedOperationException("El método de pago PSE aún no está disponible");
    }

    private void procesarEfectivo(Pago pago) {
        // Pendiente de implementación
        throw new UnsupportedOperationException("El método de pago en efectivo aún no está disponible");
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