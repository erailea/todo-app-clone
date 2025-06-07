package com.erailea.todoappclone.service.impl;

import com.erailea.todoappclone.dto.request.AuthenticateRequest;
import com.erailea.todoappclone.dto.request.RegisterRequest;
import com.erailea.todoappclone.dto.response.AuthResponse;
import com.erailea.todoappclone.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public AuthResponse register(RegisterRequest request) {
        // Dummy implementation
        return new AuthResponse(
            "dummy-token-" + System.currentTimeMillis(),
            "user-" + System.currentTimeMillis(),
            request.getEmail(),
            request.getFullName()
        );
    }

    @Override
    public AuthResponse authenticate(AuthenticateRequest request) {
        // Dummy implementation
        return new AuthResponse(
            "dummy-token-" + System.currentTimeMillis(),
            "user-123",
            request.getEmail(),
            "John Doe"
        );
    }
} 