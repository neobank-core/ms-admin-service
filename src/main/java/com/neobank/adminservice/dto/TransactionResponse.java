package com.neobank.adminservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        BigDecimal amount,
        String currency,
        String status,
        String type,
        UUID senderCardId,
        UUID receiverCardId,
        LocalDateTime createdAt
) {}