package com.neobank.adminservice.dto;

public record UserResponse(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        String keycloakUserId,
        boolean isBlocked,
        String phone,
        String kycStatus
) {}
