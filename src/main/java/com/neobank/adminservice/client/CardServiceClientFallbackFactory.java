package com.neobank.adminservice.client;

import com.neobank.adminservice.dto.CardResponse;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Component
public class CardServiceClientFallbackFactory implements FallbackFactory<CardServiceClient> {

    @Override
    public CardServiceClient create(Throwable cause) {
        return new CardServiceClient() {
            @Override
            public CardResponse getCardById(UUID id) {
                throw new ResponseStatusException(
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "Card Service is temporarily unavailable. Circuit breaker opened. Reason: " + cause.getMessage()
                );
            }

            @Override
            public java.util.List<Object> getUserCards(UUID userId) {
                throw new ResponseStatusException(
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "Card Service is temporarily unavailable. Circuit breaker opened. Reason: " + cause.getMessage()
                );
            }
        };
    }
}
