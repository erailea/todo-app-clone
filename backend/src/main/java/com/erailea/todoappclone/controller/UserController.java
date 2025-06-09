package com.erailea.todoappclone.controller;

import com.erailea.todoappclone.dto.response.UserInfoResponse;
import com.erailea.todoappclone.model.User;
import com.erailea.todoappclone.security.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "User management APIs")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @GetMapping("/me")
    @Operation(summary = "Get current user info", description = "Returns current authenticated user information")
    public ResponseEntity<UserInfoResponse> getCurrentUser() {
        User user = UserContext.getCurrentUser();

        UserInfoResponse response = UserInfoResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();

        return ResponseEntity.ok(response);
    }
} 