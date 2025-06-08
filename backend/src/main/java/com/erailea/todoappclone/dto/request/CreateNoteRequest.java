package com.erailea.todoappclone.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateNoteRequest {
    @NotBlank(message = "Content is required")
    @Size(min = 1, max = 1000, message = "Content must be between 1 and 1000 characters")
    private String content;

    private LocalDateTime dueDate;
} 