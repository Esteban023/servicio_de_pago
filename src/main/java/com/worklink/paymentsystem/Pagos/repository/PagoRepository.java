package com.worklink.paymentsystem.Pagos.repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import com.worklink.paymentsystem.Pagos.model.Pago;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagoRepository extends JpaRepository<Pago, UUID> {
    Optional<Pago> findByReferencia(String referencia);

    // En PagoRepository
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Pago p WHERE p.tokenConfirmacion = :token")
    Optional<Pago> findByTokenConfirmacionForUpdate(@Param("token") String token);
    
    Optional<Pago> findByStripePaymentIntentId(String paymentIntentId);
    List<Pago> findByClienteID(Long clienteId);
}
