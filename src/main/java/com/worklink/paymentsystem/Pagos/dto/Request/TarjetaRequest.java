package com.worklink.paymentsystem.Pagos.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class TarjetaRequest {

    // Token generado por Stripe.js en el frontend
    @NotBlank(message = "El token de la tarjeta es obligatorio")
    @Pattern(regexp = "^pm_[a-zA-Z0-9]+$", message = "Token de Stripe inválido")
    private String tokenTarjeta;

    @NotNull(message = "Los últimos 4 dígitos son obligatorios")
    @Pattern(regexp = "^\\d{4}$", message = "Debe contener exactamente 4 dígitos")
    private String ultimos4;

    @NotBlank(message = "La marca de la tarjeta es obligatoria")
    @Size(max = 20, message = "La marca no puede exceder 20 caracteres")
    private String marcaTarjeta;

    public TarjetaRequest() {}

    public TarjetaRequest(String tokenTarjeta, String ultimos4, String marcaTarjeta) {
        this.tokenTarjeta = tokenTarjeta;
        this.ultimos4 = ultimos4;
        this.marcaTarjeta = marcaTarjeta;
    }

    public String getTokenTarjeta() { return tokenTarjeta; }
    public void setTokenTarjeta(String tokenTarjeta) { this.tokenTarjeta = tokenTarjeta; }

    public String getUltimos4() { return ultimos4; }
    public void setUltimos4(String ultimos4) { this.ultimos4 = ultimos4; }

    public String getMarcaTarjeta() { return marcaTarjeta; }
    public void setMarcaTarjeta(String marcaTarjeta) { this.marcaTarjeta = marcaTarjeta; }
}