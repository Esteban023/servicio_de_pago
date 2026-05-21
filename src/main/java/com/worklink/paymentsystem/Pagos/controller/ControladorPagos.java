package com.worklink.paymentsystem.Pagos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.worklink.paymentsystem.Pagos.dto.Request.PagoRequest;
import com.worklink.paymentsystem.Pagos.dto.Response.PagoResponse;
<<<<<<< HEAD
import com.worklink.paymentsystem.Pagos.service.ConfirmacionService;
=======
>>>>>>> d5792b3586c3433efe7aea569c80ee6e4fa66f48
import com.worklink.paymentsystem.Pagos.service.ServicioPagos;

@RestController
@RequestMapping("/api-pagos")
public class ControladorPagos {

    private final ServicioPagos pagoService;
<<<<<<< HEAD
    private final ConfirmacionService confirmacionService;

    public ControladorPagos(ServicioPagos pagoService, ConfirmacionService confirmacionService) {
        this.pagoService = pagoService;
        this.confirmacionService = confirmacionService;
=======

    public ControladorPagos(ServicioPagos pagoService) {
        this.pagoService = pagoService;
>>>>>>> d5792b3586c3433efe7aea569c80ee6e4fa66f48
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
<<<<<<< HEAD
        PagoResponse response = confirmacionService.confirmarConToken(token, prestadorID);
=======
        PagoResponse response = pagoService.confirmarConToken(token, prestadorID);
>>>>>>> d5792b3586c3433efe7aea569c80ee6e4fa66f48
        return ResponseEntity.ok(response);
    }
}