package com.teach3035.desafio_todo_jwt.domain.model;

import java.time.LocalDateTime;

public class Task {
    private Long id;
    private String titulo;
    private String descricao;
    private TaskStatus status;
    private Long version;
    private User usuario;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private LocalDateTime deletedAt;

    public Task() {}

    public Task(String titulo, String descricao, User usuario) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = TaskStatus.PENDENTE;
        this.usuario = usuario;
    }

    public boolean podeTransicionarPara(TaskStatus novoStatus) {
        if (this.status == TaskStatus.CONCLUIDA) {
            return false;
        }
        return true;
    }

    public void atualizarStatus(TaskStatus novoStatus) {
        if (!podeTransicionarPara(novoStatus)) {
            throw new IllegalStateException("Não é possível alterar status de CONCLUIDA para outro estado");
        }
        this.status = novoStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
