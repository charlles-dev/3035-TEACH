package com.teach3035.desafio_todo_jwt.infrastructure.persistence;

import com.teach3035.desafio_todo_jwt.domain.model.Task;
import com.teach3035.desafio_todo_jwt.domain.model.TaskStatus;
import com.teach3035.desafio_todo_jwt.domain.model.User;
import com.teach3035.desafio_todo_jwt.domain.model.UserRole;
import com.teach3035.desafio_todo_jwt.domain.repository.TaskRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TaskRepositoryAdapter implements TaskRepository {

    private final TaskJpaRepository jpaRepository;
    private final UserJpaRepository userJpaRepository;

    public TaskRepositoryAdapter(TaskJpaRepository jpaRepository, UserJpaRepository userJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public List<Task> findAll() {
        return jpaRepository.findByDeletedAtIsNull().stream()
                .map(e -> toModel(e, e.getUsuario()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findByUsuarioId(Long usuarioId) {
        return jpaRepository.findByUsuarioId(usuarioId).stream()
                .map(e -> toModel(e, e.getUsuario()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Task> findByIdAndUsuarioId(Long id, Long usuarioId) {
        return jpaRepository.findByIdAndUsuarioId(id, usuarioId)
                .map(e -> toModel(e, e.getUsuario()));
    }

    @Override
    public Task save(Task task) {
        TaskEntity entity = toEntity(task);
        entity = jpaRepository.save(entity);
        return toModel(entity, entity.getUsuario());
    }

    @Override
    public void delete(Task task) {
        TaskEntity entity = jpaRepository.findByIdAtivo(task.getId()).orElse(null);
        if (entity != null) {
            entity.setDeletedAt(LocalDateTime.now());
            jpaRepository.save(entity);
        }
    }

    @Override
    public Optional<Task> findById(Long id) {
        return jpaRepository.findByIdAtivo(id).map(e -> toModel(e, e.getUsuario()));
    }

    private Task toModel(TaskEntity entity, UserEntity userEntity) {
        User user = userEntity != null ? toUserModel(userEntity) : null;
        Task task = new Task(entity.getTitulo(), entity.getDescricao(), user);
        task.setId(entity.getId());
        task.setStatus(toModelStatus(entity.getStatus()));
        task.setVersion(entity.getVersion());
        task.setDataCriacao(entity.getDataCriacao());
        task.setDataAtualizacao(entity.getDataAtualizacao());
        return task;
    }

    private User toUserModel(UserEntity entity) {
        User user = new User(entity.getUsername(), entity.getPassword(),
            UserRole.valueOf(entity.getRole().name()));
        user.setId(entity.getId());
        return user;
    }

    private TaskEntity toEntity(Task task) {
        TaskEntity entity = new TaskEntity();
        entity.setId(task.getId());
        entity.setTitulo(task.getTitulo());
        entity.setDescricao(task.getDescricao());
        entity.setStatus(toEntityStatus(task.getStatus()));

        if (task.getUsuario() != null && task.getUsuario().getId() != null) {
            UserEntity userEntity = userJpaRepository.findById(task.getUsuario().getId()).orElse(null);
            entity.setUsuario(userEntity);
        }
        return entity;
    }

    private TaskStatus toModelStatus(TaskEntity.TaskStatus status) {
        if (status == null) return TaskStatus.PENDENTE;
        return switch (status) {
            case PENDENTE -> TaskStatus.PENDENTE;
            case EM_ANDAMENTO -> TaskStatus.EM_ANDAMENTO;
            case CONCLUIDA -> TaskStatus.CONCLUIDA;
        };
    }

    private TaskEntity.TaskStatus toEntityStatus(TaskStatus status) {
        if (status == null) return TaskEntity.TaskStatus.PENDENTE;
        return switch (status) {
            case PENDENTE -> TaskEntity.TaskStatus.PENDENTE;
            case EM_ANDAMENTO -> TaskEntity.TaskStatus.EM_ANDAMENTO;
            case CONCLUIDA -> TaskEntity.TaskStatus.CONCLUIDA;
        };
    }
}