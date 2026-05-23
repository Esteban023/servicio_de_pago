package com.worklink.paymentsystem.Pagos.mapper;

import com.worklink.paymentsystem.Pagos.model.TransferenciaPendiente;
import com.worklink.paymentsystem.Pagos.dto.Response.TransferenciaResponse;

public class TransferenciaMapper {

    public static TransferenciaResponse transferenciaToResponse(TransferenciaPendiente transferencia, String mensaje) {
        
        TransferenciaResponse response = new TransferenciaResponse();
        response.setPagoID(
            transferencia.getPagoID()
        );
        response.setBanco(
            transferencia.getBanco()
        );
        response.setTitular(
            transferencia.getTitular()
        );
        response.setMensaje(
            mensaje
        );
        response.setMonto(
            transferencia.getMonto()
        );
        response.setTipoCuenta(
            transferencia.getTipoCuenta()
        );
        response.setNumeroCuenta(
            transferencia.getNumeroCuenta()
        );
        response.setTransferenciaID(
            transferencia.getId()
        );
        response.setCreatedAt(
            transferencia.getCreatedAt()
        );
        response.setFechaTransferencia(
            transferencia.getCreatedAt().toString()
        );
        response.setEstadoTransferencia(
            transferencia.getEstado()
        );

        return response;
    }

}
