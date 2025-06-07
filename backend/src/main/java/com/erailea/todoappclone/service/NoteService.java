package com.erailea.todoappclone.service;

import com.erailea.todoappclone.model.Note;
import java.util.List;

public interface NoteService {
    Note createNote(String content, String listId, String userId);
    List<Note> getNotesByListId(String listId, String userId);
    Note getNoteById(String noteId, String userId);
    Note updateNote(String noteId, String content, Boolean done, String userId);
    void deleteNote(String noteId, String userId);
} 