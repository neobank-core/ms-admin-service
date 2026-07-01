package com.neobank.adminservice.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Object> handleFeignException(FeignException ex) {
        String message = ex.getMessage();
        if (ex.contentUTF8() != null && !ex.contentUTF8().isEmpty()) {
            message = ex.contentUTF8();
        }
        
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", ex.status() == -1 ? 500 : ex.status(),
                "error", "Service Call Failed",
                "message", message
        );
        
        return ResponseEntity.status(ex.status() == -1 ? 500 : ex.status()).body(body);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", ex.getStatusCode().value(),
                "error", ex.getStatusCode().toString(),
                "message", ex.getReason() != null ? ex.getReason() : ex.getMessage()
        );
        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }
}
