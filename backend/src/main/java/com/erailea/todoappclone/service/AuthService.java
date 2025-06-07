package com.erailea.todoappclone.service;

import com.erailea.todoappclone.dto.request.AuthenticateRequest;
import com.erailea.todoappclone.dto.request.RegisterRequest;
import com.erailea.todoappclone.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse authenticate(AuthenticateRequest request);
} 