package com.teach3035.desafio_todo_jwt.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long> {

    @Query("SELECT t FROM TaskEntity t WHERE t.deletedAt IS NULL")
    List<TaskEntity> findByDeletedAtIsNull();

    @Query("SELECT t FROM TaskEntity t WHERE t.usuario.id = :userId AND t.deletedAt IS NULL")
    List<TaskEntity> findByUsuarioId(@Param("userId") Long userId);

    @Query("SELECT t FROM TaskEntity t WHERE t.id = :id AND t.usuario.id = :userId AND t.deletedAt IS NULL")
    Optional<TaskEntity> findByIdAndUsuarioId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT t FROM TaskEntity t WHERE t.id = :id AND t.deletedAt IS NULL")
    Optional<TaskEntity> findByIdAtivo(@Param("id") Long id);
}
