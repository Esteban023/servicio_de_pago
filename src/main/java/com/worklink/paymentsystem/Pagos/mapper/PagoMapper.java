package com.worklink.paymentsystem.Pagos.mapper;

import com.worklink.paymentsystem.Pagos.dto.Response.PagoResponse;
import com.worklink.paymentsystem.Pagos.model.Pago;

public class PagoMapper {
    
    public static PagoResponse pagoToResponse(Pago pago, String mensaje) {
        
        PagoResponse response = new PagoResponse();
        response.setPagoID(pago.getId());
        response.setMensaje(mensaje);
        response.setClienteID(pago.getClienteID());
        response.setProveedorID(pago.getProveedorID());
        response.setServicioID(pago.getServicioID());
        response.setFechaPago(pago.getCreatedAt().toString());
        response.setEstadoPago(pago.getEstadoPago().name());
        response.setMetodoPago(pago.getMetodoPago().name());
        response.setMonto(pago.getMonto());
        response.setTokenConfirmacion(pago.getTokenConfirmacion());
        
        return response;
    }



}
