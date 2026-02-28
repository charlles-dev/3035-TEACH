package Estrutura_Condicional;

import java.util.Scanner;

public class atividade_01 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite o valor de A: ");
        double a = sc.nextDouble();
        System.out.println("Digite o valor de B: ");
        double b = sc.nextDouble();
        System.out.println("Digite o valor de C: ");
        double c = sc.nextDouble();

        if (a + b < c) {
            System.out.println("A soma de A + B é menor que C.");
        } else {
            System.out.println("A soma de A + B NÃO é menor que C.");
        }
        sc.close();
    }
}
