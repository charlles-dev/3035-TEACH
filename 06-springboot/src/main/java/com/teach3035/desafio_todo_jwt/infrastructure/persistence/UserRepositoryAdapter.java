package com.teach3035.desafio_todo_jwt.infrastructure.persistence;

import com.teach3035.desafio_todo_jwt.domain.model.User;
import com.teach3035.desafio_todo_jwt.domain.model.UserRole;
import com.teach3035.desafio_todo_jwt.domain.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository jpaRepository;

    public UserRepositoryAdapter(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaRepository.findByUsername(username).map(this::toModel);
    }

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        entity = jpaRepository.save(entity);
        return toModel(entity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id).map(this::toModel);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }

    private User toModel(UserEntity entity) {
        User user = new User(entity.getUsername(), entity.getPassword(), toModelRole(entity.getRole()));
        user.setId(entity.getId());
        user.setEnabled(entity.isEnabled());
        user.setFailedLoginAttempts(entity.getFailedLoginAttempts());
        user.setLockedUntil(entity.getLockedUntil());
        user.setDataCriacao(entity.getDataCriacao());
        return user;
    }

    private UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setRole(toEntityRole(user.getRole()));
        entity.setEnabled(user.isEnabled());
        entity.setFailedLoginAttempts(user.getFailedLoginAttempts());
        entity.setLockedUntil(user.getLockedUntil());
        return entity;
    }

    private UserRole toModelRole(UserEntity.UserRole role) {
        return switch (role) {
            case ADMIN -> UserRole.ADMIN;
            case USER -> UserRole.USER;
        };
    }

    private UserEntity.UserRole toEntityRole(UserRole role) {
        return switch (role) {
            case ADMIN -> UserEntity.UserRole.ADMIN;
            case USER -> UserEntity.UserRole.USER;
        };
    }
}
