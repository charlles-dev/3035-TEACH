package Desafio;

import Desafio.itens.PocaoCura;
import Desafio.personagem.Personagem;
import Desafio.utilitarios.ConsoleUtils;

public class Loja {
    public static void visitar(Personagem jogador) {
        boolean naLoja = true;

        while (naLoja) {
            ConsoleUtils.limparConsole();
            System.out.println("==================================");
            System.out.println("        LOJA DO MERCADOR          ");
            System.out.println("==================================");
            System.out.println("Seu Ouro: " + jogador.getOuro());
            System.out.println("Inventário: " + jogador.getInventario().size() + " Item(ns)");
            System.out.println("\nBem-vindo! O que deseja comprar?");
            System.out.println("1 - Poção de Cura (10 Ouro)");
            System.out.println("2 - Descansar (Recupera toda vida e mana) (25 Ouro)");
            System.out.println("3 - Sair da Loja");

            int escolha = ConsoleUtils.lerInteiro("\nEscolha: ");

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
                        if (jogador.getVida() == jogador.getVidaMaxima() && jogador.getMana() == jogador.getManaMaxima()) {
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
