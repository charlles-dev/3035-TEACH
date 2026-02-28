package Exceptions;

import java.util.Scanner;

public class atividade_02 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Digite uma string para converter em inteiro: ");
            String entrada = scanner.nextLine();

            // Para testar o NullPointerException, poderíamos forçar a entrada a ser null
            // No entanto, o Scanner.nextLine() geralmente não retorna null.
            // Vou adicionar um pequeno bloco para demonstrar como lidar com null se
            // necessário.
            int numero = converterParaInteiro(entrada);
            System.out.println("Número convertido com sucesso: " + numero);

        } catch (NumberFormatException e) {
            System.err.println("Erro: A string fornecida não é um número inteiro válido.");
        } catch (NullPointerException e) {
            System.err.println("Erro: A string fornecida é nula.");
        } finally {
            scanner.close();
        }
    }

    public static int converterParaInteiro(String str) {
        if (str != null && str.equalsIgnoreCase("null")) {
            str = null; // Simulação para fins educacionais se o usuário digitar "null"
        }
        return Integer.parseInt(str);
    }
}
