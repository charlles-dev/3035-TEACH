package Desafio.personagem;

public class Guerreiro extends Personagem {
    public Guerreiro(String nome) {
        super(nome, 120, 15, 10, 30);
    }

    @Override
    public String getClasse() {
        return "Guerreiro";
    }

    @Override
    public void usarHabilidadeEspecial(Personagem alvo) {
        if (this.consumirMana(15)) {
            System.out.println(this.nome + " usa Grito de Guerra!");
            System.out.println("A defesa de " + this.nome + " aumentou!");
            this.defesa += 5;
        } else {
            System.out.println("Mana insuficiente!");
        }
    }
}
