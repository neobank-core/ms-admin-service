package com.neobank.adminservice.client;

import com.neobank.adminservice.config.FeignConfig;
import com.neobank.adminservice.dto.CardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(
        name = "card-service",
        configuration = FeignConfig.class,
        fallbackFactory = CardServiceClientFallbackFactory.class
)
@Component
public interface CardServiceClient {

    @GetMapping("/api/cards/{id}")
    CardResponse getCardById(@PathVariable("id") UUID id);

    @GetMapping("/api/internal/cards/user/{userId}")
    java.util.List<Object> getUserCards(@PathVariable("userId") UUID userId);
}
