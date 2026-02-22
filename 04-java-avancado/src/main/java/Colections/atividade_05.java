package Colections;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class atividade_05 {
    public static void main(String[] args) {
        // Cria um ArrayList de números inteiros.
        List<Integer> numeros = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            numeros.add(i);
        }
        System.out.println("Lista inicial: " + numeros);

        // Remove todos os números pares da lista.
        numeros.removeIf(n -> n % 2 == 0);
        System.out.println("Lista após remover pares: " + numeros);

        // Cria um novo HashSet a partir dos números ímpares na lista.
        Set<Integer> conjuntoImpares = new HashSet<>(numeros);
        System.out.println("HashSet de ímpares: " + conjuntoImpares);

        // Verifica se o novo HashSet contém um número específico.
        int numeroProcurado = 5;
        if (conjuntoImpares.contains(numeroProcurado)) {
            System.out.println("O HashSet contém o número " + numeroProcurado + ".");
        } else {
            System.out.println("O HashSet não contém o número " + numeroProcurado + ".");
        }
    }
}
