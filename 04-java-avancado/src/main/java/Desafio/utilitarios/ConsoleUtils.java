package Desafio.utilitarios;

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

        System.out.print(label + ": [");
        System.out.print(cor);
        for (int i = 0; i < tamanhoBarra; i++) {
            if (i < preenchido) {
                System.out.print("#");
            } else {
                System.out.print("-");
            }
        }
        System.out.print(RESET + "] " + atual + "/" + max + "\n");
    }
}
