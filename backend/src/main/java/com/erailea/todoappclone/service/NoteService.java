package com.erailea.todoappclone.service;

import com.erailea.todoappclone.model.Note;
import java.time.LocalDateTime;
import java.util.List;

public interface NoteService {
    // List-specific operations
    Note createNote(String content, String listId, LocalDateTime dueDate, String userId);
    List<Note> getNotesByListId(String listId, String userId);

    // Note-specific operations
    Note getNoteById(String id, String userId);
    Note updateNote(String id, String content, Boolean done, LocalDateTime dueDate, String targetListId, String userId);
    void deleteNote(String id, String userId);
} 