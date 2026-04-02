================================================================================
  SISTEMA DE GERENCIAMENTO DE EVENTOS - JPA/HIBERNATE
================================================================================

DESCRIÇÃO:
----------
Sistema de gerenciamento de eventos com inscrições de participantes.
Demonstra o uso de JPA/Hibernate com relacionamentos Many-to-Many.

TECNOLOGIAS:
------------
- Java 17+
- JPA (Jakarta Persistence API) 3.1
- Hibernate 6.4.4
- PostgreSQL OU MySQL
- Maven

================================================================================
  COMO EXECUTAR - PASSO A PASSO
================================================================================

PASSO 1: INSTALAR PRÉ-REQUISITOS
---------------------------------
- Java JDK 17 ou superior
- Maven 3.6+
- PostgreSQL OU MySQL (escolha um)

PASSO 2: CONFIGURAR O BANCO DE DADOS
-------------------------------------

>>> OPÇÃO A: PostgreSQL (RECOMENDADO)

1. Instalar PostgreSQL (https://www.postgresql.org/download/)

2. Criar o banco de dados:
   - Abrir terminal/prompt de comando
   - Executar: psql -U postgres
   - Digitar: CREATE DATABASE eventos_db;
   - Digitar: \q (para sair)

   OU usar o script SQL fornecido:
   - psql -U postgres -f src/main/java/JPA/postgresql_schema.sql

3. Configurar credenciais:
   - Abrir arquivo: src/main/resources/META-INF/persistence.xml
   - Verificar linhas 24-25:
     <property name="jakarta.persistence.jdbc.user" value="postgres"/>
     <property name="jakarta.persistence.jdbc.password" value="postgres"/>
   - Alterar usuário e senha se necessário


>>> OPÇÃO B: MySQL

1. Instalar MySQL (https://dev.mysql.com/downloads/)

2. Criar o banco de dados:
   - Abrir terminal/prompt de comando
   - Executar: mysql -u root -p
   - Digitar senha
   - Digitar: CREATE DATABASE eventos_db;
   - Digitar: exit

   OU usar o script SQL fornecido:
   - mysql -u root -p < src/main/java/JPA/mysql_schema.sql

3. Configurar o projeto para MySQL:
   - Abrir arquivo: src/main/resources/META-INF/persistence.xml
   - COMENTAR as linhas 22-26 (PostgreSQL)
   - DESCOMENTAR as linhas 29-34 (MySQL)
   - Verificar usuário/senha nas linhas 32-33


PASSO 3: COMPILAR E EXECUTAR
-----------------------------

>>> Via Maven (Terminal/Prompt):

cd 05-java-db
mvn clean compile
mvn exec:java -Dexec.mainClass="JPA.Main"


>>> Via IDE (IntelliJ IDEA / Eclipse):

1. Importar o projeto 05-java-db como projeto Maven
2. Aguardar o Maven baixar todas as dependências
3. Abrir a classe: src/main/java/JPA/Main.java
4. Clicar com botão direito → Run 'Main.main()'


================================================================================
  ARQUIVOS IMPORTANTES
================================================================================

src/main/java/JPA/
├── postgresql_schema.sql    # Script SQL para PostgreSQL
├── mysql_schema.sql          # Script SQL para MySQL
├── README.txt                # Este arquivo
├── Evento.java               # Entidade: Eventos
├── Participante.java         # Entidade: Participantes
├── Inscricao.java            # Entidade: Inscrições (relacionamento)
└── Main.java                 # Classe principal (demonstração)


================================================================================
  MODELO DE DADOS
================================================================================

TABELA: eventos
---------------
- id (BIGINT, PK, AUTO_INCREMENT)
- nome (VARCHAR 100)
- data (DATE)
- local (VARCHAR 200)

TABELA: participantes
---------------------
- id (BIGINT, PK, AUTO_INCREMENT)
- nome (VARCHAR 100)
- email (VARCHAR 100, UNIQUE)

TABELA: inscricoes
------------------
- id (BIGINT, PK, AUTO_INCREMENT)
- evento_id (BIGINT, FK → eventos.id)
- participante_id (BIGINT, FK → participantes.id)
- data_inscricao (TIMESTAMP)
- confirmada (BOOLEAN)


================================================================================
  O QUE O PROGRAMA FAZ
================================================================================

Ao executar Main.java, o sistema demonstra:

1. ✓ Criação de 3 eventos
2. ✓ Criação de 4 participantes
3. ✓ Realização de 8 inscrições
4. ✓ Confirmação de 5 inscrições
5. ✓ Listagem de eventos com suas inscrições
6. ✓ Listagem de participantes com seus eventos
7. ✓ Listagem de inscrições confirmadas
8. ✓ Remoção de uma inscrição

OPERAÇÕES JPA DEMONSTRADAS:
- Persist (INSERT)
- Merge (UPDATE)
- Remove (DELETE)
- Find (SELECT por ID)
- JPQL Queries com JOIN FETCH
- Relacionamentos @OneToMany e @ManyToOne
- Cascade operations
- Lazy/Eager loading


================================================================================
  SOLUÇÃO DE PROBLEMAS
================================================================================

ERRO: "Communications link failure" ou "Connection refused"
------------------------------------------------------------
Solução: O banco de dados não está rodando.
- PostgreSQL: Iniciar serviço PostgreSQL
- MySQL: Iniciar serviço MySQL


ERRO: "Access denied for user"
-------------------------------
Solução: Usuário ou senha incorretos.
- Verificar credenciais em: src/main/resources/META-INF/persistence.xml
- Linhas 24-25 (PostgreSQL) ou 32-33 (MySQL)


ERRO: "Unknown database 'eventos_db'"
--------------------------------------
Solução: Banco de dados não foi criado.
- Executar o script SQL apropriado:
  - PostgreSQL: src/main/java/JPA/postgresql_schema.sql
  - MySQL: src/main/java/JPA/mysql_schema.sql


ERRO: "No Persistence provider for EntityManager"
--------------------------------------------------
Solução: Arquivo persistence.xml não foi encontrado.
- Verificar se existe: src/main/resources/META-INF/persistence.xml
- Recompilar: mvn clean compile


ERRO: "Package jakarta.persistence does not exist"
---------------------------------------------------
Solução: Dependências Maven não foram baixadas.
- Executar: mvn clean install
- Ou recarregar projeto Maven na IDE


================================================================================
  CONFIGURAÇÕES IMPORTANTES
================================================================================

HIBERNATE SETTINGS (persistence.xml):
--------------------------------------

hibernate.hbm2ddl.auto = update
  → Cria/atualiza tabelas automaticamente
  → ATENÇÃO: Em produção, use "validate" ou "none"

hibernate.show_sql = true
  → Mostra comandos SQL no console
  → Útil para aprendizado e debug

hibernate.format_sql = true
  → Formata SQL para melhor legibilidade


NOTA: Se preferir, você pode NÃO executar os scripts SQL!
O Hibernate irá criar as tabelas automaticamente graças à configuração
"hibernate.hbm2ddl.auto=update". Basta criar o banco vazio:
- PostgreSQL: CREATE DATABASE eventos_db;
- MySQL: CREATE DATABASE eventos_db;


================================================================================
  CONTATO
================================================================================

Projeto desenvolvido como desafio de curso
GitHub: https://github.com/charlles-dev/3035-TEACH

================================================================================
