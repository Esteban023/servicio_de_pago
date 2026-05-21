package com.worklink.paymentsystem.integrations.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

import com.stripe.exception.CardException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;
import com.worklink.paymentsystem.Pagos.Exceptions.PagoFallidoException;
import com.worklink.paymentsystem.Pagos.model.Pago;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class StripeService {

    private static final Logger log = LoggerFactory.getLogger(StripeService.class);

    // Monedas Stripe sin decimales (https://stripe.com/docs/currencies#zero-decimal)
    private static final Set<String> ZERO_DECIMAL_CURRENCIES = Set.of(
        "BIF", "CLP", "DJF", "GNF", "JPY", "KMF", "KRW", "MGA",
        "PYG", "RWF", "UGX", "VND", "VUV", "XAF", "XOF", "XPF"
    );

    public PaymentIntent cobrar(Pago pago) {
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(toStripeMinorUnits(pago.getMonto(), pago.getMoneda()))
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

            RequestOptions opts = RequestOptions.builder()
                .setIdempotencyKey("pago-create-" + pago.getId())
                .build();

            PaymentIntent intent = PaymentIntent.create(params, opts);
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

            RequestOptions opts = RequestOptions.builder()
                .setIdempotencyKey("pago-capture-" + stripePaymentIntentId)
                .build();

            intent.capture(opts);
            log.info("PaymentIntent {} capturado exitosamente", stripePaymentIntentId);

        } catch (StripeException e) {
            log.error("Error capturando pago {}: {}", stripePaymentIntentId, e.getMessage());
            throw new PagoFallidoException("Error capturando el pago: " + e.getMessage());
        }
    }

    private long toStripeMinorUnits(BigDecimal amount, String currency) {
        String c = currency == null ? "" : currency.toUpperCase();
        BigDecimal multiplier = ZERO_DECIMAL_CURRENCIES.contains(c)
            ? BigDecimal.ONE
            : BigDecimal.valueOf(100);
        return amount.multiply(multiplier).setScale(0, RoundingMode.UNNECESSARY).longValueExact();
    }
}
