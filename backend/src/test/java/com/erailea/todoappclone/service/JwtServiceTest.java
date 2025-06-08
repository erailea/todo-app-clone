package com.erailea.todoappclone.service;

import com.erailea.todoappclone.fixture.TestFixtures;
import com.erailea.todoappclone.service.impl.JwtServiceImpl;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtServiceImpl jwtService;

    private static final String TEST_SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final long TEST_EXPIRATION = 86400000; // 24 hours in milliseconds

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secretKey", TEST_SECRET_KEY);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", TEST_EXPIRATION);
    }

    @Nested
    @DisplayName("generateToken()")
    class GenerateTokenTests {
        @Test
        @DisplayName("Should generate token successfully with user ID")
        void shouldGenerateTokenWithUserId() {
            String token = jwtService.generateToken(TestFixtures.TEST_USER_EMAIL);

            assertNotNull(token);
            assertTrue(token.split("\\.").length == 3); // JWT has 3 parts
            assertEquals(TestFixtures.TEST_USER_EMAIL, jwtService.extractUsername(token));
        }

        @Test
        @DisplayName("Should generate token successfully with extra claims")
        void shouldGenerateTokenWithExtraClaims() {
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("role", "ADMIN");

            String token = jwtService.generateToken(extraClaims, TestFixtures.TEST_USER_EMAIL);

            assertNotNull(token);
            assertTrue(token.split("\\.").length == 3);
            assertEquals(TestFixtures.TEST_USER_EMAIL, jwtService.extractUsername(token));
        }
    }

    @Nested
    @DisplayName("extractUsername()")
    class ExtractUsernameTests {
        @Test
        @DisplayName("Should extract username from token")
        void shouldExtractUsername() {
            String token = jwtService.generateToken(TestFixtures.TEST_USER_EMAIL);

            String username = jwtService.extractUsername(token);

            assertEquals(TestFixtures.TEST_USER_EMAIL, username);
        }
    }

    @Nested
    @DisplayName("isTokenValid()")
    class IsTokenValidTests {
        @Test
        @DisplayName("Should return true for valid token")
        void shouldReturnTrueForValidToken() {
            String token = jwtService.generateToken(TestFixtures.TEST_USER_EMAIL);

            boolean isValid = jwtService.isTokenValid(token);

            assertTrue(isValid);
        }

        @Test
        @DisplayName("Should return false for invalid token")
        void shouldReturnFalseForInvalidToken() {
            boolean isValid = jwtService.isTokenValid("invalid.token.here");

            assertFalse(isValid);
        }
    }

    @Nested
    @DisplayName("extractClaim()")
    class ExtractClaimTests {
        @Test
        @DisplayName("Should extract claim from token")
        void shouldExtractClaim() {
            String token = jwtService.generateToken(TestFixtures.TEST_USER_EMAIL);

            Date expiration = jwtService.extractClaim(token, Claims::getExpiration);

            assertNotNull(expiration);
            assertTrue(expiration.after(new Date()));
        }
    }
} 