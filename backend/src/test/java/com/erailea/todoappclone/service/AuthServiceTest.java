package com.erailea.todoappclone.service;

import com.erailea.todoappclone.dto.request.AuthenticateRequest;
import com.erailea.todoappclone.dto.request.RegisterRequest;
import com.erailea.todoappclone.dto.response.AuthResponse;
import com.erailea.todoappclone.exception.BusinessException;
import com.erailea.todoappclone.exception.ResourceNotFoundException;
import com.erailea.todoappclone.fixture.TestFixtures;
import com.erailea.todoappclone.model.User;
import com.erailea.todoappclone.repository.UserRepository;
import com.erailea.todoappclone.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @Nested
    @DisplayName("register()")
    class RegisterTests {
        private RegisterRequest request;
        private User expectedUser;
        private String expectedToken;

        @BeforeEach
        void setUp() {
            request = TestFixtures.createRegisterRequest();
            expectedUser = TestFixtures.createTestUser();
            expectedToken = "test-jwt-token";
        }

        @Test
        @DisplayName("Should register user successfully when email is not taken")
        void shouldRegisterUserSuccessfully() {
            when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
            when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded-password");
            when(userRepository.save(any(User.class))).thenReturn(expectedUser);
            when(jwtService.generateToken(expectedUser.getEmail())).thenReturn(expectedToken);

            AuthResponse result = authService.register(request);

            assertNotNull(result);
            assertEquals(expectedToken, result.getToken());
            assertEquals(expectedUser.getEmail(), result.getEmail());
            assertEquals(expectedUser.getFullName(), result.getFullName());
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw BusinessException when email is already taken")
        void shouldThrowExceptionWhenEmailExists() {
            when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

            assertThrows(BusinessException.class, () -> authService.register(request));
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("authenticate()")
    class AuthenticateTests {
        private AuthenticateRequest request;
        private User expectedUser;
        private String expectedToken;

        @BeforeEach
        void setUp() {
            request = TestFixtures.createAuthenticateRequest();
            expectedUser = TestFixtures.createTestUser();
            expectedToken = "test-jwt-token";
        }

        @Test
        @DisplayName("Should authenticate user successfully with valid credentials")
        void shouldAuthenticateUserSuccessfully() {
            when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(expectedUser));
            when(jwtService.generateToken(expectedUser.getEmail())).thenReturn(expectedToken);

            AuthResponse result = authService.authenticate(request);

            assertNotNull(result);
            assertEquals(expectedToken, result.getToken());
            assertEquals(expectedUser.getId(), result.getUserId());
            assertEquals(expectedUser.getEmail(), result.getEmail());
            assertEquals(expectedUser.getFullName(), result.getFullName());
            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when user does not exist")
        void shouldThrowExceptionWhenUserDoesNotExist() {
            when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> authService.authenticate(request));
        }
    }
} 