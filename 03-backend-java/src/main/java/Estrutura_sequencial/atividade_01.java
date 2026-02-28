package Estrutura_sequencial;

import java.util.Scanner;

public class atividade_01 {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        System.out.println("Digite seu nome: ");
        String nome = teclado.nextLine();
        System.out.println("Digite sua idade: ");
        int idade = teclado.nextInt();

        System.out.println("Olá, " + nome + "! Você tem " + idade + " anos.");
        teclado.close();
    }

}