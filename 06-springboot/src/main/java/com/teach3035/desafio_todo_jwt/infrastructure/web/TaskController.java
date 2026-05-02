package com.teach3035.desafio_todo_jwt.infrastructure.web;

import com.teach3035.desafio_todo_jwt.application.usecase.TaskUseCase;
import com.teach3035.desafio_todo_jwt.domain.model.Task;
import com.teach3035.desafio_todo_jwt.domain.model.TaskStatus;
import com.teach3035.desafio_todo_jwt.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskUseCase taskUseCase;

    public TaskController(TaskUseCase taskUseCase) {
        this.taskUseCase = taskUseCase;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Task>>> getAll() {
        List<Task> tasks = taskUseCase.findAll();
        return ResponseEntity.ok(ApiResponse.success(tasks));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Task>> create(
            @RequestParam String titulo,
            @RequestParam(required = false) String descricao) {
        
        if (titulo == null || titulo.isBlank()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Titulo e obrigatorio"));
        }
        
        Task task = taskUseCase.create(titulo, descricao);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(task));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Task>> update(
            @PathVariable Long id,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String status) {
        
        TaskStatus taskStatus = status != null ? TaskStatus.valueOf(status.toUpperCase()) : null;
        Task task = taskUseCase.update(id, titulo, taskStatus);
        return ResponseEntity.ok(ApiResponse.success(task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        taskUseCase.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}