package com.worklink.paymentsystem.Pagos.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.worklink.paymentsystem.Pagos.model.Pago;


public interface PagoRepository extends JpaRepository<Pago, UUID> {
    Optional<Pago> findByReferencia(String referencia);
    Optional<Pago> findByTokenConfirmacion(String token);
    Optional<Pago> findByStripePaymentIntentId(String paymentIntentId);
}
