package Expressões_Lambdas;

import java.util.List;

public class atividade_03 {
    public static void main(String[] args) {
        List<String> palavras = List.of("java", "lambda", "stream", "api");
        List<String> maiusculas = converterParaMaiusculas(palavras);
        System.out.println("Palavras originais: " + palavras);
        System.out.println("Palavras em maiúsculas: " + maiusculas);
    }

    public static List<String> converterParaMaiusculas(List<String> strings) {
        return strings.stream()
                .map(String::toUpperCase)
                .toList(); // Mais limpo e garante imutabilidade da lista de retorno
    }
}
