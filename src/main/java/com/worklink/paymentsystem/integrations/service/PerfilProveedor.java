package com.worklink.paymentsystem.integrations.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.worklink.paymentsystem.integrations.Response.ProveedorBancarioResponse;

@FeignClient(name = "cuenta-bancaria", url = "http://localhost:8081")
public interface PerfilProveedor {
    @GetMapping("/api/perfil-servidor/perfil/proveedor/{proveedorID}/cuenta-bancaria")
    ProveedorBancarioResponse obtenerCuentaBancaria(@PathVariable("proveedorID") Long proveedorID);
}

