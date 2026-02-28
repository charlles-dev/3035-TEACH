package Colections;

import java.util.ArrayList;

public class atividade_01 {
    public static void main(String[] args) {
        // Cria um ArrayList de Strings para armazenar nomes de frutas.
        ArrayList<String> frutas = new ArrayList<>();
        frutas.add("Maçã");
        frutas.add("Banana");
        frutas.add("Laranja");
        frutas.add("Uva");
        frutas.add("Manga");

        // Imprime a lista completa de frutas.
        System.out.println("Lista de frutas: " + frutas);

        // Acessa e imprime uma fruta específica usando seu índice.
        System.out.println("A terceira fruta da lista é: " + frutas.get(2));

        // Remove uma fruta da lista pelo valor.
        frutas.remove("Banana");
        System.out.println("Lista após remover a Banana: " + frutas);

        // Verifica se a lista contém uma fruta específica e imprime um aviso se sim ou
        // se não.
        String frutaParaVerificar = "Maçã";
        if (frutas.contains(frutaParaVerificar)) {
            System.out.println("A lista contém a fruta: " + frutaParaVerificar);
        } else {
            System.out.println("A lista não contém a fruta: " + frutaParaVerificar);
        }

        // Usa um loop "for-each" para iterar sobre os elementos da lista e imprimi-los
        // um por linha.
        System.out.println("Imprimindo cada fruta em uma nova linha:");
        for (String fruta : frutas) {
            System.out.println(fruta);
        }
    }
}
