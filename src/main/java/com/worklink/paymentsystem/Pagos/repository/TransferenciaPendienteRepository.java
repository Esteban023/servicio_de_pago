package com.worklink.paymentsystem.Pagos.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.worklink.paymentsystem.Pagos.model.TransferenciaPendiente;

public interface TransferenciaPendienteRepository extends JpaRepository<TransferenciaPendiente, UUID> {
    List<TransferenciaPendiente> findByEstado(String estado);
    List<TransferenciaPendiente> findByProveedorID(Long proveedorID);
}