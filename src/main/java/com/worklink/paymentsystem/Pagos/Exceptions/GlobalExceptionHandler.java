package com.worklink.paymentsystem.Pagos.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServicioNotFoundException.class)
    public ResponseEntity<String> handleServicioNotFound(ServicioNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(MontoInvalidoException.class)
    public ResponseEntity<String> handleMontoInvalido(MontoInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(PagoFallidoException.class)
    public ResponseEntity<String> handlePagoFallido(PagoFallidoException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(TokenInvalidoException.class)
    public ResponseEntity<String> handleTokenInvalido(TokenInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    
}