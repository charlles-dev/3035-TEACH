package com.teach3035.exercicio1_produtos_inmemory.repository;

import com.teach3035.exercicio1_produtos_inmemory.model.Produto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryProdutoRepository implements ProdutoRepository {

    private final List<Produto> produtos = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<Produto> findAll() {
        return new ArrayList<>(produtos);
    }

    @Override
    public Optional<Produto> findById(Long id) {
        return produtos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    @Override
    public Produto save(Produto produto) {
        Long newId = idGenerator.getAndIncrement();
        produto.setId(newId);
        produtos.add(produto);
        return produto;
    }

    @Override
    public void deleteById(Long id) {
        produtos.removeIf(p -> p.getId().equals(id));
    }

    @Override
    public Produto update(Produto produto) {
        Optional<Produto> existing = findById(produto.getId());
        if (existing.isPresent()) {
            int index = produtos.indexOf(existing.get());
            produtos.set(index, produto);
        }
        return produto;
    }
}
