package com.neobank.adminservice.client;

import com.neobank.adminservice.config.FeignConfig;
import com.neobank.adminservice.dto.KycResponse;
import com.neobank.adminservice.dto.PageResponse;
import com.neobank.adminservice.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "user-service", 
        configuration = FeignConfig.class,
        fallbackFactory = UserServiceClientFallbackFactory.class
)
@Component
public interface UserServiceClient {

    @GetMapping("/api/users")
    PageResponse<UserResponse> getAllUsers(@RequestParam(value = "search", required = false) String search,
                                           @RequestParam(value = "page", required = false) Integer page,
                                           @RequestParam(value = "size", required = false) Integer size);

    @GetMapping("/api/users/{id}")
    UserResponse getUserById(@PathVariable("id") Long id);

    @PostMapping("/api/users/{id}/block")
    UserResponse blockUser(@PathVariable("id") Long id);

    @PostMapping("/api/users/{id}/unblock")
    UserResponse unblockUser(@PathVariable("id") Long id);

    @PostMapping("/api/users/kyc/{userId}/approve")
    KycResponse approveKyc(@PathVariable("userId") Long userId);

    @PostMapping("/api/users/kyc/{userId}/reject")
    KycResponse rejectKyc(@PathVariable("userId") Long userId);
}
