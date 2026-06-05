package com.neobank.adminservice.client;

import com.neobank.adminservice.config.FeignConfig;
import com.neobank.adminservice.dto.TransactionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "transaction-service", url = "${transaction-service.url}", configuration = FeignConfig.class)
@Component
public interface TransactionServiceClient {

    @GetMapping("/api/transactions/{id}")
    TransactionResponse getTransactionById(@PathVariable UUID id);

    @PostMapping("/api/transactions/internal/{id}/reverse")
    TransactionResponse reverseTransaction(@PathVariable UUID id);
}