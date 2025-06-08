package com.erailea.todoappclone.repository;

import com.erailea.todoappclone.model.Note;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends CouchbaseRepository<Note, String> {
    @Query("#{#n1ql.selectEntity} WHERE listId = $1 AND (deletedAt IS MISSING OR deletedAt IS NULL)")
    List<Note> findAllByListIdAndDeletedAtIsNull(String listId);

    @Query("#{#n1ql.selectEntity} WHERE meta().id = $1 AND (deletedAt IS MISSING OR deletedAt IS NULL)")
    Optional<Note> findByIdAndDeletedAtIsNull(String id);
} 