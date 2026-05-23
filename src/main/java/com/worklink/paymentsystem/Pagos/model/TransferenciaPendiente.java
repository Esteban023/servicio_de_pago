package com.worklink.paymentsystem.Pagos.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transferencias_pendientes")
public class TransferenciaPendiente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Long proveedorID;

    @Column(nullable = false)
    private UUID pagoID;

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(nullable = false)
    private String titular;

    @Column(nullable = false)
    private String banco;

    @Column(nullable = false)
    private String tipoCuenta;

    @Column(nullable = false)
    private String numeroCuenta;

    @Column(nullable = false)
    private String documento;

    @Column(nullable = false)
    private String estado; // "PENDIENTE", "TRANSFERIDO"

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime transferidoAt;

    public TransferenciaPendiente() {
        this.createdAt = LocalDateTime.now();
        this.estado = "PENDIENTE";
    }

    // Getters y setters
    public void setId(UUID id) { this.id = id; }
    public UUID getId() { return id; }
    public Long getProveedorID() { return proveedorID; }
    public void setProveedorID(Long proveedorID) { this.proveedorID = proveedorID; }
    public UUID getPagoID() { return pagoID; }
    public void setPagoID(UUID pagoID) { this.pagoID = pagoID; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public String getTitular() { return titular; }
    public void setTitular(String titular) { this.titular = titular; }
    public String getBanco() { return banco; }
    public void setBanco(String banco) { this.banco = banco; }
    public String getTipoCuenta() { return tipoCuenta; }
    public void setTipoCuenta(String tipoCuenta) { this.tipoCuenta = tipoCuenta; }
    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getTransferidoAt() { return transferidoAt; }
    public void setTransferidoAt(LocalDateTime transferidoAt) { this.transferidoAt = transferidoAt; }
}