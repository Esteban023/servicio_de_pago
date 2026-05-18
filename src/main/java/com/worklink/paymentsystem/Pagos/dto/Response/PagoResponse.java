package com.worklink.paymentsystem.Pagos.dto.Response;

import java.math.BigDecimal;
import java.util.UUID;

public class PagoResponse {

    private UUID pagoID;
    private String mensaje;
    private Long clienteID;
    private Long proveedorID;
    private Long servicioID;
    private String fechaPago;
    private String estadoPago;
    private String metodoPago;
    private BigDecimal montoPago;
    
    public UUID getPagoID() {
        return pagoID;
    }

    public void setPagoID(UUID pagoID) {
        this.pagoID = pagoID;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Long getClienteID() {
        return clienteID;
    }

    public void setClienteID(Long clienteID) {
        this.clienteID = clienteID;
    }

    public Long getProveedorID() {
        return proveedorID;
    }

    public void setProveedorID(Long proveedorID) {
        this.proveedorID = proveedorID;
    }

    public Long getServicioID() {
        return servicioID;
    }

    public void setServicioID(Long servicioID) {
        this.servicioID = servicioID;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public BigDecimal getMonto() {
        return montoPago;
    }

    public void setMonto(BigDecimal montoPago) {
        this.montoPago = montoPago;
    }

}
