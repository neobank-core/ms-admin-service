package com.neobank.adminservice.dto;

import java.util.UUID;

public record CardResponse(
        UUID id,
        UUID accountId,
        String cardNumberMasked,
        String cardHolderName,
        String cardType,
        String status
) {}