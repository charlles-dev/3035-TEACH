package Estrutura_Condicional;

import java.util.Scanner;

public class atividade_02 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite o nome: ");
        String nome = sc.nextLine();
        System.out.println("Digite o sexo (M/F): ");
        String sexo = sc.nextLine().toUpperCase();
        System.out.println("Digite o estado civil: ");
        String estadoCivil = sc.nextLine().toUpperCase();

        if (sexo.equals("F") && estadoCivil.equals("CASADA")) {
            System.out.println("Digite o tempo de casada (anos): ");
            int tempoCasada = sc.nextInt();
            System.out.println(nome + " é casada há " + tempoCasada + " anos.");
        } else {
            System.out.println("Dados registrados para " + nome + ".");
        }
        sc.close();
    }
}
