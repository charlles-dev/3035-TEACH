package Estrutura_sequencial;

import java.util.Scanner;

public class atividade_02 {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        System.out.println("Digite o primeiro numero: ");
        int num1 = teclado.nextInt();
        System.out.println("Digite o segundo numero: ");
        int num2 = teclado.nextInt();
        System.out.println("Resultado:" + (num1 + num2));
        teclado.close();
    }
}
