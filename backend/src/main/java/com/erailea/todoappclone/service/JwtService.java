package com.erailea.todoappclone.service;

import io.jsonwebtoken.Claims;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(String userId);

    String generateToken(Map<String, Object> extraClaims, String userId);

    boolean isTokenValid(String token);
}