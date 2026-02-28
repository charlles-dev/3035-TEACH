package Estrutura_Condicional;

import java.util.Scanner;

public class atividade_05 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite um número: ");
        int numero = sc.nextInt();
        int resultado;
        if (numero >= 0) {
            resultado = numero * 2;
        } else {
            resultado = numero * 3;
        }
        System.out.println("O resultado é: " + resultado);
        sc.close();
    }
}
