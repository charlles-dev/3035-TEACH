package Estrutura_de_Repeticoes;

import java.util.Scanner;

public class atividade_03 {
    static void main() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite um número de 1 a 10: ");
        int numero = scanner.nextInt();

        if (numero >= 1 && numero <= 10) {
            System.out.println("Tabuada do " + numero + ":");
            for (int i = 1; i <= 10; i++) {
                System.out.println(numero + " x " + i + " = " + (numero * i));
            }
        } else {
            System.out.println("Número inválido! Por favor, digite um número entre 1 e 10.");
        }

        scanner.close();
    }
}
