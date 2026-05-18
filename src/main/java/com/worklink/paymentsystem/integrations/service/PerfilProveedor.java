package com.worklink.paymentsystem.integrations.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.worklink.paymentsystem.integrations.Response.ProveedorBancarioResponse;

@FeignClient(name = "cuenta-bancaria", url = "http://localhost:8081")
public interface PerfilProveedor {
    @GetMapping("/api/perfil-servidor/cuenta-bancaria/{proveedorid}")
    ProveedorBancarioResponse obtenerCuentaBancaria(@PathVariable("proveedorid") Long proveedorId);
}

