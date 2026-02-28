package Colections;

import java.util.ArrayList;
import java.util.HashSet;

public class atividade_03 {
    public static void main(String[] args) {
        // Cria um ArrayList e um HashSet contendo números inteiros.
        ArrayList<Integer> arrayList = new ArrayList<>();
        HashSet<Integer> hashSet = new HashSet<>();

        // Adiciona os mesmos números em ambas as coleções.
        int[] numeros = { 50, 10, 30, 20, 40 };

        for (int num : numeros) {
            arrayList.add(num);
            hashSet.add(num);
        }

        // Imprime os elementos do ArrayList e do HashSet.
        System.out.println("Elementos no ArrayList (ordem de inserção):");
        System.out.println(arrayList);

        System.out.println("\nElementos no HashSet (ordem não garantida):");
        System.out.println(hashSet);

        // Explicação da diferença entre a ordem dos elementos nos dois:
        /*
         * O ArrayList mantém a ordem de inserção dos elementos, funcionando como uma
         * lista indexada.
         * O HashSet não garante nenhuma ordem específica para os elementos, pois
         * utiliza uma tabela hash
         * para armazenamento, o que prioriza a performance de busca e evita duplicatas,
         * mas perde a
         * informação da sequência em que os itens foram adicionados.
         */
    }
}
