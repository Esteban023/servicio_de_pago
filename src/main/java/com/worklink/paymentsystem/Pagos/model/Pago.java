package com.worklink.paymentsystem.Pagos.model;

import java.util.UUID;
import java.math.BigDecimal;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.worklink.paymentsystem.Pagos.enums.EstadoPago;
import com.worklink.paymentsystem.Pagos.enums.MetodoPago;

@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Long clienteID;

    @Column(nullable = false)
    private Long servicioID;

    @Column(nullable = false)
    private Long proveedorID;

    @Column(nullable = false, unique = true)
    private String referencia;

    @Column(nullable = true, unique = true)
    private String transactionID;

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(nullable = false)
    private BigDecimal comision;

    @Column(nullable = false)
    private BigDecimal montoNeto;

    @Column(nullable = false)
    private String moneda;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodoPago;

    @Column(length = 500)
    private String motivoRechazo;

    @Column(length = 500)
    private String tokenPasarela;

    @Column(length = 500, unique = true)
    private String tokenConfirmacion;

    @Column(nullable = false)
    private Boolean tokenUsado = false;

    @Column(name = "stripe_payment_intent_id", length = 255)
    private String stripePaymentIntentId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime processedAt;

    private LocalDateTime releasedAt;

    public Pago() {
        this.createdAt = LocalDateTime.now();
        this.referencia = "PAY-" + System.currentTimeMillis();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Long getClienteID() { return clienteID; }
    public void setClienteID(Long clienteID) { this.clienteID = clienteID; }

    public Long getServicioID() { return servicioID; }
    public void setServicioID(Long servicioID) { this.servicioID = servicioID; }

    public Long getProveedorID() { return proveedorID; }
    public void setProveedorID(Long proveedorID) { this.proveedorID = proveedorID; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public String getTransactionID() { return transactionID; }
    public void setTransactionID(String transactionID) { this.transactionID = transactionID; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public BigDecimal getComision() { return comision; }
    public void setComision(BigDecimal comision) { this.comision = comision; }

    public BigDecimal getMontoNeto() { return montoNeto; }
    public void setMontoNeto(BigDecimal montoNeto) { this.montoNeto = montoNeto; }

    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }

    public EstadoPago getEstadoPago() { return estado; }
    public void setEstadoPago(EstadoPago estado) { this.estado = estado; }

    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }

    public String getMotivoRechazo() { return motivoRechazo; }
    public void setMotivoRechazo(String motivoRechazo) { this.motivoRechazo = motivoRechazo; }

    public String getTokenPasarela() { return tokenPasarela; }
    public void setTokenPasarela(String tokenPasarela) { this.tokenPasarela = tokenPasarela; }

    public String getTokenConfirmacion() { return tokenConfirmacion; }
    public void setTokenConfirmacion(String tokenConfirmacion) { this.tokenConfirmacion = tokenConfirmacion; }

    public Boolean getTokenUsado() { return tokenUsado; }
    public void setTokenUsado(Boolean tokenUsado) { this.tokenUsado = tokenUsado; }

    public String getStripePaymentIntentId() { return stripePaymentIntentId; }
    public void setStripePaymentIntentId(String stripePaymentIntentId) { this.stripePaymentIntentId = stripePaymentIntentId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }

    public LocalDateTime getReleasedAt() { return releasedAt; }
    public void setReleasedAt(LocalDateTime releasedAt) { this.releasedAt = releasedAt; }
}