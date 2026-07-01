package com.neobank.adminservice.client;

import com.neobank.adminservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "unleash-service",
        url = "http://unleash:4242",
        configuration = FeignConfig.class
)
@Component
public interface UnleashClient {

    @GetMapping("/api/admin/projects/default/features")
    String getAllFeatures(@RequestHeader("Authorization") String token);

    @PostMapping("/api/admin/projects/default/features/{featureName}/environments/default/on")
    String enableFeature(@PathVariable("featureName") String featureName, @RequestHeader("Authorization") String token);

    @PostMapping("/api/admin/projects/default/features/{featureName}/environments/default/off")
    String disableFeature(@PathVariable("featureName") String featureName, @RequestHeader("Authorization") String token);

    @PostMapping(value = "/api/admin/projects/default/features/{featureName}/environments/default/strategies", consumes = "application/json")
    String addStrategy(@PathVariable("featureName") String featureName, @RequestBody String strategyJson, @RequestHeader("Authorization") String token);

    @GetMapping("/api/admin/projects/default/features/{featureName}/environments/default/strategies")
    String getStrategies(@PathVariable("featureName") String featureName, @RequestHeader("Authorization") String token);

    @DeleteMapping("/api/admin/projects/default/features/{featureName}/environments/default/strategies/{strategyId}")
    String deleteStrategy(@PathVariable("featureName") String featureName, @PathVariable("strategyId") String strategyId, @RequestHeader("Authorization") String token);
}
