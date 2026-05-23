package com.worklink.paymentsystem.Pagos.dto.Response;

import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransferenciaResponse {
    UUID pagoID;
    String banco;
    String titular;
    String mensaje;
    BigDecimal monto;
    String tipoCuenta;
    String numeroCuenta;
    UUID transferenciaID;
    LocalDateTime createdAt;
    String fechaTransferencia;
    String estadoTransferencia;

    public TransferenciaResponse() {
        
    }

    public UUID getPagoID() {
        return pagoID;
    }
    public void setPagoID(UUID pagoID) {
        this.pagoID = pagoID;
    }

    public String getBanco() {
        return banco;
    }
    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getTitular() {
        return titular;
    }
    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public BigDecimal getMonto() {
        return monto;
    }
    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }
    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }
    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public UUID getTransferenciaID() {
        return transferenciaID;
    }
    public void setTransferenciaID(UUID transferenciaID) {
        this.transferenciaID = transferenciaID;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getFechaTransferencia() {
        return fechaTransferencia;
    }
    public void setFechaTransferencia(String fechaTransferencia) {
        this.fechaTransferencia = fechaTransferencia;
    }

    public String getEstadoTransferencia() {
        return estadoTransferencia;
    }
    public void setEstadoTransferencia(String estadoTransferencia) {
        this.estadoTransferencia = estadoTransferencia;
    }

}
