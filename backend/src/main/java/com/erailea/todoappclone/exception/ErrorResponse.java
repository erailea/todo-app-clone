package com.erailea.todoappclone.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String errorCode;
    private String message;
    private List<ValidationError> validationErrors;
    private String path;

    @Data
    @Builder
    public static class ValidationError {
        private String field;
        private String message;
    }
} 