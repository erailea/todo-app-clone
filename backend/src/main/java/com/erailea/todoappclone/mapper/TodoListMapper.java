package com.erailea.todoappclone.mapper;

import com.erailea.todoappclone.dto.response.NoteResponse;
import com.erailea.todoappclone.dto.response.TodoListResponse;
import com.erailea.todoappclone.model.Note;
import com.erailea.todoappclone.model.TodoList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TodoListMapper {
    
    TodoListMapper INSTANCE = Mappers.getMapper(TodoListMapper.class);
    
    @Mapping(target = "notes", source = "notes")
    TodoListResponse toResponse(TodoList todoList, List<Note> notes);
    
    NoteResponse toResponse(Note note);
    
    List<NoteResponse> toNoteResponseList(List<Note> notes);
} 