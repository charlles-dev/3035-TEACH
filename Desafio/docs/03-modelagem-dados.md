# 03 - Modelagem De Dados

## Convencoes Gerais

Banco principal: PostgreSQL.

Padroes:

- IDs como `UUID`.
- Datas como `TIMESTAMP WITH TIME ZONE` quando possivel.
- Soft delete com `deleted_at` para entidades principais.
- `created_at` e `updated_at` em todas as tabelas relevantes.
- Constraints unicas para email, username e relacoes sociais.
- Indices para feed, busca, foreign keys e paginacao.

Nomes:

- Tabelas em `snake_case`.
- Colunas em `snake_case`.
- Enums em texto ou enum PostgreSQL, preferindo texto no inicio para reduzir atrito de migrations.

## Entidades Principais

### user_accounts

Representa identidade e credenciais.

| Coluna | Tipo | Obrigatorio | Observacao |
| --- | --- | --- | --- |
| id | uuid | sim | PK |
| email | varchar(255) | sim | unico, normalizado em lowercase |
| username | varchar(40) | sim | unico, sem arroba salvo no banco |
| password_hash | varchar(255) | sim | BCrypt/Argon2 |
| role | varchar(30) | sim | `USER`, `ADMIN` |
| status | varchar(30) | sim | `ACTIVE`, `LOCKED`, `DELETED` |
| last_login_at | timestamptz | nao | atualizado no login |
| deleted_at | timestamptz | nao | soft delete |
| created_at | timestamptz | sim | criacao |
| updated_at | timestamptz | sim | ultima alteracao |

Constraints:

- `uk_user_accounts_email`
- `uk_user_accounts_username`

Indices:

- `idx_user_accounts_status`
- `idx_user_accounts_created_at`

### user_profiles

Representa dados publicos/editaveis do usuario.

| Coluna | Tipo | Obrigatorio | Observacao |
| --- | --- | --- | --- |
| id | uuid | sim | PK |
| user_id | uuid | sim | FK para `user_accounts.id` |
| display_name | varchar(120) | sim | nome exibido |
| phone | varchar(30) | nao | unico quando preenchido |
| bio | varchar(500) | nao | descricao do perfil |
| avatar_url | text | nao | URL externa ou Supabase Storage |
| profile_visibility | varchar(30) | sim | `PUBLIC`, `AUTHENTICATED_ONLY` |
| created_at | timestamptz | sim | criacao |
| updated_at | timestamptz | sim | ultima alteracao |

Constraints:

- `uk_user_profiles_user_id`
- `uk_user_profiles_phone`

### refresh_tokens

Armazena refresh tokens revogaveis.

| Coluna | Tipo | Obrigatorio | Observacao |
| --- | --- | --- | --- |
| id | uuid | sim | PK |
| user_id | uuid | sim | FK |
| token_hash | varchar(255) | sim | nunca salvar token cru |
| expires_at | timestamptz | sim | expiracao |
| revoked_at | timestamptz | nao | logout/revogacao |
| created_at | timestamptz | sim | criacao |

Indices:

- `idx_refresh_tokens_user_id`
- `idx_refresh_tokens_expires_at`

### posts

Representa publicacao.

| Coluna | Tipo | Obrigatorio | Observacao |
| --- | --- | --- | --- |
| id | uuid | sim | PK |
| author_id | uuid | sim | FK para `user_accounts.id` |
| title | varchar(50) | sim | limite do desafio |
| description | varchar(200) | nao | limite do desafio |
| visibility | varchar(30) | sim | `PUBLIC`, `PRIVATE`, `FOLLOWERS` |
| moderation_status | varchar(30) | sim | `PENDING`, `APPROVED`, `REVIEW_REQUIRED`, `HIDDEN` |
| like_count | integer | sim | contador denormalizado |
| comment_count | integer | sim | contador denormalizado |
| save_count | integer | sim | contador denormalizado |
| deleted_at | timestamptz | nao | soft delete |
| created_at | timestamptz | sim | criacao |
| updated_at | timestamptz | sim | ultima alteracao |

Indices:

- `idx_posts_author_created`
- `idx_posts_feed_public_created`
- `idx_posts_moderation_status`

Observacao:

- Contadores denormalizados melhoram feed.
- A fonte de verdade de likes/comentarios/saves continua nas tabelas proprias.

### media_assets

Representa midias associadas a posts.

| Coluna | Tipo | Obrigatorio | Observacao |
| --- | --- | --- | --- |
| id | uuid | sim | PK |
| post_id | uuid | sim | FK |
| type | varchar(30) | sim | `IMAGE`, `VIDEO` |
| url | text | sim | link externo ou storage |
| storage_key | text | nao | caminho Supabase Storage |
| alt_text | varchar(255) | nao | acessibilidade |
| created_at | timestamptz | sim | criacao |

Indices:

- `idx_media_assets_post_id`

### follows

Representa relacao dirigida entre usuarios.

| Coluna | Tipo | Obrigatorio | Observacao |
| --- | --- | --- | --- |
| id | uuid | sim | PK |
| follower_id | uuid | sim | quem segue |
| followed_id | uuid | sim | quem e seguido |
| status | varchar(30) | sim | `ACTIVE`, `BLOCKED` futuro |
| created_at | timestamptz | sim | criacao |

Constraints:

- `uk_follows_pair(follower_id, followed_id)`
- check `follower_id <> followed_id`

Indices:

- `idx_follows_follower_id`
- `idx_follows_followed_id`

### post_likes

Representa curtida unica por usuario/post.

| Coluna | Tipo | Obrigatorio | Observacao |
| --- | --- | --- | --- |
| id | uuid | sim | PK |
| post_id | uuid | sim | FK |
| user_id | uuid | sim | FK |
| created_at | timestamptz | sim | criacao |

Constraints:

- `uk_post_likes_post_user(post_id, user_id)`

Indices:

- `idx_post_likes_post_id`
- `idx_post_likes_user_id`

### post_comments

Representa comentario.

| Coluna | Tipo | Obrigatorio | Observacao |
| --- | --- | --- | --- |
| id | uuid | sim | PK |
| post_id | uuid | sim | FK |
| author_id | uuid | sim | FK |
| content | varchar(500) | sim | texto |
| deleted_at | timestamptz | nao | soft delete |
| created_at | timestamptz | sim | criacao |
| updated_at | timestamptz | sim | ultima alteracao |

Indices:

- `idx_post_comments_post_created`
- `idx_post_comments_author_id`

### saved_posts

Representa posts salvos pelo usuario.

| Coluna | Tipo | Obrigatorio | Observacao |
| --- | --- | --- | --- |
| id | uuid | sim | PK |
| post_id | uuid | sim | FK |
| user_id | uuid | sim | FK |
| created_at | timestamptz | sim | criacao |

Constraints:

- `uk_saved_posts_post_user(post_id, user_id)`

### feed_items

Tabela opcional para feed materializado.

| Coluna | Tipo | Obrigatorio | Observacao |
| --- | --- | --- | --- |
| id | uuid | sim | PK |
| owner_id | uuid | sim | dono do feed |
| post_id | uuid | sim | post exibido |
| author_id | uuid | sim | autor do post |
| reason | varchar(30) | sim | `SELF`, `FOLLOWING`, `PUBLIC_DISCOVERY` |
| score | numeric(12,4) | sim | ranking futuro |
| created_at | timestamptz | sim | momento em que entrou no feed |

Constraints:

- `uk_feed_items_owner_post(owner_id, post_id)`

Indices:

- `idx_feed_items_owner_created`
- `idx_feed_items_owner_score`

Observacao:

- Para MVP, feed pode consultar `posts` direto.
- Para versao portfolio, materializar feed demonstra arquitetura mais avancada.

### notifications

Representa notificacao.

| Coluna | Tipo | Obrigatorio | Observacao |
| --- | --- | --- | --- |
| id | uuid | sim | PK |
| recipient_id | uuid | sim | usuario que recebe |
| actor_id | uuid | nao | usuario que causou |
| type | varchar(40) | sim | `FOLLOW`, `LIKE`, `COMMENT`, `AI_REVIEW` |
| resource_type | varchar(40) | nao | `POST`, `PROFILE`, etc. |
| resource_id | uuid | nao | id do recurso |
| message | varchar(255) | sim | texto curto |
| read_at | timestamptz | nao | lida |
| created_at | timestamptz | sim | criacao |

Indices:

- `idx_notifications_recipient_created`
- `idx_notifications_recipient_read`

### ai_jobs

Registra execucoes de IA.

| Coluna | Tipo | Obrigatorio | Observacao |
| --- | --- | --- | --- |
| id | uuid | sim | PK |
| user_id | uuid | nao | usuario solicitante |
| provider | varchar(50) | sim | `GROQ_COMPOUND`, `FALLBACK` |
| model | varchar(120) | nao | modelo usado |
| type | varchar(40) | sim | `CAPTION`, `HASHTAGS`, `BIO`, `MODERATION` |
| status | varchar(40) | sim | `PENDING`, `RUNNING`, `SUCCEEDED`, `FAILED`, `FALLBACK_USED` |
| input_hash | varchar(128) | sim | hash para auditoria sem armazenar conteudo bruto sensivel |
| output_preview | varchar(500) | nao | preview seguro |
| error_code | varchar(80) | nao | falha tecnica |
| latency_ms | integer | nao | tempo de resposta |
| created_at | timestamptz | sim | criacao |
| completed_at | timestamptz | nao | fim |

Indices:

- `idx_ai_jobs_user_created`
- `idx_ai_jobs_status_created`
- `idx_ai_jobs_type_created`

### moderation_events

Registra resultado de moderacao.

| Coluna | Tipo | Obrigatorio | Observacao |
| --- | --- | --- | --- |
| id | uuid | sim | PK |
| resource_type | varchar(40) | sim | `POST`, `COMMENT`, `PROFILE` |
| resource_id | uuid | sim | id do recurso |
| provider | varchar(50) | sim | provider |
| model | varchar(120) | nao | modelo |
| status | varchar(40) | sim | `SAFE`, `UNSAFE`, `REVIEW_REQUIRED`, `ERROR` |
| categories | jsonb | nao | categorias detectadas |
| raw_score | numeric(8,4) | nao | score se houver |
| created_at | timestamptz | sim | criacao |

Indices:

- `idx_moderation_events_resource`
- `idx_moderation_events_status_created`

### audit_events

Auditoria de acoes relevantes.

| Coluna | Tipo | Obrigatorio | Observacao |
| --- | --- | --- | --- |
| id | uuid | sim | PK |
| actor_id | uuid | nao | usuario |
| action | varchar(80) | sim | acao |
| resource_type | varchar(40) | nao | tipo |
| resource_id | uuid | nao | id |
| ip_address | varchar(80) | nao | IP |
| user_agent | text | nao | navegador |
| metadata | jsonb | nao | dados extras |
| created_at | timestamptz | sim | criacao |

Indices:

- `idx_audit_events_actor_created`
- `idx_audit_events_action_created`
- `idx_audit_events_resource`

### outbox_events

Tabela para eventos persistentes/retry.

| Coluna | Tipo | Obrigatorio | Observacao |
| --- | --- | --- | --- |
| id | uuid | sim | PK |
| aggregate_type | varchar(80) | sim | modulo/agregado |
| aggregate_id | uuid | sim | id |
| event_type | varchar(120) | sim | nome do evento |
| payload | jsonb | sim | conteudo |
| status | varchar(40) | sim | `PENDING`, `PROCESSING`, `PROCESSED`, `FAILED` |
| attempts | integer | sim | tentativas |
| last_error | text | nao | erro |
| created_at | timestamptz | sim | criacao |
| processed_at | timestamptz | nao | processamento |

Indices:

- `idx_outbox_events_status_created`
- `idx_outbox_events_aggregate`

## Relacionamentos

Resumo:

- `user_accounts 1:1 user_profiles`
- `user_accounts 1:N posts`
- `posts 1:N media_assets`
- `user_accounts N:N user_accounts` via `follows`
- `posts N:N user_accounts` via `post_likes`
- `posts N:N user_accounts` via `saved_posts`
- `posts 1:N post_comments`
- `user_accounts 1:N notifications`
- `posts 1:N moderation_events`

## Regras De Soft Delete

Usuarios:

- `status = DELETED`
- `deleted_at` preenchido.
- Login bloqueado.
- Perfil nao aparece em busca.

Posts:

- `deleted_at` preenchido.
- Sai de feed e busca.
- Mantem relacionamentos historicos.

Comentarios:

- `deleted_at` preenchido.
- Pode exibir placeholder "comentario removido" ou ocultar.

## Regras De Privacidade

`posts.visibility`:

- `PUBLIC`: aparece para todos os usuarios autenticados e em perfil publico.
- `FOLLOWERS`: aparece para autor e seguidores.
- `PRIVATE`: aparece apenas para autor.

Primeira versao pode implementar apenas `PUBLIC` e `PRIVATE`, mantendo o enum pronto para `FOLLOWERS`.

## Cursor Pagination

Padrao recomendado:

- Ordenar por `created_at DESC, id DESC`.
- Cursor codificado em Base64 contendo `createdAt` e `id`.
- Nunca usar offset para feed principal em escala.

Exemplo de cursor decodificado:

```json
{
  "createdAt": "2026-05-13T18:30:00Z",
  "id": "0a5b8e5d-9f5a-4b5a-9f1f-8c0a4bde9871"
}
```

## Migrations Recomendadas

Ordem:

1. `V1__create_identity_tables.sql`
2. `V2__create_profile_tables.sql`
3. `V3__create_posts_and_media.sql`
4. `V4__create_social_graph.sql`
5. `V5__create_engagement.sql`
6. `V6__create_feed_notifications.sql`
7. `V7__create_ai_moderation_audit.sql`
8. `V8__create_outbox.sql`
9. `V9__seed_demo_data.sql`

## Dados De Seed

Ambiente demo deve conter:

- Usuario demo principal.
- 8 a 12 usuarios ficticios.
- 30 a 60 posts.
- Relacoes de follow.
- Likes e comentarios.
- Algumas notificacoes nao lidas.
- Exemplos de posts com IA sugerida.

Regras:

- Nunca usar dados pessoais reais.
- Usar imagens de placeholder ou assets permitidos.
- Senhas demo devem ser explicitas no README, mas apenas em ambiente demo.

