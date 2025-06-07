package com.erailea.todoappclone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoList {
    private String id;
    private String title;
    private LocalDateTime createdAt;
    private String userId;
} 