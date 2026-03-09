package desafio;

import desafio.utilitarios.MenuInterativo;

/**
 * Ponto de entrada da aplicação.
 * Inicia a instância do jogo e chama o loop principal.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Bem-vindo a Arena dos Campeões!");
        System.out.println("1 - Jogar com Setas do Teclado (Recomendado)");
        System.out.println("2 - Jogar com Digitação (Modo Clássico)");

        // Antes de ter o MenuInterativo funcionando, usamos o scanner nativo via
        // ConsoleUtils
        int modo = desafio.utilitarios.ConsoleUtils.lerInteiro("Sua escolha: ");
        if (modo == 1) {
            MenuInterativo.setModoSetas(true);
        } else {
            MenuInterativo.setModoSetas(false);
        }

        Jogo jogo = new Jogo();
        jogo.iniciar();
    }
}
