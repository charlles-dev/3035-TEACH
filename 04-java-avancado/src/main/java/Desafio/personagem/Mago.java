package Desafio.personagem;

public class Mago extends Personagem {
    public Mago(String nome) {
        // Vida baixa, Ataque alto, Defesa baixa, Mana alta
        super(nome, 80, 25, 5, 100);
    }

    @Override
    public String getClasse() {
        return "Mago";
    }

    @Override
    public void usarHabilidadeEspecial(Personagem alvo) {
        if (this.consumirMana(30)) {
            System.out.println(this.nome + " lança uma Bola de Fogo!");
            int dano = this.ataque * 2;
            alvo.tomarDano(dano);
        } else {
            System.out.println("Mana insuficiente!");
        }
    }
}
