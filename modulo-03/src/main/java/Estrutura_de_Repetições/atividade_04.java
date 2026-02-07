package Estrutura_de_Repetições;

import java.util.Scanner;

public class atividade_04 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int contadorMaioresDe18 = 0;

        for (int i = 1; i <= 5; i++) {
            System.out.print("Digite a idade da " + i + "ª pessoa: ");
            int idade = scanner.nextInt();

            if (idade > 18) {
                contadorMaioresDe18++;
            }
        }

        System.out.println("Total de pessoas com mais de 18 anos: " + contadorMaioresDe18);

        scanner.close();
    }
}
