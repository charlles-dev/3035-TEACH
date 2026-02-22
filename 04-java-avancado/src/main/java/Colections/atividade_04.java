package Colections;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class atividade_04 {
    public static void main(String[] args) {
        // Cria um ArrayList de palavras.
        List<String> listaPalavras = new ArrayList<>();
        listaPalavras.add("Java");
        listaPalavras.add("Python");
        listaPalavras.add("JavaScript");
        listaPalavras.add("C++");
        listaPalavras.add("Ruby");

        System.out.println("ArrayList original (ordem de inserção):");
        System.out.println(listaPalavras);

        // Converte o ArrayList em um HashSet.
        Set<String> setPalavras = new HashSet<>(listaPalavras);

        // Imprime os elementos do HashSet.
        System.out.println("\nHashSet após conversão (ordem não garantida):");
        System.out.println(setPalavras);

        // Converte o HashSet de volta para um ArrayList.
        List<String> listaResultante = new ArrayList<>(setPalavras);

        //  Imprima os elementos do ArrayList resultante.
        System.out.println("\nArrayList resultante (de volta do HashSet):");
        System.out.println(listaResultante);

        // Observe se a ordem dos elementos muda durante as conversões.
        /*
         * Observação: A ordem dos elementos pode mudar ao converter o ArrayList para HashSet,
         * pois o HashSet não garante a ordem de inserção. Ao converter de volta para ArrayList,
         * a nova lista manterá a ordem que os elementos tinham no HashSet no momento da conversão,
         * que pode ser diferente da ordem original do primeiro ArrayList.
         */
    }
}
