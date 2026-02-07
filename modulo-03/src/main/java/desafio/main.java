package desafio;

import java.util.Scanner;
import java.util.Random;

public class main {
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();
    
    // Histórico: [Pontuação][Dificuldade] (String para facilitar exibição)
    private static String[] historico = new String[10];
    private static int historicoIndex = 0;
    
    // Recordes por nível: Fácil, Médio, Difícil
    private static int[] recordes = {0, 0, 0};

    // Configurações: {Limite Máximo, Tentativas, Pontuação Base}
    private static final int[][] CONFIGS = {
        {50, 10, 100}, // Fácil
        {100, 7, 200}, // Médio
        {200, 5, 300}  // Difícil
    };

    public static void main(String[] args) {
        int opcao = 0;
        do {
            exibirMenuPrincipal();
            try {
                opcao = Integer.parseInt(scanner.nextLine());
                processarOpcaoMenu(opcao);
            } catch (NumberFormatException e) {
                System.out.println("Por favor, insira um número válido.");
            }
        } while (opcao != 4);
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n=== JOGO DE ADIVINHAÇÃO ===");
        System.out.println("1. Iniciar Novo Jogo");
        System.out.println("2. Ver Regras");
        System.out.println("3. Ver Histórico de Pontuações");
        System.out.println("4. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void processarOpcaoMenu(int opcao) {
        switch (opcao) {
            case 1:
                iniciarJogo();
                break;
            case 2:
                exibirRegras();
                break;
            case 3:
                exibirHistorico();
                break;
            case 4:
                System.out.println("Saindo do jogo... Até logo!");
                break;
            default:
                System.out.println("Opção inválida!");
        }
    }

    private static void exibirRegras() {
        System.out.println("\n=== REGRAS DO JOGO ===");
        System.out.println("Escolha um nível de dificuldade:");
        System.out.println("- Fácil: Números de 1 a 50, 10 tentativas.");
        System.out.println("- Médio: Números de 1 a 100, 7 tentativas.");
        System.out.println("- Difícil: Números de 1 a 200, 5 tentativas.");
        System.out.println("\nPontuação:");
        System.out.println("- Base: Fácil(100), Médio(200), Difícil(300).");
        System.out.println("- Bônus: +50 por tentativa não utilizada.");
        System.out.println("- Dicas têm custo de pontos.");
        System.out.println("\nModo Sequência: Adivinhe 3 números seguidos!");
    }

    private static void exibirHistorico() {
        System.out.println("\n=== HISTÓRICO DAS ÚLTIMAS 10 PONTUAÇÕES ===");
        boolean vazio = true;
        for (String registro : historico) {
            if (registro != null) {
                System.out.println(registro);
                vazio = false;
            }
        }
        if (vazio) System.out.println("Nenhum jogo registrado.");
        
        System.out.println("\n=== RECORDES POR NÍVEL ===");
        System.out.println("Fácil: " + recordes[0]);
        System.out.println("Médio: " + recordes[1]);
        System.out.println("Difícil: " + recordes[2]);
    }

    private static void iniciarJogo() {
        System.out.println("\nEscolha o Modo:");
        System.out.println("1. Normal");
        System.out.println("2. Modo Sequência (Desafio)");
        int modo = 0;
        try {
            modo = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) { modo = 1; }

        System.out.println("\nEscolha a Dificuldade:");
        System.out.println("1. Fácil");
        System.out.println("2. Médio");
        System.out.println("3. Difícil");
        int dificuldade = 0;
        try {
            dificuldade = Integer.parseInt(scanner.nextLine()) - 1;
            if (dificuldade < 0 || dificuldade > 2) throw new Exception();
        } catch (Exception e) {
            System.out.println("Dificuldade padrão: Fácil");
            dificuldade = 0;
        }

        if (modo == 2) {
            jogarModoSequencia(dificuldade);
        } else {
            jogarModoNormal(dificuldade);
        }
    }

    private static void jogarModoNormal(int dif) {
        int numeroSecreto = random.nextInt(CONFIGS[dif][0]) + 1;
        int tentativasRestantes = CONFIGS[dif][1];
        int pontuacao = CONFIGS[dif][2];
        boolean acertou = false;

        System.out.println("\nTente adivinhar o número entre 1 e " + CONFIGS[dif][0]);

        while (tentativasRestantes > 0 && !acertou) {
            System.out.println("\nTentativas restantes: " + tentativasRestantes + " | Pontos: " + pontuacao);
            System.out.println("Digite seu palpite ou 'D' para dica: ");
            String entrada = scanner.nextLine();

            if (entrada.equalsIgnoreCase("D")) {
                pontuacao = pedirDica(numeroSecreto, pontuacao, CONFIGS[dif][0]);
                continue;
            }

            try {
                int palpite = Integer.parseInt(entrada);
                if (palpite == numeroSecreto) {
                    acertou = true;
                    int bonus = (tentativasRestantes - 1) * 50;
                    pontuacao += bonus;
                    System.out.println("PARABÉNS! Você acertou!");
                    System.out.println("Bônus por tentativas: " + bonus);
                    System.out.println("Pontuação Final: " + pontuacao);
                    salvarHistorico(pontuacao, dif);
                    atualizarRecorde(pontuacao, dif);
                } else {
                    tentativasRestantes--;
                    if (palpite < numeroSecreto) {
                        System.out.println("O número é MAIOR.");
                    } else {
                        System.out.println("O número é MENOR.");
                    }
                    if (tentativasRestantes > 0) pontuacao -= 5; // Pequena penalidade por erro
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida.");
            }
        }

        if (!acertou) {
            System.out.println("\nGame Over! O número era: " + numeroSecreto);
        }
    }

    private static void jogarModoSequencia(int dif) {
        int[] sequencia = {
            random.nextInt(CONFIGS[dif][0]) + 1,
            random.nextInt(CONFIGS[dif][0]) + 1,
            random.nextInt(CONFIGS[dif][0]) + 1
        };
        int pontuacao = CONFIGS[dif][2] * 2; // Dobro da pontuação base
        int tentativasTotais = CONFIGS[dif][1] + 5; 
        int numerosAcertados = 0;

        System.out.println("\nMODO SEQUÊNCIA: Adivinhe 3 números entre 1 e " + CONFIGS[dif][0]);

        while (numerosAcertados < 3 && tentativasTotais > 0) {
            System.out.println("\nAcertos: " + numerosAcertados + "/3 | Tentativas: " + tentativasTotais + " | Pontos: " + pontuacao);
            System.out.print("Adivinhe o " + (numerosAcertados + 1) + "º número: ");
            
            try {
                int palpite = Integer.parseInt(scanner.nextLine());
                if (palpite == sequencia[numerosAcertados]) {
                    System.out.println("Correto!");
                    numerosAcertados++;
                } else {
                    tentativasTotais--;
                    if (palpite < sequencia[numerosAcertados]) System.out.println("MAIOR");
                    else System.out.println("MENOR");
                    pontuacao -= 10;
                }
            } catch (Exception e) { System.out.println("Inválido"); }
        }

        if (numerosAcertados == 3) {
            pontuacao += (tentativasTotais * 50);
            System.out.println("Você completou a sequência! Pontuação: " + pontuacao);
            salvarHistorico(pontuacao, dif, true);
            atualizarRecorde(pontuacao, dif);
        } else {
            System.out.println("Falhou! Sequência era: [" + sequencia[0] + ", " + sequencia[1] + ", " + sequencia[2] + "]");
        }
    }

    private static int pedirDica(int numero, int pontos, int max) {
        System.out.println("Escolha a dica:");
        System.out.println("1. Paridade (-10 pts)");
        System.out.println("2. Intervalo (Metade) (-20 pts)");
        System.out.println("3. Proximidade (Próximo palpite) (-15 pts)");
        
        try {
            int tipo = Integer.parseInt(scanner.nextLine());
            switch (tipo) {
                case 1:
                    if (pontos < 10) { System.out.println("Pontos insuficientes!"); return pontos; }
                    System.out.println("O número é " + (numero % 2 == 0 ? "PAR" : "ÍMPAR"));
                    return pontos - 10;
                case 2:
                    if (pontos < 20) { System.out.println("Pontos insuficientes!"); return pontos; }
                    if (numero <= max / 2) System.out.println("Está na metade INFERIOR (1-" + (max/2) + ")");
                    else System.out.println("Está na metade SUPERIOR (" + (max/2 + 1) + "-" + max + ")");
                    return pontos - 20;
                case 3:
                    if (pontos < 15) { System.out.println("Pontos insuficientes!"); return pontos; }
                    System.out.print("Qual seu palpite para testar proximidade? ");
                    int p = Integer.parseInt(scanner.nextLine());
                    int diff = Math.abs(p - numero);
                    if (diff <= 5) System.out.println("QUENTE! (Diferença <= 5)");
                    else System.out.println("FRIO! (Diferença > 5)");
                    return pontos - 15;
            }
        } catch (Exception e) { System.out.println("Erro ao processar dica."); }
        return pontos;
    }

    private static void salvarHistorico(int pontos, int dif) {
        salvarHistorico(pontos, dif, false);
    }

    private static void salvarHistorico(int pontos, int dif, boolean seq) {
        String nivel = (dif == 0 ? "Fácil" : (dif == 1 ? "Médio" : "Difícil"));
        if (seq) nivel += " (Seq)";
        historico[historicoIndex] = "Pontos: " + pontos + " | Nível: " + nivel;
        historicoIndex = (historicoIndex + 1) % 10;
    }

    private static void atualizarRecorde(int pontos, int dif) {
        if (pontos > recordes[dif]) {
            recordes[dif] = pontos;
            System.out.println("NOVO RECORDE para o nível " + (dif == 0 ? "Fácil" : (dif == 1 ? "Médio" : "Difícil")) + "!");
        }
    }
}
