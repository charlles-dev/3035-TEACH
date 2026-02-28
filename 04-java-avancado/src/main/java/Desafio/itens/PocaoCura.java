package Desafio.itens;

import Desafio.personagem.Personagem;

public class PocaoCura extends Item {
    private int cura;

    public PocaoCura() {
        super("Poção de Cura", 10);
        this.cura = 25;
    }

    @Override
    public void usar(Personagem usuario) {
        usuario.curar(cura);
    }
}
