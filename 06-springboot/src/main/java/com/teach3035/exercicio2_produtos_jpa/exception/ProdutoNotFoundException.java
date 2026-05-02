package com.teach3035.exercicio2_produtos_jpa.exception;

public class ProdutoNotFoundException extends RuntimeException {
    public ProdutoNotFoundException(Long id) {
        super("Produto não encontrado com id: " + id);
    }

    public ProdutoNotFoundException(String message) {
        super(message);
    }
}

