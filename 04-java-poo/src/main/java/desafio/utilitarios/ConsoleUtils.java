package desafio.utilitarios;

import java.util.Scanner;

public class ConsoleUtils {
    private static final Scanner scanner = new Scanner(System.in);

    public static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Por favor, digite um número inteiro.");
            }
        }
    }

    public static String lerString(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    public static void pausar(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void limparConsole() {
        // Tentativa de limpar console de forma genérica
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    // Cores ANSI
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String PURPLE = "\u001B[35m";

    public static void exibirBarra(String label, int atual, int max, String cor) {
        int tamanhoBarra = 20;
        double percentual = (double) atual / max;
        int preenchido = (int) (percentual * tamanhoBarra);

        // Formatando o label para ter tamanho fixo (ex: "Vida : [...]")
        System.out.printf("%-6s: [", label);
        System.out.print(cor);
        for (int i = 0; i < tamanhoBarra; i++) {
            if (i < preenchido) {
                System.out.print("█"); // Usando um bloco sólido para melhor visual
            } else {
                System.out.print("░"); // Reticências para vazio
            }
        }
        // Formatando os números para que fiquem alinhados
        System.out.print(RESET + "] " + String.format("%3d/%3d", atual, max) + "\n");
    }

    public static void imprimirDigitando(String texto, int delayMs) {
        for (char c : texto.toCharArray()) {
            System.out.print(c);
            System.out.flush();
            pausar(delayMs);
        }
        System.out.println();
    }

    public static void imprimirDigitando(String texto) {
        imprimirDigitando(texto, 15); // Default de 15ms por caractere
    }
}
