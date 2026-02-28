package Estrutura_de_Repeticoes;

import java.util.Scanner;

public class atividade_04 {
    static void main() {
        Scanner scanner = new Scanner(System.in);
        int contadorMaioresDe18 = 0;

        for (int i = 1; i <= 5; i++) {
            System.out.print("Digite a idade da pessoa " + i + ": ");
            int idade = scanner.nextInt();
            if (idade >= 18) {
                contadorMaioresDe18++;
            }
        }
        System.out.println("Número de pessoas maiores de 18 anos: " + contadorMaioresDe18);
        scanner.close();
    }
}
