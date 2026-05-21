package com.worklink.paymentsystem.Pagos.repository;

import com.worklink.paymentsystem.Pagos.model.TransferenciaPendiente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransferenciaPendienteRepository extends JpaRepository<TransferenciaPendiente, UUID> {
    List<TransferenciaPendiente> findByEstado(String estado);
    List<TransferenciaPendiente> findByProveedorID(Long proveedorID);
}