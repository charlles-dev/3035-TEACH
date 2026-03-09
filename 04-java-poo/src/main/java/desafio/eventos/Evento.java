package desafio.eventos;

import desafio.personagem.Personagem;

public interface Evento {
    void executar(Personagem jogador);
}
