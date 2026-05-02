# 🚀 Desafio Final - API ToDo com JWT

## Objetivo
Criar uma API completa de gerenciamento de tarefas (ToDo) com autenticação JWT, estruturada com boas práticas de arquitetura hexagonal Spring Boot de nível profissional.

## 🎯 Funcionalidades
- Autenticação JWT (Access + Refresh Token)
- Arquitetura Hexagonal (Ports & Adapters)
- Autorização com Roles (USER, ADMIN)
- Account Lockout (5 tentativas)
- Rate Limiting
- Soft delete
- Optimistic Locking
- HATEOAS
- ETag / Cache HTTP
- Correlation ID
- Eventos de domínio

## 📡 Endpoints

### Auth (Públicos)
| Método | Endpoint | Descrição |
|-------|---------|-----------|
| POST | `/api/v1/auth/register` | Cadastrar usuário |
| POST | `/api/v1/auth/login` | Fazer login |
| POST | `/api/v1/auth/refresh` | Refresh token |
| POST | `/api/v1/auth/logout` | Logout |

### Tasks (Protegidos)
| Método | Endpoint | Descrição |
|-------|---------|-----------|
| GET | `/api/v1/tasks` | Lista tarefas |
| GET | `/api/v1/tasks/{id}` | Detalhes tarefa |
| POST | `/api/v1/tasks` | Criar tarefa |
| PATCH | `/api/v1/tasks/{id}` | Atualizar tarefa |
| DELETE | `/api/v1/tasks/{id}` | Deletar tarefa |

### Admin (ROLE_ADMIN)
| Método | Endpoint | Descrição |
|-------|---------|-----------|
| GET | `/api/v1/admin/users` | Listar usuários |
| PATCH | `/api/v1/admin/users/{id}/unlock` | Desbloquear usuário |

## 🔐 Autenticação

```bash
# Registrar
curl -X POST http://localhost:8083/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "joao", "password": "Senha@123"}'

# Login
curl -X POST http://localhost:8083/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "joao", "password": "Senha@123"}'

# resposta:
# {
#   "success": true,
#   "data": {
#     "accessToken": "eyJ...",
#     "refreshToken": "eyJ...",
#     "expiresIn": 900
#   }
# }
```

## 📋 Request Examples

```bash
# Criar tarefa (com token)
curl -X POST http://localhost:8083/api/v1/tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {ACCESS_TOKEN}" \
  -d '{"titulo": "Estudar Spring", "descricao": "Revisar JWT"}'

# Listar tarefas
curl http://localhost:8083/api/v1/tasks \
  -H "Authorization: Bearer {ACCESS_TOKEN}"

# Atualizar status
curl -X PATCH http://localhost:8083/api/v1/tasks/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {ACCESS_TOKEN}" \
  -d '{"status": "EM_ANDAMENTO"}'
```

## 🏗️ Arquitetura Hexagonal

```
desafio_todo_jwt/
├── domain/           # Regras de negócio (sem Spring)
│   ├── model/       # User, Task, TaskStatus
│   ├── repository/ # Interfaces
│   └── exception/  # Exceções de domínio
├── application/    # Use Cases
│   ├── usecase/   # AuthUseCase, TaskUseCase
│   └── dto/
└── infrastructure # adapters
    ├── persistence/   # JPA
    ├── security/     # JWT, Filter
    └── web/          # Controllers
```

## 🔒 Segurança

- **Password BCrypt** - senhas hasheadas
- **JWT** - tokens com expiração
- **Account Lockout** - 5 tentativas = 30min bloqueado
- **Rate Limiting** - 5 req/min por IP

## ⚙️ Configuração

- **Porta**: 8083
- **Banco**: PostgreSQL (docker-compose)
- **JWT Secret**: configurável via variável de ambiente
- **Access Token**: 15 min
- **Refresh Token**: 7 dias

## 🚀 Executar (Docker Compose)

```powershell
docker-compose up -d desafio
```

Acesse: http://localhost:8083

## 📋 Regras de Negócio

1. **Transições de Status**:
   - PENDENTE → EM_ANDAMENTO ✓
   - EM_ANDAMENTO → CONCLUIDA ✓
   - CONCLUIDA → qualquer estado ✗ (422)

2. **Usuário só vê suas próprias tarefas**

3. **ADMIN pode ver todas as tarefas**