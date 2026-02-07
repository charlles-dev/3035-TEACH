package Estrutura_Condicional;
import java.util.Scanner;

public class atividade_06 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Digite um número: ");
        int numero = sc.nextInt();

        int resultado;

        if (numero % 2 == 0) {
            resultado = numero + 5;
        } else {
            resultado = numero + 8;
        }

        System.out.println("O resultado da operação é: " + resultado);

        sc.close();
    }
}
