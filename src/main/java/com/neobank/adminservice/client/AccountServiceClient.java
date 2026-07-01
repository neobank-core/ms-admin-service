package com.neobank.adminservice.client;

import com.neobank.adminservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "account-service", 
        configuration = FeignConfig.class
)
@Component
public interface AccountServiceClient {
    @GetMapping("/api/internal/accounts/stats/active-count")
    Long getActiveAccountCount();

    @GetMapping("/api/internal/accounts/user/{userId}")
    java.util.List<Object> getUserAccounts(@org.springframework.web.bind.annotation.PathVariable("userId") java.util.UUID userId);
}
