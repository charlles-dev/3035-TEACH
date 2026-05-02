package com.teach3035.desafio_todo_jwt.domain.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long id) {
        super("Task não encontrada com id: " + id);
    }
}
