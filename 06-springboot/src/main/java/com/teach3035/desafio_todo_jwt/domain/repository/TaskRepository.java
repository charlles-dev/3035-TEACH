package com.teach3035.desafio_todo_jwt.domain.repository;

import com.teach3035.desafio_todo_jwt.domain.model.Task;
import com.teach3035.desafio_todo_jwt.domain.model.TaskStatus;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();
    List<Task> findByUsuarioId(Long usuarioId);
    Optional<Task> findByIdAndUsuarioId(Long id, Long usuarioId);
    Task save(Task task);
    void delete(Task task);
    Optional<Task> findById(Long id);
}
