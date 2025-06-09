package com.erailea.todoappclone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoListResponse {
    private String id;
    private String title;
    private LocalDateTime createdAt;
    private String userId;
    private List<NoteResponse> notes;
} 