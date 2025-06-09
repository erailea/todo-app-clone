package com.erailea.todoappclone.service;

import com.erailea.todoappclone.dto.request.CreateNoteRequest;
import com.erailea.todoappclone.dto.request.UpdateNoteRequest;
import com.erailea.todoappclone.exception.ResourceNotFoundException;
import com.erailea.todoappclone.fixture.TestFixtures;
import com.erailea.todoappclone.model.Note;
import com.erailea.todoappclone.repository.NoteRepository;
import com.erailea.todoappclone.repository.TodoListRepository;
import com.erailea.todoappclone.service.impl.NoteServiceImpl;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private TodoListRepository todoListRepository;

    @InjectMocks
    private NoteServiceImpl noteService;

    @Nested
    @DisplayName("createNote()")
    class CreateNoteTests {
        private CreateNoteRequest request;
        private Note expectedNote;

        @BeforeEach
        void setUp() {
            request = TestFixtures.createNoteRequest();
            expectedNote = TestFixtures.createTestNote();
        }

        @Test
        @DisplayName("Should create note successfully when list exists and belongs to user")
        void shouldCreateNoteSuccessfully() {
            when(todoListRepository.countByIdAndUserIdAndNotDeleted(TestFixtures.TEST_LIST_ID, TestFixtures.TEST_USER_ID))
                    .thenReturn(1L);
            when(noteRepository.save(any(Note.class))).thenReturn(expectedNote);

            Note result = noteService.createNote(request, TestFixtures.TEST_LIST_ID, TestFixtures.TEST_USER_ID);

            assertNotNull(result);
            assertEquals(expectedNote.getContent(), result.getContent());
            assertEquals(expectedNote.getListId(), result.getListId());
            verify(noteRepository).save(any(Note.class));
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when list does not exist")
        void shouldThrowExceptionWhenListDoesNotExist() {
            when(todoListRepository.countByIdAndUserIdAndNotDeleted(TestFixtures.TEST_LIST_ID, TestFixtures.TEST_USER_ID))
                    .thenReturn(0L);

            assertThrows(ResourceNotFoundException.class, () ->
                    noteService.createNote(request, TestFixtures.TEST_LIST_ID, TestFixtures.TEST_USER_ID));
        }
    }

    @Nested
    @DisplayName("getNotesByListId()")
    class GetNotesByListIdTests {
        @Test
        @DisplayName("Should return notes when list exists and belongs to user")
        void shouldReturnNotesWhenListExists() {
            List<Note> expectedNotes = List.of(TestFixtures.createTestNote());
            when(todoListRepository.countByIdAndUserIdAndNotDeleted(TestFixtures.TEST_LIST_ID, TestFixtures.TEST_USER_ID))
                    .thenReturn(1L);
            when(noteRepository.findAllByListIdAndDeletedAtIsNullOrderByDueDate(TestFixtures.TEST_LIST_ID))
                    .thenReturn(expectedNotes);

            List<Note> result = noteService.getNotesByListId(TestFixtures.TEST_LIST_ID, TestFixtures.TEST_USER_ID);

            assertNotNull(result);
            assertEquals(expectedNotes.size(), result.size());
            assertEquals(expectedNotes.get(0).getContent(), result.get(0).getContent());
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when list does not exist")
        void shouldThrowExceptionWhenListDoesNotExist() {
            when(todoListRepository.countByIdAndUserIdAndNotDeleted(TestFixtures.TEST_LIST_ID, TestFixtures.TEST_USER_ID))
                    .thenReturn(0L);

            assertThrows(ResourceNotFoundException.class, () ->
                    noteService.getNotesByListId(TestFixtures.TEST_LIST_ID, TestFixtures.TEST_USER_ID));
        }
    }

    @Nested
    @DisplayName("getNoteById()")
    class GetNoteByIdTests {
        @Test
        @DisplayName("Should return note when it exists and belongs to user's list")
        void shouldReturnNoteWhenExists() {
            Note expectedNote = TestFixtures.createTestNote();
            when(noteRepository.findByIdAndDeletedAtIsNull(TestFixtures.TEST_NOTE_ID))
                    .thenReturn(Optional.of(expectedNote));
            when(todoListRepository.countByIdAndUserIdAndNotDeleted(TestFixtures.TEST_LIST_ID, TestFixtures.TEST_USER_ID))
                    .thenReturn(1L);

            Note result = noteService.getNoteById(TestFixtures.TEST_NOTE_ID, TestFixtures.TEST_USER_ID);

            assertNotNull(result);
            assertEquals(expectedNote.getContent(), result.getContent());
            assertEquals(expectedNote.getId(), result.getId());
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when note does not exist")
        void shouldThrowExceptionWhenNoteDoesNotExist() {
            when(noteRepository.findByIdAndDeletedAtIsNull(TestFixtures.TEST_NOTE_ID))
                    .thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () ->
                    noteService.getNoteById(TestFixtures.TEST_NOTE_ID, TestFixtures.TEST_USER_ID));
        }
    }

    @Nested
    @DisplayName("updateNote()")
    class UpdateNoteTests {
        private UpdateNoteRequest request;
        private Note existingNote;

        @BeforeEach
        void setUp() {
            request = TestFixtures.createUpdateNoteRequest();
            existingNote = TestFixtures.createTestNote();
        }

        @Test
        @DisplayName("Should update note successfully when it exists and belongs to user's list")
        void shouldUpdateNoteSuccessfully() {
            when(noteRepository.findByIdAndDeletedAtIsNull(TestFixtures.TEST_NOTE_ID))
                    .thenReturn(Optional.of(existingNote));
            when(todoListRepository.countByIdAndUserIdAndNotDeleted(TestFixtures.TEST_LIST_ID, TestFixtures.TEST_USER_ID))
                    .thenReturn(1L);
            when(noteRepository.save(any(Note.class))).thenReturn(existingNote);

            Note result = noteService.updateNote(TestFixtures.TEST_NOTE_ID, request, TestFixtures.TEST_USER_ID);

            assertNotNull(result);
            verify(noteRepository).save(any(Note.class));
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when note does not exist")
        void shouldThrowExceptionWhenNoteDoesNotExist() {
            when(noteRepository.findByIdAndDeletedAtIsNull(TestFixtures.TEST_NOTE_ID))
                    .thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () ->
                    noteService.updateNote(TestFixtures.TEST_NOTE_ID, request, TestFixtures.TEST_USER_ID));
        }
    }

    @Nested
    @DisplayName("deleteNote()")
    class DeleteNoteTests {
        @Test
        @DisplayName("Should delete note successfully when it exists and belongs to user's list")
        void shouldDeleteNoteSuccessfully() {
            Note existingNote = TestFixtures.createTestNote();
            when(noteRepository.findByIdAndDeletedAtIsNull(TestFixtures.TEST_NOTE_ID))
                    .thenReturn(Optional.of(existingNote));
            when(todoListRepository.countByIdAndUserIdAndNotDeleted(TestFixtures.TEST_LIST_ID, TestFixtures.TEST_USER_ID))
                    .thenReturn(1L);
            when(noteRepository.save(any(Note.class))).thenReturn(existingNote);

            assertDoesNotThrow(() -> noteService.deleteNote(TestFixtures.TEST_NOTE_ID, TestFixtures.TEST_USER_ID));
            verify(noteRepository).save(any(Note.class));
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when note does not exist")
        void shouldThrowExceptionWhenNoteDoesNotExist() {
            when(noteRepository.findByIdAndDeletedAtIsNull(TestFixtures.TEST_NOTE_ID))
                    .thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () ->
                    noteService.deleteNote(TestFixtures.TEST_NOTE_ID, TestFixtures.TEST_USER_ID));
        }
    }
} 