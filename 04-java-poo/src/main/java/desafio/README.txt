================================================================================
                           ⚔️ ARENA DOS CAMPEÕES ⚔️
================================================================================

Um épico RPG de terminal baseado em turnos feito 100% em Java!

--------------------------------------------------------------------------------
🔥 SOBRE O PROJETO
--------------------------------------------------------------------------------
Este projeto é o Desafio Final do módulo de "Java Avançado e Orientação a Objetos".
Ele aplica na prática os pilares da POO (Herança, Polimorfismo, Encapsulamento),
Collections (Listas e Maps), Tratamento de Exceções, Generics, Lambdas e 
Manipulação de Arquivos (IO).

--------------------------------------------------------------------------------
⚙️ COMO COMPILAR E JOGAR
--------------------------------------------------------------------------------
O jogo foi estruturado com base no Maven para gerenciamento de dependências. 
Para jogar via terminal / prompt de comando, siga os passos abaixo:

1. Abra o terminal e navegue até a pasta raiz do projeto:
   cd 04-java-poo

2. Compile e execute o jogo usando o Maven:
   mvn compile exec:java -Dexec.mainClass="desafio.Main"

💡 Dica: No IntelliJ IDEA ou VS Code, basta abrir o projeto e executar a 
classe `Main.java`. A IDE gerenciará as dependências automaticamente (JLine3).

--------------------------------------------------------------------------------
🎮 MENU INTERATIVO (NOVIDADE)
--------------------------------------------------------------------------------
O jogo agora conta com integração com a biblioteca JLine3, permitindo uma
navegação fluida pelos menus utilizando as Setas do Teclado (↑ ↓) e Enter, 
além de teclas de atalho (W/S) como fallback para terminais mais simples.

--------------------------------------------------------------------------------
📜 FUNCIONALIDADES E SISTEMAS
--------------------------------------------------------------------------------
🛡️ CLASSES DE HERÓIS E HABILIDADES
• Guerreiro: Alta vida e defesa. 
  - Habilidade: Grito de Guerra (Aumenta defesa permanentemente).
• Mago: Ataque devastador e muita Mana. 
  - Habilidade: Bola de Fogo (Dano massivo consumindo Mana).
• Arqueiro: Equilibrado e ágil. 
  - Habilidade: Tiro Preciso (Dano que ignora parte da defesa).

✨ SISTEMA DE MANA
Gerencie seu recurso de Mana para usar habilidades especiais. Algumas classes 
dependem vitalmente desse recurso para sobreviver a inimigos poderosos.

🎒 INVENTÁRIO (POO) & LOJA
O inventário agora utiliza uma arquitetura de classes para Itens.
• Poções de Cura: Restauram vida durante o combate ou no acampamento.
• Sistema de Ouro Encapsulado: Derrote inimigos para acumular riqueza.
• Loja do Mercador: Compre itens ou descanse para recuperar Vida e Mana.

🎲 EVENTOS ALEATÓRIOS (Strategy Pattern)
Entre as batalhas, você pode encontrar eventos inesperados:
• Fonte Sagrada: Um momento de paz para restaurar suas energias.
• Baú Misterioso: Risco e recompensa! Pode conter ouro ou uma armadilha cruel.

🎨 INTERFACE VISUAL (CLI AVANÇADA)
• Cores ANSI: Feedback visual imediato (Vida em Verde, Perigo em Vermelho).
• Barras de Status: Acompanhe sua Vida e Mana com barras gráficas [#####-----].

💾 PERSISTÊNCIA DE DADOS
Seu progresso não é perdido! O Hall dos Campeões agora salva e carrega o 
ranking automaticamente de um arquivo `ranking.txt`.

--------------------------------------------------------------------------------
🛠️ ARQUITETURA DE CÓDIGO
--------------------------------------------------------------------------------
• /personagem : Abstrações de Personagem, Heróis específicos e IA de Inimigo.
• /batalha    : Motor de combate por turnos e lógica de mitigação.
• /itens      : Estrutura de classes para itens consumíveis.
• /eventos    : Sistema de eventos aleatórios (Padrão Strategy).
• /utilitarios: Cores ANSI, barras de status, leitura segura de dados e geradores.
• Jogo.java   : Orquestrador da aventura, persistência e loop principal.
