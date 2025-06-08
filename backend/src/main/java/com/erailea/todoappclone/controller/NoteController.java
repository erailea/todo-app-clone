package com.erailea.todoappclone.controller;

import com.erailea.todoappclone.model.Note;
import com.erailea.todoappclone.security.UserContext;
import com.erailea.todoappclone.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
            @RequestParam String content,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dueDate) {
        String userId = UserContext.getCurrentUserId();
        return ResponseEntity.ok(noteService.createNote(content, id, dueDate, userId));
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
            @RequestParam(required = false) String content,
            @RequestParam(required = false) Boolean done,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dueDate,
            @RequestParam(required = false) String targetListId) {
        String userId = UserContext.getCurrentUserId();
        return ResponseEntity.ok(noteService.updateNote(id, content, done, dueDate, targetListId, userId));
    }

    @DeleteMapping("/notes/{id}")
    @Operation(summary = "Delete note", description = "Soft deletes a specific note")
    public ResponseEntity<Void> deleteNote(@PathVariable String id) {
        String userId = UserContext.getCurrentUserId();
        noteService.deleteNote(id, userId);
        return ResponseEntity.ok().build();
    }
} 