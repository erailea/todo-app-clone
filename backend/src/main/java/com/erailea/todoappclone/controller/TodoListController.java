package com.erailea.todoappclone.controller;

import com.erailea.todoappclone.model.TodoList;
import com.erailea.todoappclone.security.UserContext;
import com.erailea.todoappclone.service.TodoListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lists")
@RequiredArgsConstructor
@Tag(name = "Todo Lists", description = "Todo list management APIs")
@SecurityRequirement(name = "bearerAuth")
public class TodoListController {
    private final TodoListService todoListService;

    @GetMapping
    @Operation(summary = "Get all todo lists", description = "Retrieves all todo lists for the authenticated user")
    public ResponseEntity<List<TodoList>> getLists() {
        String userId = UserContext.getCurrentUserId();
        return ResponseEntity.ok(todoListService.getLists(userId));
    }

    @PostMapping
    @Operation(summary = "Create a new todo list", description = "Creates a new todo list for the authenticated user")
    public ResponseEntity<TodoList> createList(@RequestParam String title) {
        String userId = UserContext.getCurrentUserId();
        return ResponseEntity.ok(todoListService.createList(title, userId));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update todo list title", description = "Updates the title of a specific todo list")
    public ResponseEntity<TodoList> updateListTitle(
            @PathVariable String id,
            @RequestParam String title) {
        String userId = UserContext.getCurrentUserId();
        return ResponseEntity.ok(todoListService.updateListTitle(id, title, userId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete todo list", description = "Deletes a specific todo list and all its notes")
    public ResponseEntity<Void> deleteList(@PathVariable String id) {
        String userId = UserContext.getCurrentUserId();
        todoListService.deleteList(id, userId);
        return ResponseEntity.ok().build();
    }
} 