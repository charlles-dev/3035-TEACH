package Desafio.eventos;

import Desafio.personagem.Personagem;
import Desafio.utilitarios.ConsoleUtils;

public class BauMisterioso implements Evento {
    @Override
    public void executar(Personagem jogador) {
        System.out.println(ConsoleUtils.YELLOW + "\nVocê encontrou um Baú Misterioso!" + ConsoleUtils.RESET);
        System.out.println("O baú está coberto de poeira e teias de aranha.");
        
        System.out.println("Deseja tentar abrir o baú?");
        System.out.println("1 - Sim");
        System.out.println("2 - Não");
        
        int escolha = ConsoleUtils.lerInteiro("Sua escolha: ");
        
        if (escolha == 1) {
            double sorte = Math.random();
            if (sorte > 0.4) {
                int ouro = 20 + (int) (Math.random() * 30);
                System.out.println("O baú estava cheio de tesouros! Você encontrou " + 
                                   ConsoleUtils.YELLOW + ouro + " de ouro" + ConsoleUtils.RESET + ".");
                jogador.adicionarOuro(ouro);
            } else if (sorte > 0.15) {
                System.out.println(ConsoleUtils.RED + "Era uma armadilha!" + ConsoleUtils.RESET);
                int dano = (int) (jogador.getVidaMaxima() * 0.15);
                System.out.println("Setas dispararam do baú! Você recebeu " + dano + " de dano.");
                jogador.tomarDano(dano);
            } else {
                System.out.println("O baú estava vazio... que decepção.");
            }
        } else {
            System.out.println("Você decidiu ignorar o baú e seguir em frente.");
        }
        ConsoleUtils.pausar(2500);
    }
}
