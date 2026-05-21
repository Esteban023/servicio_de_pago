package com.worklink.paymentsystem.integrations.Response;

import lombok.Data;

@Data
public class ProveedorBancarioResponse {
    private Long proveedorID;
    private String titular;
    private String banco;
    private String tipoCuenta;
    private String numeroCuenta;
    private String documento;
}