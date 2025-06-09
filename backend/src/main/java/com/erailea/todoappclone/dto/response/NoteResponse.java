package com.erailea.todoappclone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteResponse {
    private String id;
    private String content;
    private boolean done;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private String listId;
} 