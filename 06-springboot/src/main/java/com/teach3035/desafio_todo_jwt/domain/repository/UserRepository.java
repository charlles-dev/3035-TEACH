package com.teach3035.desafio_todo_jwt.domain.repository;

import com.teach3035.desafio_todo_jwt.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    User save(User user);
    Optional<User> findById(Long id);
    boolean existsByUsername(String username);
}
