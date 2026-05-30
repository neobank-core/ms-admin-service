package com.neobank.adminservice.client;

import com.neobank.adminservice.config.FeignConfig;
import com.neobank.adminservice.dto.CardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "card-service",
        url = "${card-service.url}",
        configuration = FeignConfig.class
)
@Component
public interface CardServiceClient {

    @GetMapping("/api/cards/{id}")
    CardResponse getCardById(@PathVariable UUID id);
}