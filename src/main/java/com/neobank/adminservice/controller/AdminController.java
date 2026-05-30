package com.neobank.adminservice.controller;

import com.neobank.adminservice.client.CardServiceClient;
import com.neobank.adminservice.client.TransactionServiceClient;
import com.neobank.adminservice.client.UserServiceClient;
import com.neobank.adminservice.dto.CardResponse;
import com.neobank.adminservice.dto.PageResponse;
import com.neobank.adminservice.dto.TransactionResponse;
import com.neobank.adminservice.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserServiceClient userServiceClient;
    private final TransactionServiceClient transactionServiceClient;
    private final CardServiceClient cardServiceClient;

    @GetMapping("/users")
    public ResponseEntity<PageResponse<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userServiceClient.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userServiceClient.getUserById(id));
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable UUID id) {
        return ResponseEntity.ok(transactionServiceClient.getTransactionById(id));
    }

    @GetMapping("/cards/{id}")
    public ResponseEntity<CardResponse> getCardById(@PathVariable UUID id) {
        return ResponseEntity.ok(cardServiceClient.getCardById(id));
    }
}