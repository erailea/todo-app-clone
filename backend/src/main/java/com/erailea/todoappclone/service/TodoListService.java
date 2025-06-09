package com.erailea.todoappclone.service;

import com.erailea.todoappclone.dto.response.TodoListResponse;
import com.erailea.todoappclone.model.TodoList;

import java.util.List;

public interface TodoListService {
    TodoList createList(String title, String userId);

    List<TodoListResponse> getLists(String userId);

    TodoList updateListTitle(String id, String title, String userId);

    void deleteList(String id, String userId);
} 