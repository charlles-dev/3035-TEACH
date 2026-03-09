package desafio_poo;

import desafio_poo.batalha.Batalha;
import desafio_poo.eventos.BauMisterioso;
import desafio_poo.eventos.FonteSagrada;
import desafio_poo.personagem.*;
import desafio_poo.utilitarios.ConsoleUtils;
import desafio_poo.utilitarios.GeradorInimigos;
import desafio_poo.utilitarios.MenuInterativo;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Jogo {
    private Personagem jogador;
    private int inimigosDerrotados;
    private int nivelDificuldade;

    // Bônus: Ranking (Pontuação máxima alcançada)
    private static Map<String, Integer> ranking = new HashMap<>();
    private static final String RANKING_FILE = "ranking.txt";

    public Jogo() {
        this.inimigosDerrotados = 0;
        this.nivelDificuldade = 1;
        carregarRanking();
    }

    private void carregarRanking() {
        File file = new File(RANKING_FILE);
        if (!file.exists())
            return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 2) {
                    ranking.put(partes[0], Integer.parseInt(partes[1]));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao carregar ranking.");
        }
    }

    private void salvarRankingEmArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RANKING_FILE))) {
            for (Map.Entry<String, Integer> entry : ranking.entrySet()) {
                writer.write(entry.getKey() + ";" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar ranking.");
        }
    }

    public void iniciar() {
        boolean jogoAtivo = true;

        while (jogoAtivo) {
            int escolha = MenuInterativo.escolherOpcao(() -> {
                System.out.println(ConsoleUtils.YELLOW + "==================================" + ConsoleUtils.RESET);
                System.out.println(ConsoleUtils.CYAN + "      ARENA DOS CAMPEÕES RPG      " + ConsoleUtils.RESET);
                System.out.println(ConsoleUtils.YELLOW + "==================================" + ConsoleUtils.RESET);
            }, "Novo Jogo", "Ver Ranking", "Sair");

            switch (escolha) {
                case 1:
                    novoJogo();
                    break;
                case 2:
                    exibirRanking();
                    break;
                case 3:
                    ConsoleUtils.imprimirDigitando("Obrigado por jogar!");
                    salvarRankingEmArquivo();
                    jogoAtivo = false;
                    break;
                default:
                    System.out.println("Opção inválida.");
                    ConsoleUtils.pausar(1500);
            }
        }
    }

    private void novoJogo() {
        ConsoleUtils.limparConsole();
        String nome = ConsoleUtils.lerString("Qual o nome do seu herói? ");

        System.out.println();

        boolean classeEscolhida = false;
        while (!classeEscolhida) {
            int r = MenuInterativo.escolherOpcao(() -> {
                System.out.println(ConsoleUtils.YELLOW + "\n==================================" + ConsoleUtils.RESET);
                System.out.println("Escolha sua classe:");
            },
                    ConsoleUtils.GREEN + "Guerreiro" + ConsoleUtils.RESET + " (Vida e defesa altas)",
                    ConsoleUtils.PURPLE + "Mago" + ConsoleUtils.RESET + "      (Ataque muito alto, vida baixa)",
                    ConsoleUtils.BLUE + "Arqueiro" + ConsoleUtils.RESET + "  (Equilibrado)");

            switch (r) {
                case 1:
                    jogador = new Guerreiro(nome);
                    classeEscolhida = true;
                    break;
                case 2:
                    jogador = new Mago(nome);
                    classeEscolhida = true;
                    break;
                case 3:
                    jogador = new Arqueiro(nome);
                    classeEscolhida = true;
                    break;
                default:
                    System.out.println("Escolha inválida.");
            }
        }

        // Resetar status
        inimigosDerrotados = 0;
        jogador.setOuro(20); // Começa com 20 de ouro
        nivelDificuldade = 1;

        ConsoleUtils.imprimirDigitando(
                "\nHerói criado com sucesso! " + ConsoleUtils.CYAN + jogador.getNome() + ConsoleUtils.RESET
                        + " o " + jogador.getClasse() + ".",
                20);
        ConsoleUtils.pausar(2000);

        loopDeAventura();
    }

    private void loopDeAventura() {
        boolean emAventura = true;

        while (emAventura && jogador.estaVivo()) {
            int acao = MenuInterativo.escolherOpcao(() -> {
                System.out.println(ConsoleUtils.YELLOW + "----------- ACAMPAMENTO -----------" + ConsoleUtils.RESET);
                System.out
                        .println(ConsoleUtils.CYAN + jogador.getNome() + ConsoleUtils.RESET + " | "
                                + jogador.getClasse());
                ConsoleUtils.exibirBarra("Vida", jogador.getVida(), jogador.getVidaMaxima(), ConsoleUtils.GREEN);
                ConsoleUtils.exibirBarra("Mana", jogador.getMana(), jogador.getManaMaxima(), ConsoleUtils.BLUE);
                System.out.println("Inimigos derrotados: " + inimigosDerrotados);
                System.out.println("Ouro: " + ConsoleUtils.YELLOW + jogador.getOuro() + ConsoleUtils.RESET);
                System.out.println(ConsoleUtils.YELLOW + "-----------------------------------" + ConsoleUtils.RESET);
            }, "Procurar Próxima Batalha", "Ir à Loja", "Fugir (Voltar ao Menu Principal)");

            switch (acao) {
                case 1:
                    Inimigo inimigo = GeradorInimigos.gerarInimigoAleatorio(nivelDificuldade);
                    Batalha batalha = new Batalha(jogador, inimigo);
                    boolean vitoria = batalha.iniciar();

                    if (vitoria) {
                        inimigosDerrotados++;
                        int ouroDropado = 10 + (int) (Math.random() * nivelDificuldade * 5);
                        jogador.adicionarOuro(ouroDropado);
                        ConsoleUtils
                                .imprimirDigitando("Você encontrou " + ConsoleUtils.YELLOW + ouroDropado + " de ouro"
                                        + ConsoleUtils.RESET + " no corpo do inimigo!");

                        // Aumenta dificuldade a cada 2 inimigos
                        if (inimigosDerrotados % 2 == 0) {
                            nivelDificuldade++;
                            ConsoleUtils.imprimirDigitando(
                                    ConsoleUtils.RED + "Os inimigos estão ficando mais fortes..." + ConsoleUtils.RESET);
                        }

                        ConsoleUtils.pausar(1500);

                        // Chance de evento aleatório após batalha
                        if (Math.random() > 0.6) {
                            int chanceEvento = (int) (Math.random() * 2);
                            if (chanceEvento == 0) {
                                new FonteSagrada().executar(jogador);
                            } else {
                                new BauMisterioso().executar(jogador);
                            }
                        }

                        ConsoleUtils.pausar(1000);
                    } else {
                        gameOver();
                        emAventura = false;
                    }
                    break;
                case 2:
                    Loja.visitar(jogador);
                    break;
                case 3:
                    ConsoleUtils.imprimirDigitando("Sua aventura termina aqui por enquanto.");
                    salvarPontuacao();
                    emAventura = false;
                    ConsoleUtils.pausar(2000);
                    break;
                default:
                    System.out.println("Ação inválida.");
                    ConsoleUtils.pausar(1000);
            }
        }
    }

    private void gameOver() {
        System.out.println("\n" + ConsoleUtils.RED + "==================================" + ConsoleUtils.RESET);
        System.out.println(ConsoleUtils.RED + "            GAME OVER             " + ConsoleUtils.RESET);
        System.out.println(ConsoleUtils.RED + "==================================" + ConsoleUtils.RESET);
        ConsoleUtils.imprimirDigitando(
                "O herói " + ConsoleUtils.CYAN + jogador.getNome() + ConsoleUtils.RESET + " pereceu em combate.", 30);
        System.out.println("Inimigos derrotados: " + inimigosDerrotados);

        salvarPontuacao();
        ConsoleUtils.pausar(3000);
    }

    private void salvarPontuacao() {
        // Se a pontuação (inimigosDerrotados) for maior que a anterior guarda essa.
        int pontuacaoAnterior = ranking.getOrDefault(jogador.getNome(), 0);
        if (inimigosDerrotados > pontuacaoAnterior) {
            ConsoleUtils.imprimirDigitando(ConsoleUtils.YELLOW + "\nNOVO RECORDE PESSOAL!" + ConsoleUtils.RESET, 20);
            ranking.put(jogador.getNome(), inimigosDerrotados);
            salvarRankingEmArquivo();
        }
    }

    private void exibirRanking() {
        ConsoleUtils.limparConsole();
        System.out.println(ConsoleUtils.YELLOW + "==================================" + ConsoleUtils.RESET);
        System.out.println(ConsoleUtils.CYAN + "        HALL DOS CAMPEÕES         " + ConsoleUtils.RESET);
        System.out.println(ConsoleUtils.YELLOW + "==================================" + ConsoleUtils.RESET);
        if (ranking.isEmpty()) {
            System.out.println("Nenhum herói registrou seu nome ainda.");
        } else {
            // Estilo mais Junior: em vez de Streams e Lambdas, usamos for-each clássico.
            // É mais verboso e fácil de ler para iniciantes, mas menos elegante.
            for (Map.Entry<String, Integer> entry : ranking.entrySet()) {
                System.out.println(ConsoleUtils.GREEN + entry.getKey() + ConsoleUtils.RESET + " - " + entry.getValue()
                        + " vitórias");
            }
        }
        System.out.println(ConsoleUtils.YELLOW + "==================================" + ConsoleUtils.RESET);
        ConsoleUtils.lerString("Pressione ENTER para voltar.");
    }
}
