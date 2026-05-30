package com.neobank.adminservice.dto;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record KycResponse(
        Long id,
        Long userId,
        String documentType,
        String status,
        LocalDateTime submittedAt,
        LocalDateTime verifiedAt
) {
}
