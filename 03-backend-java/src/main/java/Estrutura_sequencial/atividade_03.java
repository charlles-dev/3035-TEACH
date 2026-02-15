package Estrutura_sequencial;

import java.util.Locale;
import java.util.Scanner;

public class atividade_03 {
    public static void main(String[] args) {
        Locale.setDefault(Locale.of("pt", "BR"));
        Scanner sc = new Scanner(System.in);

        System.out.print("Informe seu salário: ");
        double salario = sc.nextDouble();

        System.out.printf("Salário: %.2f%n", salario);

        sc.close();
    }
}
