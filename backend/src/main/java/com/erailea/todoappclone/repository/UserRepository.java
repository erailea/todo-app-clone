package com.erailea.todoappclone.repository;

import com.erailea.todoappclone.model.User;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CouchbaseRepository<User, String> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}