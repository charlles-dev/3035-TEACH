package Desafio.batalha;

import Desafio.personagem.Inimigo;
import Desafio.personagem.Personagem;
import Desafio.utilitarios.ConsoleUtils;

public class Batalha {
    private Personagem jogador;
    private Inimigo inimigo;
    private int turno;

    public Batalha(Personagem jogador, Inimigo inimigo) {
        this.jogador = jogador;
        this.inimigo = inimigo;
        this.turno = 1;
    }

    public boolean iniciar() {
        System.out.println("\n==================================");
        System.out.println("BATALHA INICIADA!");
        System.out.println("Você encontrou um " + inimigo.getNome() + " selvagem!");
        System.out.println("==================================\n");

        while (jogador.estaVivo() && inimigo.estaVivo()) {
            System.out.println("--- Turno " + turno + " ---");
            exibirStatus();

            // Turno do jogador
            turnoJogador();

            if (!inimigo.estaVivo()) {
                System.out.println("\nVitória! Você derrotou o " + inimigo.getNome() + "!");
                return true; // Jogador venceu
            }

            ConsoleUtils.pausar(1000);

            // Turno do inimigo
            turnoInimigo();

            if (!jogador.estaVivo()) {
                System.out.println("\nDerrota... O " + inimigo.getNome() + " venceu a batalha.");
                return false; // Jogador perdeu
            }

            turno++;
            ConsoleUtils.pausar(1000);
            System.out.println("\n");
        }
        return false;
    }

    private void exibirStatus() {
        System.out.println(ConsoleUtils.CYAN + jogador.getNome() + " (" + jogador.getClasse() + ")" + ConsoleUtils.RESET);
        ConsoleUtils.exibirBarra("Vida", jogador.getVida(), jogador.getVidaMaxima(), ConsoleUtils.GREEN);
        ConsoleUtils.exibirBarra("Mana", jogador.getMana(), jogador.getManaMaxima(), ConsoleUtils.BLUE);
        
        System.out.println("\n" + ConsoleUtils.RED + inimigo.getNome() + ConsoleUtils.RESET);
        ConsoleUtils.exibirBarra("Vida", inimigo.getVida(), inimigo.getVidaMaxima(), ConsoleUtils.RED);
        System.out.println();
    }

    private void turnoJogador() {
        jogador.resetarDefesa(); // Reseta a defesa do turno anterior
        boolean acaoValida = false;

        while (!acaoValida) {
            System.out.println("Escolha sua ação:");
            System.out.println("1 - Atacar");
            System.out.println("2 - Defender");
            System.out.println("3 - Usar Item");
            System.out.println("4 - Habilidade Especial");

            int escolha = ConsoleUtils.lerInteiro("Sua escolha: ");

            System.out.println();
            switch (escolha) {
                case 1:
                    jogador.atacar(inimigo);
                    acaoValida = true;
                    break;
                case 2:
                    jogador.defender();
                    acaoValida = true;
                    break;
                case 3:
                    if (jogador.getInventario().isEmpty()) {
                        System.out.println("Inventário vazio! Escolha outra ação.");
                    } else {
                        jogador.usarItem();
                        acaoValida = true;
                    }
                    break;
                case 4:
                    jogador.usarHabilidadeEspecial(inimigo);
                    acaoValida = true;
                    break;
                default:
                    System.out.println("Ação inválida. Tente novamente.\n");
            }
        }
    }

    private void turnoInimigo() {
        System.out.println("--- Turno do Inimigo ---");
        inimigo.resetarDefesa();
        inimigo.acaoIA(jogador);
    }
}
