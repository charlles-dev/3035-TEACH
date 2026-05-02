# 🧩 Módulo 6 - Spring Boot

## Objetivo
Dominar Spring Boot para construir APIs REST com segurança, persistência e boas práticas de arquitetura.

## 📚 Stack
- Spring Boot 3.4
- Spring Data JPA
- Spring Security + JWT
- PostgreSQL / H2

## 📋 Estrutura do Projeto

| # | Serviço | Porta | Descrição |
|---|---------|------|-----------|
| 1 | **Frontend Unificado** | 3000 | SPA React com 3 abas consumindo todas as APIs |
| 2 | **Exercício 1** | 8081 | API de catálogo de produtos (In-Memory) |
| 3 | **Exercício 2** | 8082 | API de catálogo de produtos (PostgreSQL) |
| 4 | **Desafio** | 8083 | API de tarefas com autenticação JWT |

## 🎯 Frontend Unificado

O módulo 6 agora conta com um **Frontend React unificado** que consome todas as três APIs em uma única aplicação SPA:

- **Exercício 1**: Aba "Exercício 01" - Gerenciamento de produtos em memória
- **Exercício 2**: Aba "Exercício 02" - Gerenciamento de produtos com PostgreSQL
- **Desafio**: Aba "Desafio ToDo" - Kanban de tarefas com autenticação JWT

### Features do Frontend:
- Layout Kanban com drag & drop entre colunas
- Autenticação JWT integrada
- Design responsivo moderno
- CRUD completo para todas as funcionalidades

## 🚀 Quick Start (Docker Compose)

```powershell
# Iniciar todos os serviços (Backend + Frontend)
cd 06-springboot
docker-compose up -d

# Acessar o Frontend
# http://localhost:3000

# Parar
docker-compose down
```

### Servicios disponíveis:

| Serviço | URL |
|---------|-----|
| Frontend | http://localhost:3000 |
| Exercício 1 (API) | http://localhost:8081 |
| Exercício 2 (API) | http://localhost:8082 |
| Desafio (API) | http://localhost:8083 |
| PostgreSQL | localhost:5432 |

## 📁 Estrutura

```
06-springboot/
├── src/main/java/com/teach3035/
│   ├── exercicio1_produtos_inmemory/
│   ├── exercicio2_produtos_jpa/
│   └── desafio_todo_jwt/
├── frontend/                    # Frontend React unificado
│   ├── src/
│   │   └── components/
│   │       ├── TodoApp.tsx     # Kanban JWT
│   │       └── ProductManager.tsx
│   ├── package.json
│   └── vite.config.ts
├── docker-compose.yml
└── pom.xml
```

## 📖 Documentação da API (Swagger)

| Serviço | Swagger |
|---------|---------|
| Exercício 1 | http://localhost:8081/swagger-ui.html |
| Exercício 2 | http://localhost:8082/swagger-ui.html |
| Desafio | http://localhost:8083/swagger-ui.html |

## 🛠️ Desenvolvimento Local (sem Docker)

### Backend:
```powershell
cd 06-springboot
./mvnw spring-boot:run -Dspring-boot.run.profiles=ex1   # Porta 8081
./mvnw spring-boot:run -Dspring-boot.run.profiles=ex2   # Porta 8082
./mvnw spring-boot:run -Dspring-boot.run.profiles=desafio # Porta 8083
```

### Frontend:
```powershell
cd 06-springboot/frontend
npm install
npm run dev
# Acesse http://localhost:3000
```

## ⚠️ Requisitos
- Java 17+
- Docker / Docker Compose
- Node.js 18+ (para desenvolvimento local do frontend)