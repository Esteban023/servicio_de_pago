package com.worklink.paymentsystem.Pagos.dto.Request;

import java.math.BigDecimal;
import com.worklink.paymentsystem.Pagos.enums.MetodoPago;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PagoRequest {

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteID;

    @NotNull(message = "El ID del servicio es obligatorio")
    private Long servicioID;

    // En PagoRequest
    @NotNull
    @Positive
    @Digits(integer = 10, fraction = 0, message = "El monto debe ser en pesos enteros, sin decimales")
    private BigDecimal monto;

    @NotNull(message = "El método de pago es obligatorio")
    private MetodoPago metodoPago;

    @Valid
    @NotNull(message = "La información de la tarjeta es obligatoria para pagos con tarjeta")
    private TarjetaRequest tarjeta; // nullable si metodoPago es PSE o EFECTIVO

    public Long getClienteID() { return clienteID; }
    public void setClienteID(Long clienteID) { this.clienteID = clienteID; }

    public Long getServicioID() { return servicioID; }
    public void setServicioID(Long servicioID) { this.servicioID = servicioID; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }

    public TarjetaRequest getTarjeta() { return tarjeta; }
    public void setTarjeta(TarjetaRequest tarjeta) { this.tarjeta = tarjeta; }
}