package com.erailea.todoappclone.fixture;

import com.erailea.todoappclone.dto.request.*;
import com.erailea.todoappclone.model.Note;
import com.erailea.todoappclone.model.TodoList;
import com.erailea.todoappclone.model.User;

import java.time.LocalDateTime;

public class TestFixtures {
    public static final String TEST_USER_ID = "test-user-id";
    public static final String TEST_USER_EMAIL = "test@example.com";
    public static final String TEST_USER_PASSWORD = "password123";
    public static final String TEST_USER_FULL_NAME = "Test User";

    public static final String TEST_LIST_ID = "test-list-id";
    public static final String TEST_LIST_TITLE = "Test List";

    public static final String TEST_NOTE_ID = "test-note-id";
    public static final String TEST_NOTE_CONTENT = "Test Note Content";

    public static final LocalDateTime TEST_CREATED_AT = LocalDateTime.of(2024, 1, 1, 12, 0);
    public static final LocalDateTime TEST_DUE_DATE = LocalDateTime.of(2024, 1, 2, 12, 0);

    public static User createTestUser() {
        User user = new User();
        user.setId(TEST_USER_ID);
        user.setEmail(TEST_USER_EMAIL);
        user.setPassword(TEST_USER_PASSWORD);
        user.setFullName(TEST_USER_FULL_NAME);
        return user;
    }

    public static TodoList createTestTodoList() {
        TodoList list = new TodoList();
        list.setId(TEST_LIST_ID);
        list.setTitle(TEST_LIST_TITLE);
        list.setCreatedAt(TEST_CREATED_AT);
        list.setUserId(TEST_USER_ID);
        list.setDeletedAt(null);
        return list;
    }

    public static Note createTestNote() {
        Note note = new Note();
        note.setId(TEST_NOTE_ID);
        note.setContent(TEST_NOTE_CONTENT);
        note.setDone(false);
        note.setCreatedAt(TEST_CREATED_AT);
        note.setDueDate(TEST_DUE_DATE);
        note.setListId(TEST_LIST_ID);
        note.setDeletedAt(null);
        return note;
    }

    public static RegisterRequest createRegisterRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail(TEST_USER_EMAIL);
        request.setPassword(TEST_USER_PASSWORD);
        request.setFullName(TEST_USER_FULL_NAME);
        return request;
    }

    public static AuthenticateRequest createAuthenticateRequest() {
        AuthenticateRequest request = new AuthenticateRequest();
        request.setEmail(TEST_USER_EMAIL);
        request.setPassword(TEST_USER_PASSWORD);
        return request;
    }

    public static CreateTodoListRequest createTodoListRequest() {
        CreateTodoListRequest request = new CreateTodoListRequest();
        request.setTitle(TEST_LIST_TITLE);
        return request;
    }

    public static CreateNoteRequest createNoteRequest() {
        CreateNoteRequest request = new CreateNoteRequest();
        request.setContent(TEST_NOTE_CONTENT);
        request.setDueDate(TEST_DUE_DATE);
        return request;
    }

    public static UpdateNoteRequest createUpdateNoteRequest() {
        UpdateNoteRequest request = new UpdateNoteRequest();
        request.setContent("Updated Content");
        request.setDone(true);
        request.setDueDate(TEST_DUE_DATE.plusDays(1));
        request.setTargetListId(TEST_LIST_ID);
        return request;
    }
} 