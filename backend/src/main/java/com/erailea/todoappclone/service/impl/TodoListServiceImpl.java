package com.erailea.todoappclone.service.impl;

import com.erailea.todoappclone.model.TodoList;
import com.erailea.todoappclone.service.TodoListService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TodoListServiceImpl implements TodoListService {
    @Override
    public TodoList createList(String title, String userId) {
        // Dummy implementation
        return new TodoList(
            "list-" + System.currentTimeMillis(),
            title,
            LocalDateTime.now(),
            userId
        );
    }

    @Override
    public List<TodoList> getLists(String userId) {
        // Dummy implementation
        List<TodoList> lists = new ArrayList<>();
        lists.add(new TodoList("list-1", "Shopping List", LocalDateTime.now(), userId));
        lists.add(new TodoList("list-2", "Work Tasks", LocalDateTime.now(), userId));
        return lists;
    }

    @Override
    public TodoList updateListTitle(String id, String title) {
        // Dummy implementation
        return new TodoList(
            id,
            title,
            LocalDateTime.now(),
            "user-123"
        );
    }

    @Override
    public void deleteList(String id) {
        // Dummy implementation - in real implementation, delete the list and its notes
    }
} 