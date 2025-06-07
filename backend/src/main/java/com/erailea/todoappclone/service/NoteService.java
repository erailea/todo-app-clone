package com.erailea.todoappclone.service;

import com.erailea.todoappclone.model.Note;
import java.util.List;

public interface NoteService {
    Note createNote(String content, String listId);
    List<Note> getNotesByListId(String listId);
    Note getNoteById(String noteId);
    Note updateNote(String noteId, String content, Boolean done);
    void deleteNote(String noteId);
} 