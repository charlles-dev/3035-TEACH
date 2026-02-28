package Estrutura_Condicional;

import java.util.Scanner;

public class atividade_07 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite o primeiro valor: ");
        int a = sc.nextInt();
        System.out.println("Digite o segundo valor: ");
        int b = sc.nextInt();
        System.out.println("Digite o terceiro valor: ");
        int c = sc.nextInt();

        if (a > b && a > c) {
            if (b > c) {
                System.out.println("Ordem decrescente: " + a + ", " + b + ", " + c);
            } else {
                System.out.println("Ordem decrescente: " + a + ", " + c + ", " + b);
            }
        } else if (b > a && b > c) {
            if (a > c) {
                System.out.println("Ordem decrescente: " + b + ", " + a + ", " + c);
            } else {
                System.out.println("Ordem decrescente: " + b + ", " + c + ", " + a);
            }
        } else {
            if (a > b) {
                System.out.println("Ordem decrescente: " + c + ", " + a + ", " + b);
            } else {
                System.out.println("Ordem decrescente: " + c + ", " + b + ", " + a);
            }
        }
        sc.close();
    }
}
