package Colections;
import java.util.HashSet;

public class atividade_02 {
    public static void main(String[] args) {
        // Cria um HashSet de cores e adicione pelo menos 5 cores a ele.
        HashSet<String> cores = new HashSet<>();
        cores.add("Vermelho");
        cores.add("Verde");
        cores.add("Azul");
        cores.add("Amarelo");
        cores.add("Preto");

        // Imprime o tamanho do HashSet.
        System.out.println("Tamanho do HashSet: " + cores.size());

        // Tenta adicionar uma cor que já existe no conjunto.
        // Como o HashSet lida com elementos duplicados? Ele simplesmente ignora a adição e retorna false.
        boolean adicionou = cores.add("Azul");
        System.out.println("Tentou adicionar 'Azul' novamente? " + (adicionou ? "Sim" : "Não (Já existe)"));
        System.out.println("Tamanho após tentar adicionar duplicata: " + cores.size());

        // Remove uma cor do conjunto.
        cores.remove("Preto");
        System.out.println("Cor 'Preto' removida.");

        // Verifique se uma cor específica está no conjunto.
        String corParaVerificar = "Verde";
        if (cores.contains(corParaVerificar)) {
            System.out.println("A cor " + corParaVerificar + " está no conjunto.");
        } else {
            System.out.println("A cor " + corParaVerificar + " não está no conjunto.");
        }

        // Itere sobre o conjunto e imprima todas as cores.
        System.out.println("Lista de cores no conjunto:");
        for (String cor : cores) {
            System.out.println(cor);
        }
    }
}
