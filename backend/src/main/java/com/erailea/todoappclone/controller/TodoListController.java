package com.erailea.todoappclone.controller;

import com.erailea.todoappclone.dto.request.CreateTodoListRequest;
import com.erailea.todoappclone.dto.request.UpdateTodoListRequest;
import com.erailea.todoappclone.dto.response.TodoListResponse;
import com.erailea.todoappclone.model.TodoList;
import com.erailea.todoappclone.security.UserContext;
import com.erailea.todoappclone.service.TodoListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    @Operation(summary = "Get all todo lists with notes", description = "Retrieves all todo lists with their notes for the authenticated user")
    public ResponseEntity<List<TodoListResponse>> getLists() {
        String userId = UserContext.getCurrentUserId();
        return ResponseEntity.ok(todoListService.getLists(userId));
    }

    @PostMapping
    @Operation(summary = "Create a new todo list", description = "Creates a new todo list for the authenticated user")
    public ResponseEntity<TodoList> createList(@Valid @RequestBody CreateTodoListRequest request) {
        String userId = UserContext.getCurrentUserId();
        return ResponseEntity.ok(todoListService.createList(request.getTitle(), userId));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update todo list title", description = "Updates the title of a specific todo list")
    public ResponseEntity<TodoList> updateListTitle(
            @PathVariable String id,
            @Valid @RequestBody UpdateTodoListRequest request) {
        String userId = UserContext.getCurrentUserId();
        return ResponseEntity.ok(todoListService.updateListTitle(id, request.getTitle(), userId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete todo list", description = "Deletes a specific todo list and all its notes")
    public ResponseEntity<Void> deleteList(@PathVariable String id) {
        String userId = UserContext.getCurrentUserId();
        todoListService.deleteList(id, userId);
        return ResponseEntity.ok().build();
    }
} 