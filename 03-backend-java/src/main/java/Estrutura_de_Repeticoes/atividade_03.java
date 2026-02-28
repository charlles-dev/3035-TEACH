package Estrutura_de_Repeticoes;

import java.util.Scanner;

public class atividade_03 {
    static void main() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Tabuada do número: ");
        int numero = scanner.nextInt();

        for (int i = 1; i <= 10; i++) {
            System.out.println(numero + " x " + i + " = " + (numero * i));
        }
        scanner.close();
    }
}
