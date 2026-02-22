package Colections;

import java.util.ArrayList;

public class atividade_01 {
    public static void main(String[] args) {
        // Cria um ArrayList de nomes de frutas e adicione pelo menos 5 nomes de frutas a ele.
        ArrayList<String> frutas = new ArrayList<>();
        frutas.add("Maçã");
        frutas.add("Banana");
        frutas.add("Morango");
        frutas.add("Uva");
        frutas.add("Abacaxi");

        // Imprime o tamanho do ArrayList.
        System.out.println("Tamanho do ArrayList: " + frutas.size());

        // Imprime o terceiro elemento do ArrayList.
        System.out.println("Terceiro elemento: " + frutas.get(2));

        // Remove a primeira fruta da lista.
        frutas.remove(0);
        System.out.println("Primeira fruta removida.");

        // Verifica se uma determinada fruta existe na lista.
        String frutaParaVerificar = "Morango";
        if (frutas.contains(frutaParaVerificar)) {
            System.out.println("A fruta " + frutaParaVerificar + " existe na lista.");
        } else {
            System.out.println("A fruta " + frutaParaVerificar + " não existe na lista.");
        }

        // Itera sobre a lista e imprima todas as frutas.
        System.out.println("Lista de frutas:");
        for (String fruta : frutas) {
            System.out.println(fruta);
        }
    }
}
