package com.worklink.paymentsystem.integrations.service;

import com.stripe.exception.CardException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;
import com.worklink.paymentsystem.Pagos.Exceptions.PagoFallidoException;
import com.worklink.paymentsystem.Pagos.model.Pago;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class StripeService {

    private static final Logger log = LoggerFactory.getLogger(StripeService.class);

    @Retry(name = "stripe", fallbackMethod = "cobrarFallback")
    @CircuitBreaker(name = "stripe")
    public PaymentIntent cobrar(Pago pago) {
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(pago.getMonto().longValue() * 100L)
                .setCurrency(pago.getMoneda().toLowerCase())
                .setPaymentMethod(pago.getTokenPasarela())
                .setConfirm(true)
                .setCaptureMethod(PaymentIntentCreateParams.CaptureMethod.MANUAL)
                .addPaymentMethodType("card")
                .setDescription("Pago WorkLink - Ref: " + pago.getReferencia())
                .putMetadata("pagoID", pago.getId().toString())
                .putMetadata("referencia", pago.getReferencia())
                .putMetadata("servicioID", pago.getServicioID().toString())
                .putMetadata("clienteID", pago.getClienteID().toString())
                .build();

            // 🔑 Clave de idempotencia derivada del ID interno del pago
            RequestOptions options = RequestOptions.builder()
            .setIdempotencyKey(
                "pago_" + pago.getId()
            )
            .build();

            PaymentIntent intent = PaymentIntent.create(params, options);
            log.info("PaymentIntent creado en modo retención: {}", intent.getId());
            return intent;

        } catch (CardException e) {
            log.warn("Tarjeta rechazada para pago {}: {}", pago.getId(), e.getMessage());
            throw new PagoFallidoException("Tarjeta rechazada: " + e.getMessage());

        } catch (StripeException e) {
            log.error("Error de Stripe para pago {}: {}", pago.getId(), e.getMessage());
            throw new PagoFallidoException("Error al procesar el pago con Stripe: " + e.getMessage());
        }
    }

    public void capturarPago(String stripePaymentIntentId) {
    try {
        PaymentIntent intent = PaymentIntent.retrieve(stripePaymentIntentId);

        // Si ya fue capturado, no hacer nada (idempotencia)
        if ("succeeded".equals(intent.getStatus())) {
            log.info("PaymentIntent {} ya estaba capturado, se omite la captura", stripePaymentIntentId);
            return;
        }

        // Solo capturar si está en el estado esperado
        if (!"requires_capture".equals(intent.getStatus())) {
            log.warn(
                "PaymentIntent {} no está en estado capturable (estado actual: {})",
                stripePaymentIntentId, intent.getStatus()
            );
            throw new PagoFallidoException(
                "El pago no se puede capturar en su estado actual: " + intent.getStatus()
            );
        }

        intent.capture();
        log.info("PaymentIntent {} capturado exitosamente", stripePaymentIntentId);

    } catch (StripeException e) {
        log.error("Error capturando pago {}: {}", stripePaymentIntentId, e.getMessage());
        throw new PagoFallidoException("Error capturando el pago: " + e.getMessage());
    }
}
}
