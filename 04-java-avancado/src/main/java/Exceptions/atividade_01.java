package Exceptions;

import java.util.Scanner;

public class atividade_01 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Digite o numerador: ");
            int numerador = scanner.nextInt();

            System.out.print("Digite o denominador: ");
            int denominador = scanner.nextInt();

            int resultado = numerador / denominador;
            System.out.println("O resultado da divisão é: " + resultado);
        } catch (ArithmeticException e) {
            System.err.println("Erro: Divisão por zero não é permitida.");
        } catch (Exception e) {
            System.err.println("Erro: Entrada inválida. Por favor, insira números inteiros.");
        } finally {
            scanner.close();
        }
    }
}
