package com.erailea.todoappclone.service;

import com.erailea.todoappclone.model.TodoList;
import java.util.List;

public interface TodoListService {
    TodoList createList(String title, String userId);
    List<TodoList> getLists(String userId);
    TodoList updateListTitle(String id, String title);
    void deleteList(String id);
} 