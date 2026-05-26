package com.worklink.paymentsystem.Pagos.controller;

import com.worklink.paymentsystem.Pagos.model.Pago;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.worklink.paymentsystem.Pagos.dto.Request.PagoRequest;
import com.worklink.paymentsystem.Pagos.dto.Response.PagoResponse;
import com.worklink.paymentsystem.Pagos.dto.Response.TransferenciaResponse;
import com.worklink.paymentsystem.Pagos.service.ConfirmacionService;
import com.worklink.paymentsystem.Pagos.service.ServicioPagos;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api-pagos")
public class ControladorPagos {

    private final ServicioPagos pagoService;
    private final ConfirmacionService confirmacionService;

    @PostMapping
    public ResponseEntity<PagoResponse> realizarPago(
        @Valid @RequestBody PagoRequest pagoRequest
    ) {
        PagoResponse pagoResponse = pagoService.procesarPago(pagoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoResponse);
    }

    @PostMapping("/confirmar")
    public ResponseEntity<TransferenciaResponse> confirmarConToken(
        @RequestParam String token,
        @RequestParam Long prestadorID
    ) {
        TransferenciaResponse response = confirmacionService.confirmarConToken(token, prestadorID);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/cliente/{clienteID}")
    public ResponseEntity<List<Pago>> obtenerPagoPorCliente(@PathVariable Long clienteID){
        List<Pago> pagos = pagoService.obtenerPorCliente(clienteID);
        return ResponseEntity.ok(pagos);
    }
}