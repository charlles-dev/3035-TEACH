package Estrutura_sequencial;

import java.util.Locale;
import java.util.Scanner;

public class atividade_03 {
    public static void main(String[] args) {
        Locale.setDefault(Locale.of("pt", "BR"));
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o seu salário: ");
        double salario = scanner.nextDouble();

        System.out.println("O seu salário formatado é: R$ " + String.format("%.2f", salario));
        scanner.close();
    }
}
