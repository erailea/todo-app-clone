package com.erailea.todoappclone.service;

import com.erailea.todoappclone.dto.response.TodoListResponse;
import com.erailea.todoappclone.exception.ResourceNotFoundException;
import com.erailea.todoappclone.fixture.TestFixtures;
import com.erailea.todoappclone.mapper.TodoListMapper;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoListServiceTest {

    @Mock
    private TodoListRepository todoListRepository;

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private TodoListMapper todoListMapper;

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
            TodoList testList = TestFixtures.createTestTodoList();
            List<TodoList> todoLists = Arrays.asList(testList);
            List<Note> notes = Collections.emptyList();
            TodoListResponse expectedResponse = new TodoListResponse();
            expectedResponse.setId(testList.getId());
            expectedResponse.setTitle(testList.getTitle());
            
            when(todoListRepository.findAllByUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(TestFixtures.TEST_USER_ID))
                    .thenReturn(todoLists);
            when(noteRepository.findAllByListIdInAndDeletedAtIsNull(Arrays.asList(testList.getId())))
                    .thenReturn(notes);
            when(todoListMapper.toResponse(testList, notes))
                    .thenReturn(expectedResponse);

            List<TodoListResponse> result = todoListService.getLists(TestFixtures.TEST_USER_ID);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(expectedResponse.getTitle(), result.get(0).getTitle());
            verify(todoListRepository).findAllByUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(TestFixtures.TEST_USER_ID);
            verify(noteRepository).findAllByListIdInAndDeletedAtIsNull(Arrays.asList(testList.getId()));
            verify(todoListMapper).toResponse(testList, notes);
        }

        @Test
        @DisplayName("Should return empty list when no lists found")
        void shouldReturnEmptyListWhenNoListsFound() {
            when(todoListRepository.findAllByUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(TestFixtures.TEST_USER_ID))
                    .thenReturn(Collections.emptyList());

            List<TodoListResponse> result = todoListService.getLists(TestFixtures.TEST_USER_ID);

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(todoListRepository).findAllByUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(TestFixtures.TEST_USER_ID);
            // Verify that note repository is not called when no lists exist
            verify(noteRepository, never()).findAllByListIdInAndDeletedAtIsNull(any());
            verify(todoListMapper, never()).toResponse(any(TodoList.class), any());
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
            when(noteRepository.saveAll(anyList())).thenReturn(existingNotes);

            assertDoesNotThrow(() -> todoListService.deleteList(TestFixtures.TEST_LIST_ID, TestFixtures.TEST_USER_ID));
            verify(todoListRepository).save(any(TodoList.class));
            verify(noteRepository).saveAll(anyList());
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