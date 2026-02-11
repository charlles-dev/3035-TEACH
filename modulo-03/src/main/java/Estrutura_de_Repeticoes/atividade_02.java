package Estrutura_de_Repeticoes;

import java.util.Random;
import java.util.Scanner;

public class atividade_02 {
    static void main() {
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);

        int numeroRandomico = random.nextInt(101); // Entre 0 e 100
        int numeroDigitado = -1;

        System.out.println("Tente adivinhar o número entre 0 e 100!");

        while (numeroDigitado != numeroRandomico) {
            System.out.print("Digite um número: ");
            numeroDigitado = scanner.nextInt();

            if (numeroDigitado == numeroRandomico) {
                System.out.println("Parabéns! Você acertou.");
            } else if (numeroRandomico < numeroDigitado) {
                System.out.println("MAIOR");
            } else {
                System.out.println("MENOR");
            }
        }

        scanner.close();
    }
}
