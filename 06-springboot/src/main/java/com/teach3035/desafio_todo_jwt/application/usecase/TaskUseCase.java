package com.teach3035.desafio_todo_jwt.application.usecase;

import com.teach3035.desafio_todo_jwt.domain.exception.InvalidStatusTransitionException;
import com.teach3035.desafio_todo_jwt.domain.exception.TaskNotFoundException;
import com.teach3035.desafio_todo_jwt.domain.exception.UnauthorizedException;
import com.teach3035.desafio_todo_jwt.domain.model.Task;
import com.teach3035.desafio_todo_jwt.domain.model.TaskStatus;
import com.teach3035.desafio_todo_jwt.domain.model.User;
import com.teach3035.desafio_todo_jwt.domain.repository.TaskRepository;
import com.teach3035.desafio_todo_jwt.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskUseCase {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskUseCase(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public record CreateTaskRequest(String titulo, String descricao) {}
    public record UpdateTaskRequest(TaskStatus status, String descricao) {}

public Task create(String titulo, String descricao) {
        Task task = new Task(titulo, descricao, null);
        return taskRepository.save(task);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(Long id, Long userId) {
        return taskRepository.findByIdAndUsuarioId(id, userId)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

public Task update(Long id, String titulo, TaskStatus status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        if (status != null) {
            if (!task.podeTransicionarPara(status)) {
                throw new InvalidStatusTransitionException(
                        "Nao e possivel alterar status de CONCLUIDA para outro estado");
            }
            task.atualizarStatus(status);
        }

        if (titulo != null && !titulo.isBlank()) {
            task.setTitulo(titulo);
        }

        return taskRepository.save(task);
    }

    public void delete(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.delete(task);
    }
}
