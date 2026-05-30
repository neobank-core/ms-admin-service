package com.neobank.adminservice.dto;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String keycloakId,
        String username,
        String email,
        String firstName,
        String lastName,
        String phone,
        LocalDateTime createdAt
) {}