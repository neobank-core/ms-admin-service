package com.neobank.adminservice.controller;

import com.neobank.adminservice.client.CardServiceClient;
import com.neobank.adminservice.client.TransactionServiceClient;
import com.neobank.adminservice.client.UserServiceClient;
import com.neobank.adminservice.client.UnleashClient;
import com.neobank.adminservice.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class AdminController {

    private final UserServiceClient userServiceClient;
    private final TransactionServiceClient transactionServiceClient;
    private final CardServiceClient cardServiceClient;
    private final UnleashClient unleashClient;
    private final com.neobank.adminservice.client.AccountServiceClient accountServiceClient;

    private static final String UNLEASH_ADMIN_TOKEN = "*:*.unleash-insecure-api-token";

    @GetMapping("/dashboard/stats")
    public ResponseEntity<StatsResponse> getDashboardStats() {
        PageResponse<UserResponse> usersPage = userServiceClient.getAllUsers(null, null, null);
        long totalUsers = usersPage.totalElements();
        
        long activeAccounts = accountServiceClient.getActiveAccountCount();
        AdminTransactionStatsResponse txStats = transactionServiceClient.getAdminStats();
        
        StatsResponse stats = StatsResponse.builder()
                .totalUsers(totalUsers)
                .activeAccounts(activeAccounts) 
                .todayTransactionVolume(txStats.todayTransactionVolume() != null ? txStats.todayTransactionVolume().doubleValue() : 0.0)
                .todayTransactionCount(txStats.todayTransactionCount())
                .failedTransactionCount(txStats.failedTransactionCount())
                .build();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/features")
    public ResponseEntity<?> getFeatures() {
        String featuresJson = unleashClient.getAllFeatures(UNLEASH_ADMIN_TOKEN);
        return ResponseEntity.ok(featuresJson);
    }

    @PostMapping("/features/{name}/toggle")
    public ResponseEntity<?> toggleFeature(@PathVariable("name") String name, @RequestParam("enable") boolean enable) {
        log.info("AUDIT: Admin toggled feature {} to {}", name, enable);
        if (enable) {
            unleashClient.enableFeature(name, UNLEASH_ADMIN_TOKEN);
        } else {
            unleashClient.disableFeature(name, UNLEASH_ADMIN_TOKEN);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/features/{name}/rollout")
    public ResponseEntity<?> setRolloutPercentage(@PathVariable("name") String name, @RequestParam("percentage") int percentage) {
        log.info("AUDIT: Admin set rollout percentage of feature {} to {}", name, percentage);
        try {
            String strategiesJson = unleashClient.getStrategies(name, UNLEASH_ADMIN_TOKEN);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(strategiesJson);
            
            if (root.isArray()) {
                for (JsonNode strategy : root) {
                    String id = strategy.get("id").asText();
                    unleashClient.deleteStrategy(name, id, UNLEASH_ADMIN_TOKEN);
                }
            } else if (root.has("strategies") && root.get("strategies").isArray()) {
                 for (JsonNode strategy : root.get("strategies")) {
                    String id = strategy.get("id").asText();
                    unleashClient.deleteStrategy(name, id, UNLEASH_ADMIN_TOKEN);
                }
            }

            String payload = String.format("""
                {
                  "name": "flexibleRollout",
                  "parameters": {
                    "rollout": "%d",
                    "stickiness": "default",
                    "groupId": "%s"
                  }
                }
                """, percentage, name);
            unleashClient.addStrategy(name, payload, UNLEASH_ADMIN_TOKEN);
        } catch (Exception e) {
            log.error("Failed to update rollout percentage", e);
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    public ResponseEntity<PageResponse<UserResponse>> getAllUsers(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        return ResponseEntity.ok(userServiceClient.getAllUsers(search, page, size));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userServiceClient.getUserById(id));
    }

    @GetMapping("/users/{id}/accounts")
    public ResponseEntity<java.util.List<Object>> getUserAccounts(@PathVariable("id") Long id) {
        UserResponse user = userServiceClient.getUserById(id);
        return ResponseEntity.ok(accountServiceClient.getUserAccounts(java.util.UUID.fromString(user.keycloakUserId())));
    }

    @GetMapping("/users/{id}/cards")
    public ResponseEntity<java.util.List<Object>> getUserCards(@PathVariable("id") Long id) {
        UserResponse user = userServiceClient.getUserById(id);
        return ResponseEntity.ok(cardServiceClient.getUserCards(java.util.UUID.fromString(user.keycloakUserId())));
    }

    @GetMapping("/transactions")
    public ResponseEntity<PageResponse<TransactionResponse>> getAllTransactions(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        return ResponseEntity.ok(transactionServiceClient.getAllTransactions(status, type, startDate, endDate, search, page, size));
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(transactionServiceClient.getTransactionById(id));
    }

    @PostMapping("/transactions/{id}/reverse")
    public ResponseEntity<TransactionResponse> reverseTransaction(@PathVariable("id") UUID id) {
        log.info("AUDIT: Admin reversed transaction {}", id);
        return ResponseEntity.ok(transactionServiceClient.reverseTransaction(id));
    }

    @GetMapping("/cards/{id}")
    public ResponseEntity<CardResponse> getCardById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(cardServiceClient.getCardById(id));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('KYC_MANAGER')")
    @PostMapping("/kyc/{userId}/approve")
    public ResponseEntity<KycResponse> approveKyc(@PathVariable("userId") Long userId) {
        log.info("AUDIT: Admin approved KYC for user {}", userId);
        return ResponseEntity.ok(userServiceClient.approveKyc(userId));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('KYC_MANAGER')")
    @PostMapping("/kyc/{userId}/reject")
    public ResponseEntity<KycResponse> rejectKyc(@PathVariable("userId") Long userId) {
        log.info("AUDIT: Admin rejected KYC for user {}", userId);
        return ResponseEntity.ok(userServiceClient.rejectKyc(userId));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPORT')")
    @PostMapping("/users/{id}/block")
    public ResponseEntity<UserResponse> blockUser(@PathVariable("id") Long id) {
        log.info("AUDIT: Admin blocked user {}", id);
        return ResponseEntity.ok(userServiceClient.blockUser(id));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPORT')")
    @PostMapping("/users/{id}/unblock")
    public ResponseEntity<UserResponse> unblockUser(@PathVariable("id") Long id) {
        log.info("AUDIT: Admin unblocked user {}", id);
        return ResponseEntity.ok(userServiceClient.unblockUser(id));
    }
}
