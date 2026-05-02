package com.teach3035.exercicio2_produtos_jpa.repository;

import com.teach3035.exercicio2_produtos_jpa.model.Produto;
import com.teach3035.exercicio2_produtos_jpa.persistence.ProdutoEntity;
import com.teach3035.exercicio2_produtos_jpa.persistence.ProdutoJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JpaProdutoRepository implements ProdutoRepository {

    private final ProdutoJpaRepository jpaRepository;

    public JpaProdutoRepository(ProdutoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Produto> findAll() {
        return jpaRepository.findAllAtivo().stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Produto> findById(Long id) {
        return jpaRepository.findByIdAtivo(id).map(this::toModel);
    }

    @Override
    public Produto save(Produto produto) {
        ProdutoEntity entity = toEntity(produto);
        entity = jpaRepository.save(entity);
        return toModel(entity);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.findByIdAtivo(id).ifPresent(entity -> {
            entity.setDeletedAt(LocalDateTime.now());
            jpaRepository.save(entity);
        });
    }

    @Override
    public Produto update(Produto produto) {
        ProdutoEntity entity = toEntity(produto);
        entity = jpaRepository.save(entity);
        return toModel(entity);
    }

    private Produto toModel(ProdutoEntity entity) {
        return new Produto(entity.getId(), entity.getNome(), entity.getPreco());
    }

    private ProdutoEntity toEntity(Produto produto) {
        ProdutoEntity entity = new ProdutoEntity();
        entity.setId(produto.getId());
        entity.setNome(produto.getNome());
        entity.setPreco(produto.getPreco());
        return entity;
    }
}

