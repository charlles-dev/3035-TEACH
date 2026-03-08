package Expressões_Lambdas;

import java.util.List;

public class atividade_01 {
    public static void main(String[] args) {
        List<Integer> numeros = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> pares = filtrarPares(numeros);
        System.out.println("Números originais: " + numeros);
        System.out.println("Números pares: " + pares);
    }

    public static List<Integer> filtrarPares(List<Integer> numeros) {
        return numeros.stream()
                .filter(n -> n % 2 == 0)
                .toList();
    }
}
