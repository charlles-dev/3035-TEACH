package com.teach3035.exercicio1_produtos_inmemory.repository;

import com.teach3035.exercicio1_produtos_inmemory.model.Produto;
import java.util.List;
import java.util.Optional;

public interface ProdutoRepository {
    List<Produto> findAll();
    Optional<Produto> findById(Long id);
    Produto save(Produto produto);
    void deleteById(Long id);
    Produto update(Produto produto);
}
