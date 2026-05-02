package com.teach3035.desafio_todo_jwt.dto;

import com.teach3035.desafio_todo_jwt.domain.model.TaskStatus;

public record TaskUpdateDTO(String titulo, TaskStatus status) {}