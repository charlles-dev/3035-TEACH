package Exceptions;

public class atividade_03 {
    public static void main(String[] args) {
        String str = "123"; // Exemplo de string numérica
        // String str = "abc"; // Exemplo de string não numérica para testar a exceção

        try {
            System.out.println("Tentando converter a string para inteiro...");
            int numero = Integer.parseInt(str);
            System.out.println("Número convertido com sucesso: " + numero);
        } catch (NumberFormatException e) {
            System.err.println("Erro: A string fornecida não é um número inteiro válido.");
        } finally {
            System.out.println("Bloco finally executado: Independente de erro, este código sempre roda.");
        }
    }
}
