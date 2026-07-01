package com.neobank.adminservice.client;

import com.neobank.adminservice.dto.TransactionResponse;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Component
public class TransactionServiceClientFallbackFactory implements FallbackFactory<TransactionServiceClient> {

    @Override
    public TransactionServiceClient create(Throwable cause) {
        return new TransactionServiceClient() {
            @Override
            public TransactionResponse getTransactionById(UUID id) {
                throw createException(cause);
            }

            @Override
            public TransactionResponse reverseTransaction(UUID id) {
                throw createException(cause);
            }

            @Override
            public com.neobank.adminservice.dto.PageResponse<TransactionResponse> getAllTransactions(
                    String status, String type, String startDate, String endDate, String search, Integer page, Integer size) {
                throw createException(cause);
            }

            @Override
            public com.neobank.adminservice.dto.AdminTransactionStatsResponse getAdminStats() {
                throw createException(cause);
            }

            private ResponseStatusException createException(Throwable cause) {
                return new ResponseStatusException(
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "Transaction Service is temporarily unavailable. Circuit breaker opened. Reason: " + cause.getMessage()
                );
            }
        };
    }
}
