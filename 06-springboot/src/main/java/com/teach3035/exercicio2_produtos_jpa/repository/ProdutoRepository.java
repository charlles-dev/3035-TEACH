package com.teach3035.exercicio2_produtos_jpa.repository;

import com.teach3035.exercicio2_produtos_jpa.model.Produto;
import java.util.List;
import java.util.Optional;

public interface ProdutoRepository {
    List<Produto> findAll();
    Optional<Produto> findById(Long id);
    Produto save(Produto produto);
    void deleteById(Long id);
    Produto update(Produto produto);
}

