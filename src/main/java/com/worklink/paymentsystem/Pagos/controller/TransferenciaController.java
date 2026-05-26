package com.worklink.paymentsystem.Pagos.controller;

import com.worklink.paymentsystem.Pagos.model.TransferenciaPendiente;
import com.worklink.paymentsystem.Pagos.repository.TransferenciaPendienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transferencias")
@RequiredArgsConstructor
public class TransferenciaController {

    private final TransferenciaPendienteRepository transferenciaRepository;

    // Ver todas las transferencias pendientes (panel admin de Worklink)
    @GetMapping("/pendientes")
    public ResponseEntity<List<TransferenciaPendiente>> obtenerPendientes() {
        return ResponseEntity.ok(
            transferenciaRepository.findByEstado("PENDIENTE")
        );
    }

    @GetMapping("proveedor/{proveedorID}")
    public ResponseEntity<List<TransferenciaPendiente>> obtenerPorProveedor(@PathVariable Long proveedorID){
        List<TransferenciaPendiente> transferencias = transferenciaRepository.findByProveedorID(proveedorID);
        return ResponseEntity.ok(transferencias);

    }

    // Marcar como transferido después de hacer la transferencia bancaria manual
    @PutMapping("/{id}/transferido")
    public ResponseEntity<Void> marcarComoTransferido(@PathVariable @NonNull UUID id) {
        
        TransferenciaPendiente transferencia = transferenciaRepository.findById(id)
            .orElseThrow(
                () -> new RuntimeException("Transferencia no encontrada")
        );

        transferencia.setEstado("TRANSFERIDO");
        transferencia.setTransferidoAt(LocalDateTime.now());
        transferenciaRepository.save(transferencia);

        return ResponseEntity.ok().build();
    }
}