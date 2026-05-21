package com.worklink.paymentsystem.Pagos.repository;

import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.worklink.paymentsystem.Pagos.model.Pago;


public interface PagoRepository extends JpaRepository<Pago, UUID> {
    Optional<Pago> findByReferencia(String referencia);
    Optional<Pago> findByTokenConfirmacion(String token);
    Optional<Pago> findByStripePaymentIntentId(String paymentIntentId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Pago p WHERE p.tokenConfirmacion = :token")
    Optional<Pago> findByTokenConfirmacionForUpdate(@Param("token") String token);
}
