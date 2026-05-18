package com.worklink.paymentsystem.integrations.Response;

import java.math.BigDecimal;

public class ServiceResponse {

    private String nombreServicio;
    private BigDecimal precio;
    private Long idProveedor;

    public ServiceResponse() {

    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

}
