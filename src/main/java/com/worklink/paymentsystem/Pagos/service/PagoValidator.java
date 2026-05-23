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

        //Verificar que el servicio existe
        ServiceResponse servicio = servicioClient.buscarPorId(
            pagoRequest.getServicioID()
        );

        if (servicio == null) {
            throw new ServicioNotFoundException(
                "El servicio con ID " + pagoRequest.getServicioID() + " no fue encontrado"
            );
        }

        BigDecimal multiploCien = new BigDecimal(100);
        if (pagoRequest.getMonto().remainder(multiploCien).compareTo(BigDecimal.ZERO) != 0) {
            throw new MontoInvalidoException("El monto debe ser múltiplo de 100 pesos");
        }

        if (pagoRequest.getMonto().compareTo(new BigDecimal(1000)) < 0) {
            throw new MontoInvalidoException("El monto mínimo de cobro es 1000 COP");
        }

        return servicio;
    }
}
