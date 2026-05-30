package com.neobank.adminservice.client;

import com.neobank.adminservice.config.FeignConfig;
import com.neobank.adminservice.dto.PageResponse;
import com.neobank.adminservice.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(
        name = "user-service",
        url = "${user-service.url}",
        configuration = FeignConfig.class
)
@Component
public interface UserServiceClient {

    @GetMapping("/api/users")
    PageResponse<UserResponse> getAllUsers();

    @GetMapping("/api/users/{id}")
    UserResponse getUserById(@PathVariable UUID id);

//    @PostMapping("/api/users/{id}/block")
//    void blockUser(@PathVariable UUID id);
//
//    @PostMapping("/api/users/{id}/unblock")
//    void unblockUser(@PathVariable UUID id);
}