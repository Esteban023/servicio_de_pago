package com.worklink.paymentsystem.Pagos.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.worklink.paymentsystem.Pagos.Exceptions.MontoInvalidoException;
import com.worklink.paymentsystem.Pagos.Exceptions.ServicioNotFoundException;
import com.worklink.paymentsystem.Pagos.dto.Request.PagoRequest;
import com.worklink.paymentsystem.integrations.Response.ServiceResponse;
import com.worklink.paymentsystem.integrations.service.ServicioClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PagoValidator {

    private final ServicioClient servicioClient;

    public ServiceResponse validarPago(PagoRequest pagoRequest) {

        // 1. Verificar que el servicio existe
        ServiceResponse servicio = servicioClient.buscarPorId(
            pagoRequest.getServicioID()
        );

        if (servicio == null) {
            throw new ServicioNotFoundException(
                "El servicio con ID " + pagoRequest.getServicioID() + " no fue encontrado"
            );
        }

        // 2. Verificar que el monto coincide con el precio real del servicio
        if (pagoRequest.getMonto().compareTo(servicio.getPrecio()) != 0) {
            throw new MontoInvalidoException(
                "El monto enviado (" + pagoRequest.getMonto() +
                ") no coincide con el precio del servicio (" + servicio.getPrecio() + ")"
            );
        }

        // 3. Verificar que el monto sea positivo (doble verificación a nivel de negocio)
        if (pagoRequest.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new MontoInvalidoException("El monto debe ser mayor a cero");
        }

        return servicio;
    }
}
