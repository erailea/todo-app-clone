package com.erailea.todoappclone.dto.request;

import lombok.Data;

@Data
public class AuthenticateRequest {
    private String email;
    private String password;
} 