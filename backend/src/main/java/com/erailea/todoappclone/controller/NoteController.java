package com.erailea.todoappclone.controller;

import com.erailea.todoappclone.model.Note;
import com.erailea.todoappclone.security.UserContext;
import com.erailea.todoappclone.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lists/{listId}/notes")
@RequiredArgsConstructor
@Tag(name = "Notes", description = "Note management APIs")
@SecurityRequirement(name = "bearerAuth")
public class NoteController {
    private final NoteService noteService;

    @GetMapping
    @Operation(summary = "Get all notes", description = "Retrieves all notes in a specific todo list")
    public ResponseEntity<List<Note>> getNotes(@PathVariable String listId) {
        String userId = UserContext.getCurrentUserId();
        return ResponseEntity.ok(noteService.getNotesByListId(listId, userId));
    }

    @PostMapping
    @Operation(summary = "Create a new note", description = "Creates a new note in a specific todo list")
    public ResponseEntity<Note> createNote(
            @PathVariable String listId,
            @RequestParam String content) {
        String userId = UserContext.getCurrentUserId();
        return ResponseEntity.ok(noteService.createNote(content, listId, userId));
    }

    @GetMapping("/{noteId}")
    @Operation(summary = "Get a specific note", description = "Retrieves a specific note by its ID")
    public ResponseEntity<Note> getNote(
            @PathVariable String listId,
            @PathVariable String noteId) {
        String userId = UserContext.getCurrentUserId();
        return ResponseEntity.ok(noteService.getNoteById(noteId, userId));
    }

    @PatchMapping("/{noteId}")
    @Operation(summary = "Update note", description = "Updates a note's content and/or completion status")
    public ResponseEntity<Note> updateNote(
            @PathVariable String listId,
            @PathVariable String noteId,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) Boolean done) {
        String userId = UserContext.getCurrentUserId();
        return ResponseEntity.ok(noteService.updateNote(noteId, content, done, userId));
    }

    @DeleteMapping("/{noteId}")
    @Operation(summary = "Delete note", description = "Deletes a specific note from a todo list")
    public ResponseEntity<Void> deleteNote(
            @PathVariable String listId,
            @PathVariable String noteId) {
        String userId = UserContext.getCurrentUserId();
        noteService.deleteNote(noteId, userId);
        return ResponseEntity.ok().build();
    }
} 