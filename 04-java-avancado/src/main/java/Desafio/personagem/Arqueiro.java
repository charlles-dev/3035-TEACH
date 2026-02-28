package Desafio.personagem;

public class Arqueiro extends Personagem {
    public Arqueiro(String nome) {
        // Vida média, Ataque alto, Defesa média, Mana média
        super(nome, 100, 20, 8, 50);
    }

    @Override
    public String getClasse() {
        return "Arqueiro";
    }

    @Override
    public void usarHabilidadeEspecial(Personagem alvo) {
        if (this.consumirMana(20)) {
            System.out.println(this.nome + " dispara um Tiro Preciso!");
            // Dano aumentado que ignora parte da defesa
            alvo.tomarDano(this.ataque + 10);
        } else {
            System.out.println("Mana insuficiente!");
        }
    }
}
