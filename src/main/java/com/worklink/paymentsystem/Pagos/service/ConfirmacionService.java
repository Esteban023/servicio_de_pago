package com.worklink.paymentsystem.Pagos.service;

import com.worklink.paymentsystem.Pagos.Exceptions.PagoFallidoException;
import com.worklink.paymentsystem.Pagos.Exceptions.TokenInvalidoException;
import com.worklink.paymentsystem.Pagos.dto.Response.PagoResponse;
import com.worklink.paymentsystem.Pagos.enums.EstadoPago;
import com.worklink.paymentsystem.Pagos.mapper.PagoMapper;
import com.worklink.paymentsystem.Pagos.model.Pago;
import com.worklink.paymentsystem.Pagos.model.TransferenciaPendiente;
import com.worklink.paymentsystem.Pagos.repository.PagoRepository;
import com.worklink.paymentsystem.Pagos.repository.TransferenciaPendienteRepository;
import com.worklink.paymentsystem.integrations.Response.ProveedorBancarioResponse;
import com.worklink.paymentsystem.integrations.service.PerfilProveedor;
import com.worklink.paymentsystem.integrations.service.StripeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfirmacionService {
    private final PagoRepository pagoRepository;

    private final TransferenciaPendienteRepository transferenciaRepository;

    private final StripeService stripeService;

    private final PerfilProveedor perfilProveedor;

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

        ProveedorBancarioResponse cuenta = perfilProveedor.obtenerCuentaBancaria(prestadorID);

        if (cuenta == null || cuenta.getNumeroCuenta() == null) {
            throw new RuntimeException(
                "El proveedor " + prestadorID + " no tiene cuenta bancaria registrada"
            );
        }

        //Capturar el dinero en Stripe (ya queda en cuenta de Worklink)
        stripeService.capturarPago(pago.getStripePaymentIntentId());

        TransferenciaPendiente transferencia = new TransferenciaPendiente();
        transferencia.setProveedorID(prestadorID);
        transferencia.setPagoID(pago.getId());
        transferencia.setMonto(pago.getMontoNeto()); // sin comisión de Worklink
        transferencia.setTitular(cuenta.getTitular());
        transferencia.setBanco(cuenta.getBanco());
        transferencia.setTipoCuenta(cuenta.getTipoCuenta());
        transferencia.setNumeroCuenta(cuenta.getNumeroCuenta());
        transferencia.setDocumento(cuenta.getDocumento());
        transferenciaRepository.save(transferencia);

        //Actualizar el pago
        pago.setEstadoPago(EstadoPago.EXITOSO);
        pago.setTokenUsado(true);
        pago.setReleasedAt(LocalDateTime.now());
        pagoRepository.save(pago);

        log.info(
            "Pago {} confirmado. Transferencia pendiente creada para proveedor {}",
            pago.getId(), prestadorID
        );
        
        PagoResponse response = PagoMapper.pagoToResponse(pago, "Servicio confirmado. El pago será transferido al proveedor en breve.");

        return response;
    }

    
}