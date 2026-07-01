package com.neobank.adminservice.client;

import com.neobank.adminservice.config.FeignConfig;
import com.neobank.adminservice.dto.TransactionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(
        name = "transaction-service", 
        configuration = FeignConfig.class,
        fallbackFactory = TransactionServiceClientFallbackFactory.class
)
@Component
public interface TransactionServiceClient {

    @GetMapping("/api/transactions/{id}")
    TransactionResponse getTransactionById(@PathVariable("id") UUID id);

    @PostMapping("/api/transactions/internal/{id}/reverse")
    TransactionResponse reverseTransaction(@PathVariable("id") UUID id);

    @GetMapping("/api/transactions/admin")
    com.neobank.adminservice.dto.PageResponse<TransactionResponse> getAllTransactions(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size);

    @GetMapping("/api/transactions/admin/stats")
    com.neobank.adminservice.dto.AdminTransactionStatsResponse getAdminStats();
}
