package Desafio.eventos;

import Desafio.personagem.Personagem;
import Desafio.utilitarios.ConsoleUtils;

public class FonteSagrada implements Evento {
    @Override
    public void executar(Personagem jogador) {
        System.out.println(ConsoleUtils.CYAN + "\nVocê encontrou uma Fonte Sagrada!" + ConsoleUtils.RESET);
        System.out.println("As águas brilham com uma luz azulada.");
        System.out.println("Ao beber da água, você sente suas energias renovadas!");
        
        int cura = (int) (jogador.getVidaMaxima() * 0.3);
        int mana = (int) (jogador.getManaMaxima() * 0.3);
        
        jogador.curar(cura);
        jogador.recuperarMana(mana);
        
        System.out.println("Você recuperou " + ConsoleUtils.GREEN + cura + " de Vida" + ConsoleUtils.RESET + 
                           " e " + ConsoleUtils.BLUE + mana + " de Mana" + ConsoleUtils.RESET + ".");
        ConsoleUtils.pausar(2500);
    }
}
