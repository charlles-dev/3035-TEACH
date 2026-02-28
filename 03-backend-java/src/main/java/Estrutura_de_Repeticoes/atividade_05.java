package Estrutura_de_Repeticoes;

import java.util.Scanner;

public class atividade_05 {
    static void main() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite um número limite: ");
        int numero = scanner.nextInt();
        System.out.println("Números pares entre 1 e " + numero + ":");

        for (int i = 1; i <= numero; i++) {
            if (i % 2 == 0) {
                System.out.print(i + " ");
            }
        }
        scanner.close();
    }
}
