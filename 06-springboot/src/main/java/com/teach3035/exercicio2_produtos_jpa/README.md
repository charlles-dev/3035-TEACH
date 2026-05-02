# 📦 Exercício 2 - API de Produtos com JPA

## Objetivo
Evoluir a API de produtos do Exercício 1 para utilizar JPA com banco de dados real, adicionar Swagger completo, tratamento robusto de exceções, soft delete, cache e migrações com Flyway.

## 🎯 Novas Funcionalidades
- JPA com PostgreSQL
- Flyway migrações
- Soft delete
- Auditing automático (CreatedDate, LastModifiedDate)
- Optimistic Locking (@Version)
- Cache com Caffeine
- HATEOAS
- ETag e Cache HTTP
- Specifications (filtros dinâmicos)
- Swagger completo

## 📡 Endpoints

| Método | Endpoint | Descrição |
|-------|---------|-----------|
| GET | `/produtos` | Lista com paginação e filtros |
| GET | `/produtos/{id}` | Detalhes com HATEOAS |
| POST | `/produtos` | Cria produto |
| PATCH | `/produtos/{id}` | Atualiza parcialmente |
| DELETE | `/produtos/{id}` | Soft delete |

## 📋 Query Parameters (GET /produtos)

| Parâmetro | Descrição |
|----------|-----------|
| `page` | Página (0 based) |
| `size` | Tamanho por página |
| `sort` | Ordenação (nome,preco) |
| `nome` | Filtro por nome |
| `dataInicio` | Data início criação |
| `dataFim` | Data fim criação |
| `precoMin` | Preço mínimo |
| `precoMax` | Preço máximo |

## 📋 Request Examples

```bash
# Criar produto
curl -X POST http://localhost:8082/produtos \
  -H "Content-Type: application/json" \
  -d '{"nome": "Notebook", "preco": 2999.99}'

# Listar com filtros
curl "http://localhost:8082/produtos?page=0&size=10&sort=nome"

# Listar com filtros dinâmicos
curl "http://localhost:8082/produtos?precoMin=100&precoMax=5000"

# Buscar com ETag (cache)
curl -H "If-None-Match: \"abc123\"" http://localhost:8082/produtos
```

## 🏗️ Arquitetura

```
exercicio2_produtos_jpa/
├── controller/
├── service/
├── repository/      # Interface JPA
├── persistence/    # Entity JPA
├── dto/
├── model/
├── exception/
├── filter/
├── validation/
├── specification/ # Filtros dinâmicos
└── config/
```

## 🗄️ Banco de Dados

- **Flyway Migrations** em `src/main/resources/db/migration`
- V1__create_produto.sql - cria tabela

## ⚙️ Configuração

- **Porta**: 8082
- **Banco**: PostgreSQL (docker-compose)
- **Swagger**: http://localhost:8082/swagger-ui.html

## 🚀 Executar (Docker Compose)

```powershell
docker-compose up -d exercicio2
```

Acesse: http://localhost:8082