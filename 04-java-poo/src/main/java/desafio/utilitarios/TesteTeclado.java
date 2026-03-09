package desafio.utilitarios;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.Attributes;

import java.io.IOException;

public class TesteTeclado {
    public static void main(String[] args) throws IOException {
        Terminal terminal = TerminalBuilder.builder().system(true).dumb(true).build();
        System.out.println("Terminal: " + terminal.getClass().getName());
        Attributes attr = terminal.enterRawMode();
        System.out.println("Pressione as setas e veja o retorno (pressione 'q' para sair):");
        try {
            while (true) {
                int ch = terminal.reader().read();
                System.out.println("Key: " + ch + " (char: " + (char) ch + ")");
                if (ch == 'q')
                    break;
            }
        } finally {
            terminal.setAttributes(attr);
        }
    }
}
