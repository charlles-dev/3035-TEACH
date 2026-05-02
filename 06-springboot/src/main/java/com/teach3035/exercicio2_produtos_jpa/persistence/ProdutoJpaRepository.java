package com.teach3035.exercicio2_produtos_jpa.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoJpaRepository extends JpaRepository<ProdutoEntity, Long>, JpaSpecificationExecutor<ProdutoEntity> {

    @Query("SELECT p FROM ProdutoEntity p WHERE p.id = :id AND p.deletedAt IS NULL")
    Optional<ProdutoEntity> findByIdAtivo(@Param("id") Long id);

    @Query("SELECT p FROM ProdutoEntity p WHERE p.deletedAt IS NULL")
    List<ProdutoEntity> findAllAtivo();

    @Query("SELECT p FROM ProdutoEntity p WHERE p.nome = :nome AND p.deletedAt IS NULL")
    List<ProdutoEntity> findByNomeAtivo(@Param("nome") String nome);
}

