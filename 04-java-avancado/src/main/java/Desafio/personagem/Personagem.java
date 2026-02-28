package Desafio.personagem;

import Desafio.itens.Item;
import Desafio.itens.PocaoCura;
import java.util.ArrayList;
import java.util.List;

public abstract class Personagem {
    protected String nome;
    protected int vidaMaxima;
    protected int vida;
    protected int manaMaxima;
    protected int mana;
    protected int ataque;
    protected int defesa;
    protected int ouro;
    protected List<Item> inventario;
    protected boolean defendendo;

    public Personagem(String nome, int vidaMaxima, int ataque, int defesa, int manaMaxima) {
        this.nome = nome;
        this.vidaMaxima = vidaMaxima;
        this.vida = vidaMaxima;
        this.manaMaxima = manaMaxima;
        this.mana = manaMaxima;
        this.ataque = ataque;
        this.defesa = defesa;
        this.ouro = 0;
        this.inventario = new ArrayList<>();
        this.defendendo = false;

        // Inicializa com 3 poções de cura padrão, conforme requisitado
        this.inventario.add(new PocaoCura());
        this.inventario.add(new PocaoCura());
        this.inventario.add(new PocaoCura());
    }

    public void atacar(Personagem alvo) {
        System.out.println(this.nome + " ataca " + alvo.getNome() + "!");
        int danoBase = this.ataque;
        // Lógica simples de dano, pode ser sobrescrita nas subclasses para adicionar
        // rolagem de dados ou crítico
        alvo.tomarDano(danoBase);
    }

    public void defender() {
        System.out.println(this.nome + " assume uma postura defensiva!");
        this.defendendo = true;
    }

    public void tomarDano(int dano) {
        int danoEfetivo = dano - (this.defendendo ? this.defesa * 2 : this.defesa);
        if (danoEfetivo < 0) {
            danoEfetivo = 0; // O dano não pode ser negativo (curar o personagem)
        }

        this.vida -= danoEfetivo;
        System.out.println(this.nome + " recebe " + danoEfetivo + " de dano!");

        if (this.vida <= 0) {
            this.vida = 0;
            System.out.println(this.nome + " foi derrotado!");
        }
    }

    public void usarItem() {
        if (!inventario.isEmpty()) {
            Item item = inventario.remove(0);
            System.out.println(this.nome + " usa " + item.getNome() + "!");
            item.usar(this);
        } else {
            System.out.println(this.nome + " não tem itens no inventário!");
        }
    }

    public void curar(int quantidade) {
        this.vida += quantidade;
        if (this.vida > this.vidaMaxima) {
            this.vida = this.vidaMaxima;
        }
    }

    public void recuperarMana(int quantidade) {
        this.mana += quantidade;
        if (this.mana > this.manaMaxima) {
            this.mana = this.manaMaxima;
        }
    }

    public boolean consumirMana(int quantidade) {
        if (this.mana >= quantidade) {
            this.mana -= quantidade;
            return true;
        }
        return false;
    }

    public boolean estaVivo() {
        return this.vida > 0;
    }

    public void resetarDefesa() {
        this.defendendo = false;
    }

    // Getters e Setters básicos
    public String getNome() {
        return nome;
    }

    public int getVida() {
        return vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getMana() {
        return mana;
    }

    public int getManaMaxima() {
        return manaMaxima;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefesa() {
        return defesa;
    }

    public int getOuro() {
        return ouro;
    }

    public void setOuro(int ouro) {
        this.ouro = ouro;
    }

    public void adicionarOuro(int quantidade) {
        this.ouro += quantidade;
    }

    public boolean removerOuro(int quantidade) {
        if (this.ouro >= quantidade) {
            this.ouro -= quantidade;
            return true;
        }
        return false;
    }

    public boolean isDefendendo() {
        return defendendo;
    }

    public List<Item> getInventario() {
        return inventario;
    }

    public abstract String getClasse();

    public abstract void usarHabilidadeEspecial(Personagem alvo);
}
