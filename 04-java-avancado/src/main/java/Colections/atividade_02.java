package Colections;

import java.util.HashSet;
import java.util.Iterator;

public class atividade_02 {
    public static void main(String[] args) {
        // Cria um HashSet de Strings para armazenar nomes de cores.
        HashSet<String> cores = new HashSet<>();
        cores.add("Vermelho");
        cores.add("Verde");
        cores.add("Azul");
        cores.add("Amarelo");

        // Tenta adicionar uma cor já existente no conjunto e explico o que acontece.
        boolean adicionou = cores.add("Verde");
        System.out.println("Tentativa de adicionar 'Verde' novamente. Sucesso? " + adicionou);
        // Explicação: O método 'add' retorna false porque HashSets não permitem
        // elementos duplicados.
        // A cor 'Verde' já estava no conjunto.

        // Imprime o conjunto completo de cores.
        System.out.println("Conjunto de cores: " + cores);

        // Verifica se uma cor específica está no conjunto.
        String corParaVerificar = "Azul";
        if (cores.contains(corParaVerificar)) {
            System.out.println("A cor " + corParaVerificar + " está no conjunto.");
        } else {
            System.out.println("A cor " + corParaVerificar + " NÃO está no conjunto.");
        }

        // Remove uma cor do conjunto e depois imprime o conjunto atualizado.
        cores.remove("Amarelo");
        System.out.println("Conjunto após remover 'Amarelo': " + cores);

        // Usa um Iterator para percorrer os elementos do HashSet e imprima cada um em
        // uma nova linha.
        System.out.println("Iterando sobre as cores:");
        Iterator<String> iterator = cores.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
