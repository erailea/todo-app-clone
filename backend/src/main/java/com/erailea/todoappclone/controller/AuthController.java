package com.erailea.todoappclone.controller;

import com.erailea.todoappclone.dto.request.AuthenticateRequest;
import com.erailea.todoappclone.dto.request.RegisterRequest;
import com.erailea.todoappclone.dto.response.AuthResponse;
import com.erailea.todoappclone.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account and returns authentication token")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/authenticate")
    @Operation(summary = "Authenticate a user", description = "Authenticates a user and returns authentication token")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthenticateRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
} 