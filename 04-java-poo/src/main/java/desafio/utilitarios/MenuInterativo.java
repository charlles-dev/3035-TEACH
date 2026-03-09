package desafio.utilitarios;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.Attributes;

import java.io.IOException;

public class MenuInterativo {

    private static Terminal terminal;
    private static boolean modoSetasAtivo = false;

    // Inicializa o terminal JLine apenas uma vez
    private static void inicializarTerminal() {
        if (terminal == null) {
            try {
                // system(true) tenta usar o terminal nativo do SO via JNA
                terminal = TerminalBuilder.builder().system(true).jna(true).dumb(true).build();

                // Se o terminal não tiver suporte avançado, nós forçamos o fallback
                if (terminal.getType().equals(Terminal.TYPE_DUMB)
                        || terminal.getType().equals(Terminal.TYPE_DUMB_COLOR)) {
                    System.out.println(
                            "Aviso: Seu terminal não suporta teclas ao vivo. O jogo mudará para o modo comum.");
                    modoSetasAtivo = false;
                }
            } catch (IOException e) {
                System.err.println("Erro ao inicializar terminal interativo: " + e.getMessage());
                modoSetasAtivo = false; // Fallback forçado
            }
        }
    }

    public static void setModoSetas(boolean ativo) {
        modoSetasAtivo = ativo;
        if (ativo) {
            inicializarTerminal();
        }
    }

    public static boolean isModoSetasAtivo() {
        return modoSetasAtivo;
    }

    /**
     * Exibe um menu interativo onde a renderização do topo (status, HUD) é feita
     * por um Runnable.
     * Retorna o índice da opção (1-based).
     */
    public static int escolherOpcao(Runnable desenharCabecalho, String... opcoes) {
        if (!modoSetasAtivo || terminal == null) {
            return modoScannerFallback(desenharCabecalho, opcoes);
        }

        int indexAtual = 0;
        boolean escolhendo = true;

        Attributes atributosOriginais = terminal.enterRawMode(); // Entra em modo raw para ler teclas sem Enter

        try {
            while (escolhendo) {
                // Limpa a porção de tela usada pelo menu para redesenhar
                // Retorna cursor para o início do menu: Move (opcoes.length + 2) linhas para
                // cima se não for primeira vez
                // Para simplificar, vamos usar limparConsole() antes de desenhar toda vez (o
                // jogo já faz isso no loopDeAventura)
                // Mas as vezes queremos menu in-place. O jeito simples para Java puro sem
                // bibliotecas de TUI pesadas
                // é limpar a tela inteira. Como a interface do jogo limpa muito a tela, podemos
                // manter o padrão.
                ConsoleUtils.limparConsole();

                if (desenharCabecalho != null) {
                    desenharCabecalho.run();
                }

                // Desenha opções
                for (int i = 0; i < opcoes.length; i++) {
                    if (i == indexAtual) {
                        terminal.writer()
                                .println("  " + ConsoleUtils.CYAN + "> " + opcoes[i] + " <" + ConsoleUtils.RESET);
                    } else {
                        terminal.writer().println("    " + opcoes[i]);
                    }
                }

                terminal.writer().print("\nUse as setas \u2191 \u2193 para navegar, ou Enter para confirmar.");
                terminal.writer().flush();

                // Ler entrada do teclado
                int key = terminal.reader().read();

                if (key == 27) { // Escape sequence (Setas)
                    int next1 = terminal.reader().read(100); // Timeout aumentado
                    if (next1 == 91 || next1 == 79) { // [ ou O
                        int next2 = terminal.reader().read(100);
                        if (next2 == 65) { // Seta pra Cima
                            indexAtual--;
                            if (indexAtual < 0)
                                indexAtual = opcoes.length - 1;
                        } else if (next2 == 66) { // Seta pra Baixo
                            indexAtual++;
                            if (indexAtual >= opcoes.length)
                                indexAtual = 0;
                        }
                    }
                } else if (key == 224 || key == 0) { // Windows escape sequence para setas em modo raw
                    int next1 = terminal.reader().read(100);
                    if (next1 == 72) { // Up
                        indexAtual--;
                        if (indexAtual < 0)
                            indexAtual = opcoes.length - 1;
                    } else if (next1 == 80) { // Down
                        indexAtual++;
                        if (indexAtual >= opcoes.length)
                            indexAtual = 0;
                    }
                } else if (key == 'w' || key == 'W') { // WASD fallback
                    indexAtual--;
                    if (indexAtual < 0)
                        indexAtual = opcoes.length - 1;
                } else if (key == 's' || key == 'S') { // WASD fallback
                    indexAtual++;
                    if (indexAtual >= opcoes.length)
                        indexAtual = 0;
                } else if (key == 13 || key == 10) { // Enter
                    escolhendo = false;
                }
            }
        } catch (IOException e) {
            System.err.println("Erro na leitura de teclado. Usando Fallback.");
            return modoScannerFallback(desenharCabecalho, opcoes);
        } finally {
            terminal.setAttributes(atributosOriginais); // Restaura terminal
        }

        System.out.println(); // Pula linha após selecionar
        return indexAtual + 1; // Retorna 1-based index
    }

    // Sobrecarga simples apenas para quem mandar string
    public static int escolherOpcao(String titulo, String... opcoes) {
        return escolherOpcao(() -> {
            if (titulo != null && !titulo.isEmpty()) {
                System.out.println(titulo);
                System.out.println();
            }
        }, opcoes);
    }

    private static int modoScannerFallback(Runnable desenharCabecalho, String... opcoes) {
        if (desenharCabecalho != null) {
            desenharCabecalho.run();
        }
        for (int i = 0; i < opcoes.length; i++) {
            System.out.println((i + 1) + " - " + opcoes[i]);
        }
        int escolha = 0;
        boolean valido = false;
        while (!valido) {
            escolha = ConsoleUtils.lerInteiro("\nSua escolha: ");
            if (escolha >= 1 && escolha <= opcoes.length) {
                valido = true;
            } else {
                System.out.println("Escolha inválida. Tente novamente.");
            }
        }
        return escolha;
    }
}
