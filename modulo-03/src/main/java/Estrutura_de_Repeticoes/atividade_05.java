package Estrutura_de_Repeticoes;

import java.util.Scanner;

public class atividade_05 {
    static void main() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite um número: ");
        int numero = scanner.nextInt();

        System.out.println("Números pares até " + numero + ":");
        for (int i = 0; i <= numero; i++) {
            if (i % 2 == 0) {
                System.out.println(i);
            }
        }

        scanner.close();
    }
}
