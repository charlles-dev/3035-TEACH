package Desafio.utilitarios;

import Desafio.personagem.Inimigo;
import java.util.Random;

public class GeradorInimigos {
    private static final String[] NOMES_INIMIGOS = {
            "Goblin", "Orc", "Troll", "Esqueleto", "Zumbi", "Lobo Selvagem", "Bandido", "Dragão Bebê"
    };

    private static final Random random = new Random();

    public static Inimigo gerarInimigoAleatorio(int dificuldade) {
        String nome = NOMES_INIMIGOS[random.nextInt(NOMES_INIMIGOS.length)];

        // Atributos escalam um pouco com a dificuldade
        int vidaBase = 40 + (dificuldade * 10);
        int ataqueBase = 5 + (dificuldade * 3);
        int defesaBase = 2 + (dificuldade * 2);

        // Variação aleatória
        int vida = vidaBase + random.nextInt(20);
        int ataque = ataqueBase + random.nextInt(5);
        int defesa = defesaBase + random.nextInt(3);

        return new Inimigo(nome, vida, ataque, defesa);
    }
}
