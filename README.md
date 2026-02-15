# 🚀 3035TEACH - Portfólio Fullstack Developer

![Banner/Capa do Projeto](https://github.com/user-attachments/assets/c7cdbbb5-97db-4b72-b2ff-f930685331c8)

<div align="center">

[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)
![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0+-6DB33F?style=flat&logo=spring-boot&logoColor=white)
![React](https://img.shields.io/badge/React-18+-61DAFB?style=flat&logo=react&logoColor=black)
![TypeScript](https://img.shields.io/badge/TypeScript-5.0+-3178C6?style=flat&logo=typescript&logoColor=white)

</div>

## 📖 Sobre o Projeto

Este repositório documenta minha jornada e evolução técnica durante o programa de formação **Fullstack Developer** da **3035TECH**. Aqui estão reunidos **7 módulos** de aprendizado intensivo, totalizando mais de **600 horas** de prática, indo desde os fundamentos da web até a construção de arquiteturas complexas de microsserviços.

O foco principal é o desenvolvimento de soluções robustas, escaláveis e seguras utilizando a stack **Java (Spring)** no Backend e **React (TypeScript)** no Frontend, sempre seguindo boas práticas de engenharia de software (SOLID, Clean Code).

---

## ✨ Destaques Técnicos

O diferencial deste portfólio está na aplicação prática de conceitos avançados:

- **🔐 Segurança Avançada**: Implementação completa de autenticação e autorização via **JWT (Stateless)** com Spring Security.
- **🏗️ Arquitetura Limpa**: Backend estruturado em **Camadas (Layered Architecture)**, respeitando princípios SOLID para desacoplamento e testabilidade.
- **⚡ Frontend Moderno**: SPA reativa com **React Hooks**, **Context API** e **TypeScript** para type-safety.
- **🗄️ Persistência Eficiente**: Uso de **Spring Data JPA** e **Hibernate** para modelagem complexa de dados e otimização de queries, com suporte a migrações de banco.
- **🛠️ Design Patterns**: Aplicação real de padrões como **Strategy**, **Factory** e **Dependency Injection**.

---

## 🛠️ Tech Stack

### Backend
- **Core**: Java 17+, Spring Boot 3+
- **Data**: Spring Data JPA, PostgreSQL/MySQL, H2 Database (Testes)
- **Security**: Spring Security, JWT (JJWT)
- **Tools**: Maven/Gradle, Swagger (OpenAPI), Docker

### Frontend
- **Core**: React.js 18, TypeScript
- **Styling**: CSS Modules / Styled Components / Tailwind (se aplicável)
- **State**: Context API / Redux (se aplicável)
- **Build**: Vite / CRA

---

---

## 🗺️ Jornada de Aprendizado

```mermaid
---
config:
  theme: redux-dark-color
---
mindmap
  root((🚀 3035TEACH<br/>Fullstack))
    Frontend 🎨
      M01: Fundamentos Web
        HTML5, CSS3, JS
      M02: React & TS
        Hooks, Context, Vite
    Backend Java ☕
      M03: Lógica
        Sintaxe, Coleções
      M04: POO
        Classes, SOLID
    Backend Avançado ⚙️
      M05: Persistência
        SQL, JPA, Hibernate
      M06: Spring Boot
        API REST, Security
    Consolidação 🏆
      M07: Projeto Final
        Arquitetura Fullstack
```

### 📅 Cronograma

```mermaid
gantt
    title Cronograma de Formação Fullstack (6 Meses)
    dateFormat  YYYY-MM-DD
    axisFormat  %W
    
    section Frontend
    M01 Fundamentos Web       :done,    m1, 2023-01-01, 3w
    M02 React & TypeScript    :active,  m2, after m1, 4w
    
    section Backend Java
    M03 Java Core             :         m3, after m2, 4w
    M04 POO Avançada          :         m4, after m3, 4w
    M05 Persistência & BD     :         m5, after m4, 4w
    M06 Spring Boot           :         m6, after m5, 4w
    
    section Integração
    M07 Projeto Final         :         m7, after m6, 3w
```


## 📂 Estrutura dos Módulos

| Status | Módulo | Foco de Aprendizado | Projeto Prático |
| :---: | :--- | :--- | :--- |
| ⏳ | [**M07 - Desafio Final**](./M07-Desafio-Final) | **Arquitetura & Integração Final** | *Microserviço de Task Management Fullstack* |
| ⏳ | [**M06 - Spring Boot**](./M06-Backend-IV-Spring-Boot) | **Spring Boot & Security** | API RESTful com Autenticação JWT |
| ⏳ | [**M05 - Banco de Dados**](./M05-Backend-III-Java-BD) | **Persistência (JPA/Hibernate)** | DAO Genérico e Modelagem de Dados |
| 🟡 | [**M04 - POO Java**](./04-java-avancado) | **POO Avançada (Java)** | Sistema com Injeção de Dependência Manual |
| 🟢 | [**M03 - Lógica Java**](./03-backend-java) | **Lógica & Algoritmos** | Estruturas de Dados em Java |
| 🟢 | [**M02 - React**](./02-frontend-react) | **React & TypeScript** | Dashboard Interativo com Consumo de API |
| 🟢 | [**M01 - Web Basics**](./01-fundamentos-web) | **Fundamentos Web** | Landing Pages Responsivas |


---

## 📚 Documentação & Wiki

A documentação completa do projeto, incluindo guias de estudo, padrões de código e detalhes arquiteturais, está disponível na pasta [`/wiki`](./wiki).

Destaques da Wiki:
- [🏁 Guia de Início Rápido](./wiki/Quick-Start-Guide.md)
- [🗺️ Roadmap de Estudos](./wiki/Study-Roadmap.md)
- [🏗️ Arquitetura do Projeto](./wiki/Project-Architecture.md)
- [📏 Padrões de Código](./wiki/Coding-Standards.md)
- [❓ FAQ & Troubleshooting](./wiki/FAQ.md)

Acesse a [Home da Wiki](./wiki/Home.md) para navegar por todos os tópicos.

---

## 🚀 Quick Start

Para rodar os projetos localmente, siga os passos abaixo:

### Pré-requisitos
- Java 17+
- Node.js 18+
- Maven (Opcional, wrapper incluído)

### 1. Backend (Ex: M06/M07)
```bash
cd M06-Backend-IV-Spring-Boot
./mvnw spring-boot:run
# O servidor iniciará em http://localhost:8080
```

### 2. Frontend (Ex: M02)
```bash
cd M02-Frontend-II-React
npm install
npm run dev
# A aplicação estará disponível em http://localhost:5173 (ou 3000)
```

---

## 🤝 Contribuição & Contato

Sugestões e feedbacks são sempre bem-vindos!

- **Email**: [charllesgst@gmail.com](mailto:charllesgst@gmail.com)
- **LinkedIn**: [linkedin.com/in/charlles-augusto](https://linkedin.com/in/charlles-augusto)
- **Portfólio**: [charlles.dev](https://charlles.dev)

---
<div align="center">
  <sub>Desenvolvido com dedicação por <a href="https://github.com/charlles-dev">charlles-dev</a> 🚀</sub>
</div>
