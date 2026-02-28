package Desafio.personagem;

import Desafio.itens.PocaoCura;

public class Inimigo extends Personagem {

    public Inimigo(String nome, int vidaMaxima, int ataque, int defesa) {
        super(nome, vidaMaxima, ataque, defesa, 20);
        // Opcional: Inimigos têm menos poções
        this.inventario.clear();
        if (Math.random() > 0.5) {
            this.inventario.add(new PocaoCura());
        }
    }

    @Override
    public String getClasse() {
        return "Inimigo";
    }

    @Override
    public void usarHabilidadeEspecial(Personagem alvo) {
        System.out.println(this.nome + " usa um Golpe Brutal!");
        alvo.tomarDano(this.ataque + 5);
    }

    // IA simples para o inimigo decidir a ação
    public void acaoIA(Personagem alvo) {
        // Random 0 a 9
        int chance = (int) (Math.random() * 10);

        // 60% chance atacar, 20% especial, 10% defender, 10% usar item
        if (chance < 6) {
            this.atacar(alvo);
        } else if (chance < 8) {
            this.usarHabilidadeEspecial(alvo);
        } else if (chance < 9) {
            this.defender();
        } else {
            if (this.vida < (this.vidaMaxima / 2) && !this.inventario.isEmpty()) {
                this.usarItem();
            } else {
                this.atacar(alvo);
            }
        }
    }
}
