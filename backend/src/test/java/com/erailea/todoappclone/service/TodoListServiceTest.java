package com.erailea.todoappclone.service;

import com.erailea.todoappclone.exception.ResourceNotFoundException;
import com.erailea.todoappclone.fixture.TestFixtures;
import com.erailea.todoappclone.model.Note;
import com.erailea.todoappclone.model.TodoList;
import com.erailea.todoappclone.repository.NoteRepository;
import com.erailea.todoappclone.repository.TodoListRepository;
import com.erailea.todoappclone.service.impl.TodoListServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoListServiceTest {

    @Mock
    private TodoListRepository todoListRepository;

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private TodoListServiceImpl todoListService;

    @Nested
    @DisplayName("createList()")
    class CreateListTests {
        @Test
        @DisplayName("Should create list successfully")
        void shouldCreateListSuccessfully() {
            TodoList expectedList = TestFixtures.createTestTodoList();
            when(todoListRepository.save(any(TodoList.class))).thenReturn(expectedList);

            TodoList result = todoListService.createList(TestFixtures.TEST_LIST_TITLE, TestFixtures.TEST_USER_ID);

            assertNotNull(result);
            assertEquals(expectedList.getTitle(), result.getTitle());
            assertEquals(expectedList.getUserId(), result.getUserId());
            verify(todoListRepository).save(any(TodoList.class));
        }
    }

    @Nested
    @DisplayName("getLists()")
    class GetListsTests {
        @Test
        @DisplayName("Should return all lists for user")
        void shouldReturnAllListsForUser() {
            List<TodoList> expectedLists = Arrays.asList(TestFixtures.createTestTodoList());
            when(todoListRepository.findAllByUserIdAndDeletedAtIsNull(TestFixtures.TEST_USER_ID))
                    .thenReturn(expectedLists);

            List<TodoList> result = todoListService.getLists(TestFixtures.TEST_USER_ID);

            assertNotNull(result);
            assertEquals(expectedLists.size(), result.size());
            assertEquals(expectedLists.get(0).getTitle(), result.get(0).getTitle());
        }
    }

    @Nested
    @DisplayName("updateListTitle()")
    class UpdateListTitleTests {
        @Test
        @DisplayName("Should update list title successfully when list exists and belongs to user")
        void shouldUpdateListTitleSuccessfully() {
            TodoList existingList = TestFixtures.createTestTodoList();
            String newTitle = "Updated Title";
            when(todoListRepository.countByIdAndUserIdAndNotDeleted(TestFixtures.TEST_LIST_ID, TestFixtures.TEST_USER_ID))
                    .thenReturn(1L);
            when(todoListRepository.findById(TestFixtures.TEST_LIST_ID))
                    .thenReturn(Optional.of(existingList));
            when(todoListRepository.save(any(TodoList.class))).thenReturn(existingList);

            TodoList result = todoListService.updateListTitle(TestFixtures.TEST_LIST_ID, newTitle, TestFixtures.TEST_USER_ID);

            assertNotNull(result);
            verify(todoListRepository).save(any(TodoList.class));
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when list does not exist")
        void shouldThrowExceptionWhenListDoesNotExist() {
            when(todoListRepository.countByIdAndUserIdAndNotDeleted(TestFixtures.TEST_LIST_ID, TestFixtures.TEST_USER_ID))
                    .thenReturn(0L);

            assertThrows(ResourceNotFoundException.class, () ->
                    todoListService.updateListTitle(TestFixtures.TEST_LIST_ID, "New Title", TestFixtures.TEST_USER_ID));
        }
    }

    @Nested
    @DisplayName("deleteList()")
    class DeleteListTests {
        @Test
        @DisplayName("Should delete list and its notes successfully when list exists and belongs to user")
        void shouldDeleteListAndNotesSuccessfully() {
            TodoList existingList = TestFixtures.createTestTodoList();
            List<Note> existingNotes = Arrays.asList(TestFixtures.createTestNote());
            when(todoListRepository.countByIdAndUserIdAndNotDeleted(TestFixtures.TEST_LIST_ID, TestFixtures.TEST_USER_ID))
                    .thenReturn(1L);
            when(todoListRepository.findById(TestFixtures.TEST_LIST_ID))
                    .thenReturn(Optional.of(existingList));
            when(noteRepository.findAllByListIdAndDeletedAtIsNull(TestFixtures.TEST_LIST_ID))
                    .thenReturn(existingNotes);
            when(todoListRepository.save(any(TodoList.class))).thenReturn(existingList);
            when(noteRepository.saveAll(any())).thenReturn(existingNotes);

            assertDoesNotThrow(() -> todoListService.deleteList(TestFixtures.TEST_LIST_ID, TestFixtures.TEST_USER_ID));
            verify(todoListRepository).save(any(TodoList.class));
            verify(noteRepository).saveAll(any());
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when list does not exist")
        void shouldThrowExceptionWhenListDoesNotExist() {
            when(todoListRepository.countByIdAndUserIdAndNotDeleted(TestFixtures.TEST_LIST_ID, TestFixtures.TEST_USER_ID))
                    .thenReturn(0L);

            assertThrows(ResourceNotFoundException.class, () ->
                    todoListService.deleteList(TestFixtures.TEST_LIST_ID, TestFixtures.TEST_USER_ID));
        }
    }
} 