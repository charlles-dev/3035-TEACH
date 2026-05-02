package com.teach3035.exercicio2_produtos_jpa.dto;

import java.math.BigDecimal;

public class ProdutoPatchDTO {
    private String nome;
    private BigDecimal preco;

    public ProdutoPatchDTO() {}

    public ProdutoPatchDTO(String nome, BigDecimal preco) {
        this.nome = nome;
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
}

