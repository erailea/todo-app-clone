package com.erailea.todoappclone.service.impl;

import com.erailea.todoappclone.model.Note;
import com.erailea.todoappclone.service.NoteService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {
    @Override
    public Note createNote(String content, String listId) {
        // Dummy implementation
        return new Note(
            "note-" + System.currentTimeMillis(),
            content,
            false,
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(7),
            listId
        );
    }

    @Override
    public List<Note> getNotesByListId(String listId) {
        // Dummy implementation
        List<Note> notes = new ArrayList<>();
        notes.add(new Note("note-1", "Buy groceries", false, LocalDateTime.now(), LocalDateTime.now().plusDays(1), listId));
        notes.add(new Note("note-2", "Call mom", true, LocalDateTime.now(), LocalDateTime.now().plusDays(2), listId));
        return notes;
    }

    @Override
    public Note getNoteById(String noteId) {
        // Dummy implementation
        return new Note(
            noteId,
            "Sample note content",
            false,
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(7),
            "list-1"
        );
    }

    @Override
    public Note updateNote(String noteId, String content, Boolean done) {
        // Dummy implementation
        return new Note(
            noteId,
            content != null ? content : "Sample note content",
            done != null ? done : false,
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(7),
            "list-1"
        );
    }

    @Override
    public void deleteNote(String noteId) {
        // Dummy implementation - in real implementation, delete the note
    }
} 