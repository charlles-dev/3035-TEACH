# 📚 Atividades e Exercícios - 3035TEACH

![Image](https://github.com/user-attachments/assets/c7cdbbb5-97db-4b72-b2ff-f930685331c8)

[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)

![Status: Em Andamento](https://img.shields.io/badge/Status-Em%20Andamento-yellow.svg)

![Total Módulos](https://img.shields.io/badge/M%C3%B3dulos-7%20no%20Total-blue)

Este repositório é o meu portfólio de código, contendo todos os
exercícios, desafios e projetos práticos desenvolvidos durante o
programa de formação **Fullstack Developer** oferecido pela
**3035TECH**. O objetivo é documentar a progressão do aprendizado nos 7
módulos, com forte ênfase em **Java, Spring Boot e React**.

## 🏆 Foco e Resultados da Formação {#foco-e-resultados-da-formação}

Este curso me capacitou a atuar como um desenvolvedor Fullstack
proficiente, com domínio na criação de sistemas robustos e seguros:

- **Java e Spring Boot:** Experiência na criação de APIs RESTful

  > escaláveis, utilizando **Programação Orientada a Objetos** e o
  > ecossistema Spring (Security, Data JPA).

- **Segurança (JWT):** Capacidade de implementar fluxos completos de

  > **Autenticação e Autorização** baseados em Tokens.

- **Desenvolvimento Front-end:** Domínio do **React** e **TypeScript**

  > para construir interfaces dinâmicas, tipadas e de alta performance.

- **Boas Práticas:** Aplicação consistente dos princípios **SOLID** e
  > padrões de design no desenvolvimento Backend.

## 📋 Índice

- [🎯 Visão Geral](#visao-geral)
- [🏗️ Arquitetura (M07)](#arquitetura-m07)
- [🛠️ Tecnologias Utilizadas](#tecnologias-utilizadas)
- [⚙️ Ferramentas e Build](#ferramentas-e-build)
- [📝 Padrões de Código e Metodologia](#padroes-de-codigo-e-metodologia)
- [⚙️ Pré-requisitos](#pre-requisitos)
- [🚀 Instalação e Configuração](#instalacao-e-configuracao)
- [📁 Estrutura do Projeto](#estrutura-do-projeto)
- [🔧 Módulos de Atividades](#modulos-de-atividades)
- [🏃 Como Executar os Scripts](#como-executar-os-scripts)
- [📈 Roadmap e Próximos Passos](#roadmap-e-proximos-passos)
- [🤝 Contribuição](#contribuicao)
- [📄 Licença e Suporte](#licenca-e-suporte)

## 🎯 Visão Geral {#visao-geral}

Este projeto é um \"diário de bordo\" do meu desenvolvimento como
Fullstack, demonstrando a evolução desde o HTML/CSS básico até a criação
de APIs complexas com Java e Spring Boot.

| **Detalhe**                 | **Informação**                                   |
| --------------------------- | ------------------------------------------------ |
| **Programa:**               | **3035TEACH - Fullstack Developer**              |
| **Duração:**                | 6 Meses                                          |
| **Carga Horária Estimada:** | **600 horas**                                    |
| **Foco:**                   | Prática intensiva em Desenvolvimento de Software |
| **Status:**                 | 🔄 Em Andamento                                  |

## 🏗️ Arquitetura (M07 - Desafio Final) {#arquitetura-m07}

O **Desafio Final** segue um modelo de arquitetura em camadas (Layered
Architecture), garantindo separação de responsabilidades e
escalabilidade:

1. **Frontend (React/TS):** Single Page Application (SPA) que se

    > comunica com a API via requisições HTTP assíncronas (Axios/Fetch).

2. **Backend (Spring Boot):** Arquitetura RESTful com três camadas

    > principais:

    - **Controller:** Responsável por receber requisições HTTP e

      > roteamento.

    - **Service:** Contém a lógica de negócio principal e regras de

      > transação.

    - **Repository:** Interface de comunicação com o Banco de Dados
      > (usando Spring Data JPA).

3. **Segurança:** Utilização do padrão **Stateless** com JWT para
    > autenticação.

## 🛠️ Tecnologias Utilizadas {#tecnologias-utilizadas}

As atividades foram desenvolvidas utilizando as seguintes tecnologias
principais, com forte foco em Java:

| **Categoria**       | **Tecnologias**                                                           | **Nível de Aplicação**                                                                                        |
| ------------------- | ------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------- |
| **Frontend:**       | **ReactJS** (Hooks, Context/Redux), **TypeScript**, HTML5, CSS3           | **Componentização modular**, validação de formulários (Formik/React Hook Form) e garantia de tipagem robusta. |
| **Backend:**        | **Java** (POO), **Spring Boot**, **Spring Data JPA**, **Spring Security** | Construção de APIs robustas, arquitetura de microserviços e lógica de negócios.                               |
| **Banco de Dados:** | SQL (PostgreSQL/MySQL), JPA/Hibernate                                     | Consultas complexas, CRUD, migrações (Flyway/Liquibase) e mapeamento Objeto-Relacional.                       |

## ⚙️ Ferramentas e Build {#ferramentas-e-build}

| **Ferramenta**          | **Uso Principal**                                                    |
| ----------------------- | -------------------------------------------------------------------- |
| **Git** e **GitHub**    | Versionamento e colaboração de código.                               |
| **Maven** ou **Gradle** | Gerenciamento de dependências e construção (build) de projetos Java. |
| **Linters (ESLint)**    | Padronização e garantia de qualidade de código Frontend.             |
| **Editor de Código**    | **Nome do seu IDE/Editor**                                           |

## 📝 Padrões de Código e Metodologia {#padroes-de-codigo-e-metodologia}

Todas as atividades complexas e projetos seguem os seguintes padrões,
cruciais para um ambiente de desenvolvimento profissional:

- **Design Patterns:** Aplicação prática dos padrões **Factory**,

  > **Strategy**, e arquitetura **Service/Repository** nos módulos
  > Backend (M04-M06).

- **Princípios SOLID:** Aplicação dos 5 princípios (Single

  > Responsibility, Open/Closed, Liskov Substitution, Interface
  > Segregation, Dependency Inversion) no código Backend e na
  > arquitetura de componentes React.

- **Clean Code:** Foco na legibilidade, nomes de variáveis e funções

  > expressivos, e redução de complexidade.

- **Segurança:** Implementação de autenticação baseada em **Tokens JWT**

  > e utilização do **Spring Security** para controle de acesso
  > (Role-Based Access Control - RBAC).

- **Versionamento:** Utilização do padrão **Conventional Commits**

  > (feat:, fix:, refactor:) para um histórico Git limpo e rastreável.

- **Organização e Gestão:** Demonstração de habilidades de
  > **planejamento** e **gestão de tempo** para concluir as 600 horas do
  > curso.

## ⚙️ Pré-requisitos {#pre-requisitos}

Para executar todas as atividades (Front e Back) localmente:

- **Node.js (versão LTS):** Necessário para rodar atividades de

  > JavaScript/React.

- **JDK (Java Development Kit) 17+:** Necessário para compilar e rodar

  > os módulos Backend.

- **Maven ou Gradle:** Para gerenciamento de projetos Java.

- **Editor de Código:** VS Code (recomendado) ou IntelliJ IDEA.

- **Navegador Web:** Chrome, Firefox ou similar.

## 🚀 Instalação e Configuração {#instalacao-e-configuracao}

### 1. Clone o Repositório {#clone-o-repositório}

git clone
\[<https://github.com/seu-usuario/Atividades-3035TEACH.git\>](<https://github.com/seu-usuario/Atividades-3035TEACH.git>)  
cd Atividades-3035TEACH

### 2. Configuração de Ambientes (Se necessário) {#configuração-de-ambientes-se-necessário}

Alguns módulos (como React ou Backend) podem requerer a instalação de
dependências:

**Para Módulos Backend (Java/Spring Boot):**

\# Navegue até a pasta do módulo Spring Boot e restaure/compile  
cd M06-Backend-IV-Spring-Boot/  
\# Usando Maven:  
mvn clean install

**Para Módulos Frontend (React/TypeScript):**

\# Navegue até a pasta do projeto React e instale os pacotes  
cd M02-Frontend-II-React/  
npm install \# ou yarn install

## 📁 Estrutura do Projeto {#estrutura-do-projeto}

A organização segue os 7 módulos do programa de formação:

Atividades-3035TEACH/  
├── M01-Frontend-I-HTML-CSS-JS/ \# Frontend I  
├── M02-Frontend-II-TS-React/ \# Frontend II  
├── M03-Backend-I-Java-Logica/ \# Backend I  
├── M04-Backend-II-Java-OO/ \# Backend II  
├── M05-Backend-III-Java-BD/ \# Backend III  
├── M06-Backend-IV-Spring-Boot/ \# Backend IV  
├── M07-Desafio-Final/ \# Desafio Final  
├── README.md  
├── LICENSE  
└── .gitignore

## 🔧 Módulos de Atividades {#modulos-de-atividades}

| **Módulo**                 | **Foco Principal**                                        | **Atividade Destaque**                                                                     | **Link Direto**                                                                                                                                                                                                    |
| -------------------------- | --------------------------------------------------------- | ------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **🌐 M01 - Frontend I**    | HTML, CSS, e JavaScript básico (DOM).                     | Criação de um layout de dashboard responsivo com manipulação de DOM.                       | [/M01-Frontend-I-HTML-CSS-JS/ex-dashboard-js/](/M01-Frontend-I-HTML-CSS-JS/ex-dashboard-js/) |
| **⚛️ M02 - Frontend II**   | TypeScript, React Hooks e Componentização.                | Implementação de um fluxo de dados complexo (CRUD local) com TypeScript.                   | [/M02-Frontend-II-TS-React/ex-ts-react-crud/](/M02-Frontend-II-TS-React/ex-ts-react-crud/) |
| **☕ M03 - Backend I**     | Lógica de Programação, Estruturas de Dados e Java Básico. | Resolução de desafios de lógica e algoritmos utilizando coleções em Java.                  | [/M03-Backend-I-Java-Logica/ex-algoritmos-colecoes/](/M03-Backend-I-Java-Logica/ex-algoritmos-colecoes/) |
| **💡 M04 - Backend II**    | Orientação a Objetos, Abstração, Herança e Polimorfismo.  | Criação de um sistema de classes que implementa Injeção de Dependência manual.             | [/M04-Backend-II-Java-OO/ex-di-manual/](/M04-Backend-II-Java-OO/ex-di-manual/) |
| **💾 M05 - Backend III**   | Java, JDBC e persistência de dados.                       | Criação de um DAO (Data Access Object) genérico para reutilização em diferentes entidades. | [/M05-Backend-III-Java-BD/ex-dao-generico/](/M05-Backend-III-Java-BD/ex-dao-generico/) |
| **🌱 M06 - Backend IV**    | Spring Boot, APIs RESTful e JPA/Hibernate.                | Desenvolvimento de uma API completa com Autenticação JWT usando Spring Security.           | [/M06-Backend-IV-Spring-Boot/ex-api-auth-jwt/](/M06-Backend-IV-Spring-Boot/ex-api-auth-jwt/) |
| **🏆 M07 - Desafio Final** | Aplicação Prática Integrada.                              | **Microserviço de Gerenciamento de Tarefas (Task Management) Fullstack.**                  | [/M07-Desafio-Final/](/M07-Desafio-Final/) |

## 🏃 Como Executar os Scripts {#como-executar-os-scripts}

### 1. Módulos Frontend (M01 e M02) {#módulos-frontend-m01-e-m02}

- **M01 (Web Estático):** Abra o arquivo .html diretamente no seu

  > navegador.

- **M02 (React/TS):** Navegue até a pasta do projeto (M02) e execute:
  > npm start (ou yarn start).

### 2. Módulos Backend (M03 a M06) {#módulos-backend-m03-a-m06}

- **M03 a M05 (Java Puro/JDBC):** Compile e execute o arquivo .java

  > principal usando sua IDE ou linha de comando (javac \[Arquivo.java\]
  > e java \[Arquivo\]).

- **M06 (Spring Boot):** Navegue até a pasta e execute o comando de run:
  > mvn spring-boot:run ou gradlew bootRun.

## 📈 Roadmap e Próximos Passos {#roadmap-e-proximos-passos}

| **Fase**           | **Foco**                                                                                                           | **Status**      |
| ------------------ | ------------------------------------------------------------------------------------------------------------------ | --------------- |
| **Fase 1 (Atual)** | **Completo:** Implementação de todos os exercícios de cada módulo (M01 ao M07).                                    | 🔄 Em Andamento |
| **Fase 2**         | **Refatoração:** Aplicar os princípios **SOLID** e **Clean Code** nas atividades do Backend (M04 a M06).           | 📋 Planejando    |
| **Fase 3**         | **Projetos Extras:** Adicionar documentação OpenAPI/Swagger para as APIs e implementar Testes Unitários com JUnit. | 📋 Planejando    |

## 🤝 Contribuição {#contribuicao}

Este é um repositório pessoal de aprendizado, mas sinta-se à vontade
para sugerir melhorias em meus scripts através de Pull Requests.

### Como Contribuir

1. **Fork** o repositório.

2. Crie uma **Branch de Feature** (git checkout -b

    > refactor/melhoria-script-x).

3. **Commit** suas alterações.

4. Crie um **Pull Request**, descrevendo o que foi melhorado e o
    > motivo.

## 📄 Licença e Suporte {#licenca-e-suporte}

Este projeto está licenciado sob a licença **MIT**. Veja o arquivo
LICENSE para mais detalhes.

Em caso de dúvidas sobre as atividades do curso ou sobre a implementação
de algum script, entre em contato:

- [**Email**](mailto:charlleshst@gmail.com)
- [**LinkedIn**](https://linkedin.com/in/charlles-augusto)
- [**Portfólio Pessoal**](https://charlles.dev)

### Desenvolvido com ❤️ por charlles
