package com.neobank.adminservice.client;

import com.neobank.adminservice.dto.KycResponse;
import com.neobank.adminservice.dto.PageResponse;
import com.neobank.adminservice.dto.UserResponse;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceClientFallbackFactory implements FallbackFactory<UserServiceClient> {

    @Override
    public UserServiceClient create(Throwable cause) {
        return new UserServiceClient() {
            @Override
            public PageResponse<UserResponse> getAllUsers(String search, Integer page, Integer size) {
                throw createException(cause);
            }

            @Override
            public UserResponse getUserById(Long id) {
                throw createException(cause);
            }

            @Override
            public UserResponse blockUser(Long id) {
                throw createException(cause);
            }

            @Override
            public UserResponse unblockUser(Long id) {
                throw createException(cause);
            }

            @Override
            public KycResponse approveKyc(Long userId) {
                throw createException(cause);
            }

            @Override
            public KycResponse rejectKyc(Long userId) {
                throw createException(cause);
            }

            private ResponseStatusException createException(Throwable cause) {
                return new ResponseStatusException(
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "User Service is temporarily unavailable. Circuit breaker opened. Reason: " + cause.getMessage()
                );
            }
        };
    }
}
