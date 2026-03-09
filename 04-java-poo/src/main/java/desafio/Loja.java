package desafio;

import desafio.itens.PocaoCura;
import desafio.personagem.Personagem;
import desafio.utilitarios.ConsoleUtils;

public class Loja {
    public static void visitar(Personagem jogador) {
        boolean naLoja = true;

        while (naLoja) {
            int escolha = desafio.utilitarios.MenuInterativo.escolherOpcao(() -> {
                System.out.println(ConsoleUtils.YELLOW + "==================================" + ConsoleUtils.RESET);
                System.out.println(ConsoleUtils.CYAN + "        LOJA DO MERCADOR          " + ConsoleUtils.RESET);
                System.out.println(ConsoleUtils.YELLOW + "==================================" + ConsoleUtils.RESET);
                System.out.println("Seu Ouro: " + ConsoleUtils.YELLOW + jogador.getOuro() + ConsoleUtils.RESET);
                System.out.println("Inventário: " + jogador.getInventario().size() + " Item(ns)");
                System.out.println("\nBem-vindo! O que deseja comprar?");
            },
                    "Poção de Cura (10 Ouro)",
                    "Descansar (Recupera toda vida e mana) (25 Ouro)",
                    "Sair da Loja");

            switch (escolha) {
                case 1:
                    if (jogador.getOuro() >= 10) {
                        jogador.removerOuro(10);
                        jogador.getInventario().add(new PocaoCura());
                        System.out.println("Você comprou uma Poção de Cura!");
                    } else {
                        System.out.println("Ouro insuficiente!");
                    }
                    ConsoleUtils.pausar(1500);
                    break;
                case 2:
                    if (jogador.getOuro() >= 25) {
                        if (jogador.getVida() == jogador.getVidaMaxima()
                                && jogador.getMana() == jogador.getManaMaxima()) {
                            System.out.println("Você já está descansado!");
                        } else {
                            jogador.removerOuro(25);
                            jogador.curar(jogador.getVidaMaxima());
                            jogador.recuperarMana(jogador.getManaMaxima());
                            System.out.println("Você descansou e recuperou toda sua energia!");
                        }
                    } else {
                        System.out.println("Ouro insuficiente!");
                    }
                    ConsoleUtils.pausar(1500);
                    break;
                case 3:
                    System.out.println("Voltando à jornada...");
                    naLoja = false;
                    ConsoleUtils.pausar(1000);
                    break;
                default:
                    System.out.println("Opção inválida.");
                    ConsoleUtils.pausar(1000);
            }
        }
    }
}
