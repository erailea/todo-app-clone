package com.erailea.todoappclone.service;

import com.erailea.todoappclone.dto.request.CreateNoteRequest;
import com.erailea.todoappclone.dto.request.UpdateNoteRequest;
import com.erailea.todoappclone.model.Note;

import java.util.List;

public interface NoteService {
    // List-specific operations
    Note createNote(CreateNoteRequest request, String listId, String userId);

    List<Note> getNotesByListId(String listId, String userId);

    // Note-specific operations
    Note getNoteById(String id, String userId);

    Note updateNote(String id, UpdateNoteRequest request, String userId);

    void deleteNote(String id, String userId);
} 