# 📦 Exercício 1 - API de Produtos (In-Memory)

## Objetivo
Criar uma API REST em Spring Boot para gerenciar um catálogo de produtos sem banco de dados. Os dados são armazenados em memória.

## 🎯 Funcionalidades
- CRUD completo de produtos
- Arquitetura MVC com inversão de dependência
- Validação com Bean Validation
- Envelope de resposta padronizado
- Tratamento global de exceções
- Headers de segurança
- Correlation ID para logs

## 📡 Endpoints

| Método | Endpoint | Descrição |
|-------|---------|-----------|
| GET | `/produtos` | Lista todos os produtos |
| GET | `/produtos/{id}` | Busca produto por ID |
| POST | `/produtos` | Cria novo produto |
| PATCH | `/produtos/{id}` | Atualiza produto (parcial) |
| DELETE | `/produtos/{id}` | Remove produto |

## 📋 Request Examples

```bash
# Criar produto
curl -X POST http://localhost:8081/produtos \
  -H "Content-Type: application/json" \
  -d '{"nome": "Notebook", "preco": 2999.99}'

# Listar produtos
curl http://localhost:8081/produtos

# Atualizar produto
curl -X PATCH http://localhost:8081/produtos/1 \
  -H "Content-Type: application/json" \
  -d '{"nome": "Notebook Pro"}'

# Deletar produto
curl -X DELETE http://localhost:8081/produtos/1
```

## 🏗️ Arquitetura

```
exercicio1_produtos_inmemory/
├── controller/      # Recebe requisições HTTP
├── service/         # Lógica de negócio
├── repository/     # Interface + Implementação In-Memory
├── dto/            # Request/Response/Patch
├── model/          # Entidade
├── exception/     # Exceções customizadas
├── filter/        # Filters (Security, Correlation ID)
├── validation/    # Validações customizadas
└── config/        # Configurações
```

## ⚙️ Configuração

- **Porta**: 8081
- **Banco**: H2 (memória)
- **Swagger**: http://localhost:8081/swagger-ui.html

## 🚀 Executar (Docker Compose)

```powershell
docker-compose up -d exercicio1
```

Acesse: http://localhost:8081