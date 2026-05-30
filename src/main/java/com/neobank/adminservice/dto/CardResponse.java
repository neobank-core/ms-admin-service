package com.neobank.adminservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CardResponse(
        UUID id,
        UUID accountId,
        String cardNumberMasked,
        String cardHolderName,
        Integer expiryMonth,
        Integer expiryYear,
        String cardType,
        String cardStatus,
        LocalDateTime createdAt
) {}
