package com.neobank.adminservice.client;

import com.neobank.adminservice.config.FeignConfig;
import com.neobank.adminservice.dto.KycResponse;
import com.neobank.adminservice.dto.PageResponse;
import com.neobank.adminservice.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", url = "${user-service.url}", configuration = FeignConfig.class)
@Component
public interface UserServiceClient {

    @GetMapping("/api/users")
    PageResponse<UserResponse> getAllUsers();

    @GetMapping("/api/users/{id}")
    UserResponse getUserById(@PathVariable Long id);

    @PostMapping("/api/users/{id}/block")
    UserResponse blockUser(@PathVariable("id") Long id);

    @PostMapping("/api/users/{id}/unblock")
    UserResponse unblockUser(@PathVariable("id") Long id);

    @PostMapping("/api/users/kyc/{id}/approve")
    KycResponse approveKyc(@PathVariable("id") Long id);

    @PostMapping("/api/users/kyc/{id}/reject")
    KycResponse rejectKyc(@PathVariable("id") Long id);
}
