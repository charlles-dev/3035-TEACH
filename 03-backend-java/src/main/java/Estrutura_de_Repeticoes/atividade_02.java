package Estrutura_de_Repeticoes;

import java.util.Random;
import java.util.Scanner;

public class atividade_02 {
    static void main() {
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        int numeroRandomico = random.nextInt(100) + 1;
        int numeroDigitado = 0;
        System.out.println("Adivinhe o número entre 1 e 100!");

        while (numeroDigitado != numeroRandomico) {
            System.out.print("Seu palpite: ");
            numeroDigitado = scanner.nextInt();

            if (numeroDigitado < numeroRandomico) {
                System.out.println("Muito baixo!");
            } else if (numeroDigitado > numeroRandomico) {
                System.out.println("Muito alto!");
            } else {
                System.out.println("Parabéns! Você acertou.");
            }
        }
        scanner.close();
    }
}
