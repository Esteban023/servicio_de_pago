package com.worklink.paymentsystem.Pagos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.worklink.paymentsystem.Pagos.dto.Request.PagoRequest;
import com.worklink.paymentsystem.Pagos.dto.Response.PagoResponse;
import com.worklink.paymentsystem.Pagos.service.ServicioPagos;

@RestController
@RequestMapping("/api-pagos")
public class ControladorPagos {

    private final ServicioPagos pagoService;

    public ControladorPagos(ServicioPagos pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    public ResponseEntity<PagoResponse> realizarPago(
        @Valid @RequestBody PagoRequest pagoRequest
    ) {
        PagoResponse pagoResponse = pagoService.procesarPago(pagoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoResponse);
    }

    @PostMapping("/confirmar")
    public ResponseEntity<PagoResponse> confirmarConToken(
        @RequestParam String token,
        @RequestParam Long prestadorID
    ) {
        PagoResponse response = pagoService.confirmarConToken(token, prestadorID);
        return ResponseEntity.ok(response);
    }
}