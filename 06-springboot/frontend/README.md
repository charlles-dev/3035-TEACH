# 🎨 Frontend Unificado - Módulo 6

Frontend React unificado que consome todas as três APIs do módulo 6 em uma única aplicação SPA.

## 📋 Funcionalidades

### Aba Exercício 01
- Gerenciamento de produtos em memória (CRUD)
- Lista todos os produtos da API de exercício 1

### Aba Exercício 02
- Gerenciamento de produtos com PostgreSQL (CRUD)
- Persistência real dos dados

### Aba Desafio ToDo
- Kanban de tarefas com drag & drop
- Autenticação JWT (login/register)
- Três colunas: Pendente, Em Progresso, Concluído

## 🚀 Quick Start

### Desenvolvimento
```bash
cd frontend
npm install
npm run dev
# Acesse http://localhost:3000
```

### Docker
```bash
# Na pasta raiz do módulo
cd ..
docker-compose up -d
# Acesse http://localhost:3000
```

## 📁 Estrutura

```
frontend/
├── src/
│   ├── components/
│   │   ├── TodoApp.tsx        # Kanban JWT com drag & drop
│   │   ├── ProductManager.tsx # CRUD de produtos
│   │   └── ui/
│   │       └── ToastContext.tsx
│   ├── App.tsx                # Navegação principal
│   ├── types.ts               # TypeScript types
│   └── index.css              # Estilos (Tailwind)
├── package.json
├── vite.config.ts
└── Dockerfile
```

## 🔧 Tech Stack
- React 19
- TypeScript
- Vite
- TailwindCSS 4
- @dnd-kit (drag & drop)
- Lucide React (ícones)

## 📝 API Endpoints

O frontend consome:
- **Exercício 1**: http://localhost:8081/produtos
- **Exercício 2**: http://localhost:8082/produtos
- **Desafio**: http://localhost:8083/api/v1/auth e /api/v1/tasks

## ⚠️ Nota

O frontend faz chamadas HTTP para `localhost:8081`, `8082`, `8083` - certifique-se que as APIs estão rodando. O CORS deve estar habilitado nos controllers Spring Boot.