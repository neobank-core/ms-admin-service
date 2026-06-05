package com.neobank.adminservice.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@Configuration
public class FeignConfig {

    @org.springframework.beans.factory.annotation.Value("${neobank.internal-api-key:secret-internal-key-123}")
    private String internalApiKey;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
                requestTemplate.header("Authorization", "Bearer " + jwt.getTokenValue());
            }
            requestTemplate.header("X-Internal-Api-Key", internalApiKey);
        };
    }
}