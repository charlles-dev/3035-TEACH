# Sistema de Gerenciamento de Eventos - JPA

Bem-vindo ao **Sistema de Eventos Interativo**. Este projeto foi evoluído e modernizado para exibir uma verdadeira Interface de Linha de Comando (CLI) profissional com cores e uma experiência de usuário (UX) excelente nativa no terminal.

Desenvolvido utilizando as mais avançadas práticas do mundo corporativo Java: **JPA/Hibernate, Validação de Dados, Logging Profissional e isolamento de Banco de Dados com Docker.**

## 📋 Tecnologias e Setup

- **Linguagem:** Java 17+
- **ORM:** JPA (Jakarta Persistence API) 3.1 & Hibernate 6.4.4
- **Validação:** Hibernate Validator 8.0 (com Jakarta EL)
- **Log de Eventos:** SLF4J & Logback
- **Interface Terminal (UX/Cores):** JAnsi (org.fusesource.jansi)
- **Banco de Dados Oficial:** PostgreSQL (Isolado em Container Docker)
- **Gerenciador:** Maven

## ⚡ Como Executar e Subir o Ambiente

Para garantir a limpeza do seu ambiente e que não haja conflitos de porta com outros bancos na sua máquina, este projeto roda com banco de dados Dockerizado na porta **5433**.

**Passo 1: Subir o PostgreSQL via Docker**
Abra o terminal na raiz (`05-java-db`) e ligue **apenas** o serviço respectivo a esta atividade, poupando assim os gastos de CPU do seu computador com outros projetos:
```bash
docker compose up -d postgres
```
*Isso fará o download e rodará o postgres exclusivamente para o projeto de eventos. Seus dados ficaram salvos de forma segura no volume mapeado na sua máquina!*

**Passo 2: Iniciar a Aplicação**
Você pode rodar direto da sua IDE utilizando a classe **Main**.
Se preferir pelo terminal estruturado Maven:
```bash
mvn clean compile exec:java -Dexec.mainClass="JPA.Main"
```

## ✨ Funcionalidades do Sistema

A aplicação roda um menu infinito e interativo no Console (Terminal), permitindo ao usuário:
1. 📅 **Cadastrar Eventos** (Com validação estrita de data no padrão Brasileiro `DD/MM/YYYY`)
2. 👤 **Cadastrar Participantes** (Bloqueio automático contra e-mails inválidos ou duplicados)
3. 🎫 **Realizar Inscrição** (Relacionamento Many-to-Many entre Entidades)
4. 📋 **Painel Dinâmico de Eventos** (Tabela formatada que exibe todos os dados armazenados)
5. 👥 **Diretório Profissional de Participantes**

Toda transação possui **tratamento de exceção amigável** (sem "stacktraces" feias vermelhas subindo do nada para o usuário final) e proteção de blocos transacionais (Rollback & Commit automatizados).

## 🗄️ Modelo de Dados

### 1. Evento
- `id` (PK, AUTO_INCREMENT)
- `nome`
- `data`
- `local`

### 2. Participante
- `id` (PK, AUTO_INCREMENT)
- `nome`
- `email` (UNIQUE - Não pode possuir repetição no sistema)

### 3. Inscricao (JoinTable Modelada)
- `id` (PK)
- `evento_id` (Chave Estrangeira)
- `participante_id` (Chave Estrangeira)
- `confirmada` (Status da Inscrição)
- `dataInscricao` (Armazenamento cronológico via Timestamp)

## 🔧 Estrutura do Sistema Embutida

- **`docker-compose.yml`**: Seu gerente de banco de dados nativo. Cria, isola e conecta à porta segura `5433`. Senhas e roles pré-configuradas.
- **`persistence.xml`**: O elo entre os objetos Java e o Container Postgres, configurado com `<property name="hibernate.hbm2ddl.auto" value="update"/>` garantindo que toda vez que a aplicação fechar e for reiniciada, **SEUS DADOS SERÃO MANTIDOS** intactos (nenhum dado some quando a Main desliga).
- **`logback.xml`**: Define que logs limpos de status (`INFO`) irão para o seu Terminal coloridos, mas rastros técnicos densos de auditoria foram escondidos lá para um arquivo `application.log`.
- **`.gitignore`**: Arquivo adicionado para proteger os logs locais e o diretório `/target` de serem subidos acidentalmente ao GitHub do Professor.
