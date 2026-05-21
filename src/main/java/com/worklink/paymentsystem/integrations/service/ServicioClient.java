package com.worklink.paymentsystem.integrations.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.worklink.paymentsystem.integrations.Response.ServiceResponse;

@FeignClient(name = "servicio", url = "http://localhost:8082")
public interface ServicioClient {

    @GetMapping("/servicio/get/{id}")
    ServiceResponse buscarPorId(@PathVariable("id") Long servicioId);

}