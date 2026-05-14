# 04 - API REST

## Padroes Gerais

Base URL local:

```text
http://localhost:8080/api/v1
```

Base URL demo:

```text
https://teachgram-pro-api.onrender.com/api/v1
```

Formato:

- JSON.
- Datas em ISO 8601.
- IDs em UUID.
- Autenticacao por `Authorization: Bearer <accessToken>`.
- Erros com `application/problem+json` seguindo `ProblemDetail`.

## Erro Padrao

Exemplo:

```json
{
  "type": "https://teachgram.pro/problems/validation-error",
  "title": "Validation error",
  "status": 400,
  "detail": "One or more fields are invalid.",
  "instance": "/api/v1/posts",
  "errors": {
    "title": ["must not be blank"],
    "description": ["size must be between 0 and 200"]
  },
  "traceId": "8e7f2f4e2f5a4a0e"
}
```

Codigos comuns:

- `400 Bad Request`: validacao.
- `401 Unauthorized`: ausente, invalido ou expirado.
- `403 Forbidden`: usuario autenticado sem permissao.
- `404 Not Found`: recurso inexistente ou inacessivel.
- `409 Conflict`: duplicidade ou conflito de estado.
- `422 Unprocessable Entity`: regra de negocio falhou.
- `429 Too Many Requests`: rate limit.
- `500 Internal Server Error`: falha inesperada.
- `503 Service Unavailable`: dependencia externa indisponivel.

## Paginacao

Response padrao:

```json
{
  "items": [],
  "nextCursor": "eyJjcmVhdGVkQXQiOiIyMDI2LTA1LTEzVDE4OjMwOjAwWiIsImlkIjoiLi4uIn0=",
  "hasNext": true
}
```

Query params:

- `limit`: inteiro entre 1 e 50, padrao 20.
- `cursor`: cursor retornado pela pagina anterior.

## DTOs Comuns

### AuthUserResponse

```json
{
  "id": "4acfc4b4-cc48-44e5-94e1-d625f1e8d5a1",
  "username": "maria",
  "email": "maria@example.com",
  "displayName": "Maria da Silva",
  "avatarUrl": "https://example.com/avatar.jpg",
  "role": "USER"
}
```

### PublicProfileResponse

```json
{
  "id": "4acfc4b4-cc48-44e5-94e1-d625f1e8d5a1",
  "username": "maria",
  "displayName": "Maria da Silva",
  "bio": "Desenvolvedora fullstack e cafe dependente.",
  "avatarUrl": "https://example.com/avatar.jpg",
  "stats": {
    "posts": 12,
    "followers": 120,
    "following": 80
  },
  "viewerState": {
    "following": true,
    "ownProfile": false
  }
}
```

### PostResponse

```json
{
  "id": "cd803b8b-1c9b-4af4-9a30-327bc9b5bb4f",
  "author": {
    "id": "4acfc4b4-cc48-44e5-94e1-d625f1e8d5a1",
    "username": "maria",
    "displayName": "Maria da Silva",
    "avatarUrl": "https://example.com/avatar.jpg"
  },
  "title": "Fim de tarde",
  "description": "Testando o TeachGram Pro.",
  "visibility": "PUBLIC",
  "media": [
    {
      "id": "f33356fd-1552-40c4-879e-0817cf5e5e08",
      "type": "IMAGE",
      "url": "https://example.com/photo.jpg",
      "altText": "Ceu ao por do sol"
    }
  ],
  "stats": {
    "likes": 21,
    "comments": 4,
    "saves": 2
  },
  "viewerState": {
    "liked": false,
    "saved": true,
    "owner": false
  },
  "createdAt": "2026-05-13T18:30:00Z",
  "updatedAt": "2026-05-13T18:30:00Z"
}
```

## Auth

### POST /auth/signup

Cria conta.

Request:

```json
{
  "name": "Maria da Silva",
  "username": "maria",
  "email": "maria@example.com",
  "phone": "+5511999999999",
  "password": "Maria@123456",
  "avatarUrl": "https://example.com/avatar.jpg",
  "bio": "Desenvolvedora fullstack."
}
```

Response `201`:

```json
{
  "user": {
    "id": "4acfc4b4-cc48-44e5-94e1-d625f1e8d5a1",
    "username": "maria",
    "email": "maria@example.com",
    "displayName": "Maria da Silva",
    "avatarUrl": "https://example.com/avatar.jpg",
    "role": "USER"
  },
  "tokens": {
    "accessToken": "jwt",
    "refreshToken": "opaque-refresh-token",
    "tokenType": "Bearer",
    "expiresIn": 900
  }
}
```

Erros:

- `400`: campos invalidos.
- `409`: email, username ou telefone ja cadastrado.

### POST /auth/login

Autentica.

Request:

```json
{
  "identifier": "maria@example.com",
  "password": "Maria@123456"
}
```

Response `200`: igual ao signup.

Erros:

- `401`: credenciais invalidas.
- `429`: muitas tentativas.

### POST /auth/refresh

Renova token.

Request:

```json
{
  "refreshToken": "opaque-refresh-token"
}
```

Response `200`:

```json
{
  "accessToken": "new-jwt",
  "refreshToken": "new-refresh-token",
  "tokenType": "Bearer",
  "expiresIn": 900
}
```

### POST /auth/logout

Requer autenticacao.

Request:

```json
{
  "refreshToken": "opaque-refresh-token"
}
```

Response: `204 No Content`.

## Usuario E Perfil

### GET /users/me

Retorna usuario autenticado.

Response `200`:

```json
{
  "id": "4acfc4b4-cc48-44e5-94e1-d625f1e8d5a1",
  "username": "maria",
  "email": "maria@example.com",
  "displayName": "Maria da Silva",
  "phone": "+5511999999999",
  "bio": "Desenvolvedora fullstack.",
  "avatarUrl": "https://example.com/avatar.jpg"
}
```

### PATCH /users/me

Atualiza perfil.

Request:

```json
{
  "displayName": "Maria S.",
  "username": "mariadev",
  "phone": "+5511988887777",
  "bio": "Fullstack Java e React.",
  "avatarUrl": "https://example.com/new-avatar.jpg"
}
```

Response `200`: dados atualizados.

### DELETE /users/me

Exclui conta logicamente.

Response: `204 No Content`.

### GET /users/{username}

Retorna perfil publico.

Response `200`: `PublicProfileResponse`.

### GET /users/search?q=maria&limit=20&cursor=...

Busca perfis.

Response `200`:

```json
{
  "items": [
    {
      "id": "4acfc4b4-cc48-44e5-94e1-d625f1e8d5a1",
      "username": "maria",
      "displayName": "Maria da Silva",
      "avatarUrl": "https://example.com/avatar.jpg",
      "bio": "Desenvolvedora fullstack."
    }
  ],
  "nextCursor": null,
  "hasNext": false
}
```

## Posts

### POST /posts

Cria post.

Request:

```json
{
  "title": "Fim de tarde",
  "description": "Testando o TeachGram Pro.",
  "visibility": "PUBLIC",
  "media": [
    {
      "type": "IMAGE",
      "url": "https://example.com/photo.jpg",
      "altText": "Ceu ao por do sol"
    }
  ]
}
```

Response `201`: `PostResponse`.

### GET /posts/{postId}

Retorna post se o usuario puder visualizar.

Erros:

- `404`: inexistente, deletado ou privado para o viewer.

### PATCH /posts/{postId}

Edita post do dono.

Request:

```json
{
  "title": "Novo titulo",
  "description": "Nova descricao",
  "visibility": "PRIVATE",
  "media": [
    {
      "type": "IMAGE",
      "url": "https://example.com/new-photo.jpg",
      "altText": "Nova foto"
    }
  ]
}
```

Response `200`: `PostResponse`.

### DELETE /posts/{postId}

Soft delete.

Response: `204 No Content`.

### PATCH /posts/{postId}/privacy

Altera privacidade.

Request:

```json
{
  "visibility": "PUBLIC"
}
```

Response `200`: `PostResponse`.

### GET /users/{username}/posts

Lista posts do perfil.

Query:

- `limit`
- `cursor`

Response: pagina de `PostResponse`.

## Feed

### GET /feed

Lista timeline do usuario autenticado.

Query:

- `limit`
- `cursor`

Response `200`:

```json
{
  "items": [
    {
      "id": "cd803b8b-1c9b-4af4-9a30-327bc9b5bb4f",
      "author": {
        "id": "4acfc4b4-cc48-44e5-94e1-d625f1e8d5a1",
        "username": "maria",
        "displayName": "Maria da Silva",
        "avatarUrl": "https://example.com/avatar.jpg"
      },
      "title": "Fim de tarde",
      "description": "Testando o TeachGram Pro.",
      "visibility": "PUBLIC",
      "media": [],
      "stats": {
        "likes": 21,
        "comments": 4,
        "saves": 2
      },
      "viewerState": {
        "liked": false,
        "saved": false,
        "owner": false
      },
      "createdAt": "2026-05-13T18:30:00Z",
      "updatedAt": "2026-05-13T18:30:00Z"
    }
  ],
  "nextCursor": null,
  "hasNext": false
}
```

## Social Graph

### POST /relationships/{userId}/follow

Segue usuario.

Response `201`:

```json
{
  "following": true,
  "followedAt": "2026-05-13T18:30:00Z"
}
```

### DELETE /relationships/{userId}/follow

Deixa de seguir.

Response: `204 No Content`.

### GET /relationships/me/following

Lista quem o usuario segue.

Response: pagina de perfis resumidos.

### GET /relationships/me/followers

Lista seguidores.

Response: pagina de perfis resumidos.

## Engagement

### POST /posts/{postId}/likes

Curte post.

Response `200`:

```json
{
  "liked": true,
  "likeCount": 22
}
```

### DELETE /posts/{postId}/likes

Remove curtida.

Response `200`:

```json
{
  "liked": false,
  "likeCount": 21
}
```

### POST /posts/{postId}/comments

Cria comentario.

Request:

```json
{
  "content": "Muito bom!"
}
```

Response `201`:

```json
{
  "id": "7e357b2f-d0cd-41f2-b3d5-01bd8d0eb715",
  "content": "Muito bom!",
  "author": {
    "id": "4acfc4b4-cc48-44e5-94e1-d625f1e8d5a1",
    "username": "maria",
    "displayName": "Maria da Silva",
    "avatarUrl": "https://example.com/avatar.jpg"
  },
  "createdAt": "2026-05-13T18:30:00Z"
}
```

### GET /posts/{postId}/comments

Lista comentarios.

### POST /posts/{postId}/saves

Salva post.

Response `200`:

```json
{
  "saved": true,
  "saveCount": 3
}
```

### DELETE /posts/{postId}/saves

Remove dos salvos.

Response `200`:

```json
{
  "saved": false,
  "saveCount": 2
}
```

## Notifications

### GET /notifications

Lista notificacoes.

Response:

```json
{
  "items": [
    {
      "id": "c1a9fd2d-54f7-4275-b4d1-6be84c165c23",
      "type": "LIKE",
      "message": "Joao curtiu seu post.",
      "read": false,
      "createdAt": "2026-05-13T18:30:00Z"
    }
  ],
  "nextCursor": null,
  "hasNext": false
}
```

### PATCH /notifications/{notificationId}/read

Marca como lida.

Response `204 No Content`.

## Search

### GET /search?q=java&type=all

Busca usuarios e posts.

Query:

- `q`: termo.
- `type`: `all`, `users`, `posts`.

Response:

```json
{
  "users": [],
  "posts": []
}
```

### GET /search/semantic?q=arquitetura

Busca semantica opcional.

Regras:

- Pode retornar `503` se provider de embeddings/IA estiver desativado.
- Frontend deve cair para busca textual.

## AI

### POST /ai/caption

Gera legenda.

Request:

```json
{
  "topic": "foto de fim de tarde estudando Java",
  "tone": "casual",
  "language": "pt-BR"
}
```

Response `200`:

```json
{
  "provider": "GROQ_COMPOUND",
  "model": "groq/compound",
  "fallbackUsed": false,
  "suggestions": [
    "Fim de tarde, cafe e mais um passo no caminho do Java."
  ]
}
```

Fallback response:

```json
{
  "provider": "FALLBACK",
  "model": null,
  "fallbackUsed": true,
  "suggestions": [
    "Compartilhando um novo momento no TeachGram."
  ]
}
```

### POST /ai/hashtags

Request:

```json
{
  "title": "Projeto fullstack",
  "description": "Construindo uma rede social com Spring e React."
}
```

Response:

```json
{
  "hashtags": ["#SpringBoot", "#React", "#Fullstack", "#Java"]
}
```

### POST /ai/profile-bio

Request:

```json
{
  "keywords": ["Java", "React", "Docker", "IA"],
  "tone": "profissional"
}
```

Response:

```json
{
  "suggestions": [
    "Desenvolvedor fullstack focado em Java, React e arquitetura de produtos com IA."
  ]
}
```

### POST /ai/moderate

Request:

```json
{
  "resourceType": "POST",
  "text": "conteudo a moderar"
}
```

Response:

```json
{
  "status": "SAFE",
  "categories": [],
  "provider": "GROQ_COMPOUND",
  "model": "groq/compound-mini"
}
```

## Admin Tecnico

Endpoints de admin so devem existir se houver role `ADMIN`.

### GET /admin/ai/jobs

Lista jobs de IA.

### GET /admin/audit-events

Lista auditoria.

### GET /actuator/health

Health check publico ou parcialmente publico.

### GET /actuator/metrics

Restrito ou desativado no deploy publico.

