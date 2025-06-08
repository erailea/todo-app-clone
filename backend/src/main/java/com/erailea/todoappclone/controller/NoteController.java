package com.erailea.todoappclone.controller;

import com.erailea.todoappclone.dto.request.CreateNoteRequest;
import com.erailea.todoappclone.dto.request.UpdateNoteRequest;
import com.erailea.todoappclone.model.Note;
import com.erailea.todoappclone.security.UserContext;
import com.erailea.todoappclone.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Notes", description = "Note management APIs")
@SecurityRequirement(name = "bearerAuth")
public class NoteController {
    private final NoteService noteService;

    @GetMapping("/lists/{id}/notes")
    @Operation(summary = "Get all notes in a list", description = "Retrieves all notes in a specific todo list")
    public ResponseEntity<List<Note>> getNotesByList(@PathVariable String id) {
        String userId = UserContext.getCurrentUserId();
        return ResponseEntity.ok(noteService.getNotesByListId(id, userId));
    }

    @PostMapping("/lists/{id}/notes")
    @Operation(summary = "Create a new note", description = "Creates a new note in a specific todo list")
    public ResponseEntity<Note> createNote(
            @PathVariable String id,
            @Valid @RequestBody CreateNoteRequest request) {
        String userId = UserContext.getCurrentUserId();
        return ResponseEntity.ok(noteService.createNote(request, id, userId));
    }

    @GetMapping("/notes/{id}")
    @Operation(summary = "Get a specific note", description = "Retrieves a specific note by its ID")
    public ResponseEntity<Note> getNote(@PathVariable String id) {
        String userId = UserContext.getCurrentUserId();
        return ResponseEntity.ok(noteService.getNoteById(id, userId));
    }

    @PatchMapping("/notes/{id}")
    @Operation(summary = "Update note", description = "Updates a note's content, completion status, due date, and/or list")
    public ResponseEntity<Note> updateNote(
            @PathVariable String id,
            @Valid @RequestBody UpdateNoteRequest request) {
        String userId = UserContext.getCurrentUserId();
        return ResponseEntity.ok(noteService.updateNote(id, request, userId));
    }

    @DeleteMapping("/notes/{id}")
    @Operation(summary = "Delete note", description = "Soft deletes a specific note")
    public ResponseEntity<Void> deleteNote(@PathVariable String id) {
        String userId = UserContext.getCurrentUserId();
        noteService.deleteNote(id, userId);
        return ResponseEntity.ok().build();
    }
} 