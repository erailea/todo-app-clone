package com.erailea.todoappclone.repository;

import com.erailea.todoappclone.model.TodoList;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TodoListRepository extends CouchbaseRepository<TodoList, String> {
    @Query("#{#n1ql.selectEntity} WHERE userId = $1 AND (deletedAt IS MISSING OR deletedAt IS NULL)")
    List<TodoList> findAllByUserIdAndDeletedAtIsNull(String userId);

    @Query("SELECT COUNT(1) FROM #{#n1ql.bucket} " +
            "WHERE meta().id = $1 AND userId = $2 AND (deletedAt IS NULL OR deletedAt IS MISSING)")
    Long countByIdAndUserIdAndNotDeleted(String id, String userId);
}