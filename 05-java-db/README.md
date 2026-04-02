# Sistema de Gerenciamento de Eventos - JPA

Sistema de gerenciamento de eventos com inscrições de participantes, desenvolvido com Java, JPA/Hibernate e H2 Database.

## Tecnologias

- Java 25
- JPA (Jakarta Persistence API) 3.1
- Hibernate 6.4.4
- **H2 Database** (banco em memória - **não precisa instalação!**)
- Maven

## Estrutura do Projeto

```
05-java-db/
├── src/main/java/JPA/
│   ├── Evento.java          # Entidade de eventos
│   ├── Participante.java    # Entidade de participantes
│   ├── Inscricao.java       # Entidade de inscrições (relacionamento)
│   └── Main.java            # Classe principal com demonstração
├── src/main/resources/META-INF/
│   └── persistence.xml      # Configuração JPA
└── pom.xml                  # Dependências Maven
```

## ⚡ Como Executar (Muito Simples!)

### Pré-requisitos
- Java 17+ instalado
- Maven instalado (ou use a IDE)

### Executar

**Opção 1: Via Maven**
```bash
cd 05-java-db
mvn clean compile exec:java -Dexec.mainClass="JPA.Main"
```

**Opção 2: Via IDE (IntelliJ/Eclipse)**
1. Abrir o projeto `05-java-db`
2. Aguardar o Maven baixar as dependências
3. Executar a classe `Main.java`

**Pronto! Não precisa configurar nada!** 🎉

O banco de dados H2 roda **em memória**, então:
- ✅ Não precisa instalar nenhum banco
- ✅ Não precisa configurar usuário/senha
- ✅ Não precisa criar database
- ✅ Funciona em qualquer sistema operacional
- ✅ Perfeito para demonstrações e testes

## Funcionalidades Demonstradas

O projeto demonstra as seguintes operações JPA:

1. ✅ **Criação de entidades** (Eventos, Participantes)
2. ✅ **Relacionamento Many-to-Many** com tabela de junção (Inscrições)
3. ✅ **Persistência de dados** (INSERT)
4. ✅ **Atualização de dados** (UPDATE)
5. ✅ **Consultas JPQL** com JOIN FETCH
6. ✅ **Remoção de dados** (DELETE)
7. ✅ **Cascade operations**
8. ✅ **Lazy/Eager loading**

## Modelo de Dados

### Evento
- `id` (PK)
- `nome`
- `data`
- `local`

### Participante
- `id` (PK)
- `nome`
- `email` (unique)

### Inscricao (Tabela de Relacionamento)
- `id` (PK)
- `evento_id` (FK → Evento)
- `participante_id` (FK → Participante)
- `dataInscricao`
- `confirmada` (boolean)

## 📋 Output Esperado

Ao executar `Main.java`, o sistema irá:

1. ✅ Criar 3 eventos
2. ✅ Criar 4 participantes
3. ✅ Realizar 8 inscrições
4. ✅ Confirmar 5 inscrições
5. 📊 Listar eventos com suas inscrições
6. 📊 Listar participantes com seus eventos
7. 📊 Listar inscrições confirmadas
8. ❌ Remover uma inscrição

## Configurações Hibernate

As configurações estão em `src/main/resources/META-INF/persistence.xml`:

- `hibernate.hbm2ddl.auto=update`: Cria/atualiza tabelas automaticamente
- `hibernate.show_sql=true`: Mostra SQL no console
- `hibernate.format_sql=true`: Formata SQL para melhor legibilidade

## 🎯 Por que H2?

- **Zero configuração**: Não precisa instalar nenhum banco de dados
- **Portável**: Funciona em qualquer máquina com Java
- **Rápido**: Ideal para testes e demonstrações
- **Compatível**: Sintaxe SQL padrão, fácil migrar para outros bancos
- **Perfeito para avaliação**: O avaliador só precisa rodar `mvn exec:java`

## Autor

Desenvolvido como projeto de estudo de JPA/Hibernate
