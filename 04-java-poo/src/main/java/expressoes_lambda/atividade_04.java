package Expressões_Lambdas;

import java.util.List;

public class atividade_04 {
    public static void main(String[] args) {
        List<Integer> numeros = List.of(1, 2, 3, 4, 5);
        int soma = somarNumeros(numeros);
        System.out.println("Números originais: " + numeros);
        System.out.println("Soma total: " + soma);
    }

    public static int somarNumeros(List<Integer> numeros) {
        // mapToInt évita o custo de "Unboxing/Boxing" comparado a usar .reduce(0,
        // Integer::sum)
        return numeros.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }
}
