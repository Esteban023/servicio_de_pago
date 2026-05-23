package com.worklink.paymentsystem.integrations.Response;

import java.math.BigDecimal;

public class ServiceResponse {

    private String titulo;
    private BigDecimal precio;
    private Long proveedorId;

    public ServiceResponse() {

    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Long getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Long proveedorId) {
        this.proveedorId = proveedorId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

}
