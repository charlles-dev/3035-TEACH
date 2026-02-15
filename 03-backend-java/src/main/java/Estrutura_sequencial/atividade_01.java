package Estrutura_sequencial;

import java.util.Scanner;

public class atividade_01 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite seu nome: ");
        String nome = sc.nextLine();
        System.out.println("Digite sua idade: ");
        int idade = sc.nextInt();

        System.out.println("Olá " + nome + " tudo bem? pelo que vi você tem " + idade + " anos");
        sc.close();
    }

}