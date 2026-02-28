package Desafio.itens;

import Desafio.personagem.Personagem;

public abstract class Item {
    protected String nome;
    protected int preco;

    public Item(String nome, int preco) {
        this.nome = nome;
        this.preco = preco;
    }

    public abstract void usar(Personagem usuario);

    public String getNome() {
        return nome;
    }

    public int getPreco() {
        return preco;
    }
}
