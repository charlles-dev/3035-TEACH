# 13 - Guia De Implementacao

## Objetivo

Este guia define a ordem recomendada para implementar o TeachGram Pro manualmente. Ele evita que voce comece pela parte mais chamativa e depois descubra que faltou base.

Principio:

- Cada fase deve terminar com algo rodando e testavel.

## Fase 0 - Repositorio E Ambiente

1. Criar monorepo:

```text
backend/
frontend/
infra/
docs/
```

2. Criar backend Spring Boot com dependencias:

- Web.
- Validation.
- Security.
- OAuth2 Resource Server ou JWT library.
- Data JPA.
- PostgreSQL Driver.
- Flyway.
- Actuator.
- Spring Modulith.
- OpenAPI.

3. Criar frontend React TypeScript com Vite.

4. Criar Docker Compose local:

- Postgres.
- Redis.
- RabbitMQ opcional.
- Prometheus/Grafana opcional.

5. Criar health check no backend.

Pronto quando:

- Backend sobe.
- Frontend sobe.
- Banco conecta.
- Health responde.

## Fase 0.5 - Base Visual Do Figma

Objetivo:

- Preparar a implementacao frontend para seguir o design oficial desde o primeiro componente.

Passos:

1. Ler [14 - Design Figma Oficial](14-design-figma.md).
2. Ler [15 - Telas Pro Derivadas](15-telas-pro.md).
3. Criar tokens CSS ou tema equivalente:

```text
--color-brand-coral: #F37671
--color-text-strong: #303030
--color-text-default: #666666
--color-text-muted: #8E8E8E
--color-text-soft: #A09F9F
--color-border-default: #E2E2E2
--color-surface: #FFFFFF
```

4. Implementar componentes visuais base:

- `TeachgramLogo`
- `AuthShell`
- `AuthInput`
- `PrimaryButton`
- `SocialLoginButton`
- `SidebarNav`
- `SidebarNavItem`
- `PostCard`
- `ProfileAvatar`
- `ConfirmDialog`
- `EmptyState`

5. Validar visualmente:

- Login em `1440x1024`.
- Login em `390x844`.
- Feed em `1440x1024`.
- Feed em `390x844`.

Pronto quando:

- Tokens existem.
- Componentes base existem.
- Login e Feed conseguem ser montados seguindo o Figma.
- Telas Pro possuem caminho visual definido antes de serem codadas.

## Fase 1 - Identity E Profiles

1. Criar migrations:

- `user_accounts`
- `user_profiles`
- `refresh_tokens`

2. Criar entidades JPA.

3. Criar repositories.

4. Criar DTOs:

- `SignupRequest`
- `LoginRequest`
- `TokenResponse`
- `MeResponse`
- `ProfileUpdateRequest`

5. Criar services:

- `SignupService`
- `AuthenticationService`
- `TokenService`
- `ProfileService`

6. Criar controllers:

- `AuthController`
- `MeController`
- `ProfileController`

7. Implementar JWT.

8. Criar telas:

- Login.
- Signup.
- Meu perfil.
- Editar perfil.
- Recuperar senha derivada do Login.

Testar:

- Signup.
- Login.
- Refresh.
- Edit profile.
- Delete account.

## Fase 2 - Posts E Media

1. Criar migrations:

- `posts`
- `media_assets`

2. Criar entidades:

- `Post`
- `MediaAsset`

3. Criar services:

- `PostCreationService`
- `PostUpdateService`
- `PostVisibilityPolicy`

4. Criar endpoints:

- `POST /posts`
- `GET /posts/{id}`
- `PATCH /posts/{id}`
- `DELETE /posts/{id}`
- `PATCH /posts/{id}/privacy`
- `GET /users/{username}/posts`

5. Criar frontend:

- Criar post.
- Card de post.
- Perfil com posts.
- Menu editar/deletar.

Testar:

- Dono cria/edita/deleta.
- Terceiro nao edita.
- Post privado nao aparece para terceiro.

## Fase 3 - Feed

1. Implementar feed por consulta direta.

2. Implementar cursor pagination.

3. Criar endpoint:

- `GET /feed`

4. Criar tela:

- Feed principal.

5. Adicionar cache Redis curto.

6. Invalidar cache em create/update/delete post.

Testar:

- Feed ordenado.
- Cursor sem duplicar.
- Privacidade respeitada.
- Cache nao vaza post privado.

## Fase 4 - Social Graph

1. Criar migration:

- `follows`

2. Criar entidade:

- `Follow`

3. Criar service:

- `FollowService`

4. Criar endpoints:

- `POST /relationships/{userId}/follow`
- `DELETE /relationships/{userId}/follow`
- `GET /relationships/me/following`
- `GET /relationships/me/followers`

5. Criar frontend:

- Botao seguir.
- Tela de conexoes.
- Abas seguidores/seguindo.

6. Atualizar feed para considerar follows.

Testar:

- Seguir.
- Deixar de seguir.
- Nao seguir a si mesmo.
- Feed muda apos seguir.

## Fase 5 - Engagement

1. Criar migrations:

- `post_likes`
- `post_comments`
- `saved_posts`

2. Criar services:

- `LikeService`
- `CommentService`
- `SavePostService`

3. Criar endpoints:

- `POST /posts/{id}/likes`
- `DELETE /posts/{id}/likes`
- `POST /posts/{id}/comments`
- `GET /posts/{id}/comments`
- `POST /posts/{id}/saves`
- `DELETE /posts/{id}/saves`

4. Atualizar `PostResponse` com:

- `stats`
- `viewerState`

5. Atualizar frontend:

- Curtir.
- Comentar.
- Salvar.
- Contadores.

Testar:

- Like unico.
- Unlike.
- Comentario.
- Save.
- Contadores consistentes.

## Fase 6 - Notifications E Audit

1. Criar migrations:

- `notifications`
- `audit_events`

2. Criar eventos internos:

- Follow.
- Like.
- Comment.
- Post created/deleted.

3. Criar listeners:

- `NotificationListener`
- `AuditListener`

4. Criar endpoints:

- `GET /notifications`
- `PATCH /notifications/{id}/read`

5. Criar frontend:

- Lista de notificacoes.
- Badge de nao lidas.

Testar:

- Like de terceiro gera notificacao.
- Comentario gera notificacao.
- Propria interacao nao notifica.
- Auditoria registra eventos principais.

## Fase 7 - Spring Modulith E Outbox

1. Reorganizar pacotes por modulo.

2. Criar teste:

- `ApplicationModules.of(...).verify()`

3. Garantir que modulo nao acessa repository de outro modulo.

4. Criar `outbox_events` ou usar suporte de eventos persistentes do Spring Modulith.

5. Persistir eventos importantes.

6. Criar job simples para reprocessar falhas.

Testar:

- Teste de modularidade passa.
- Evento cria notificacao.
- Falha de listener nao quebra acao principal.

## Fase 8 - IA

1. Criar migration:

- `ai_jobs`
- `moderation_events`

2. Criar interface:

- `AiProvider`

3. Criar implementacoes:

- `GroqCompoundAiProvider`
- `FallbackAiProvider`
- `CompositeAiProvider`

4. Criar endpoints:

- `POST /ai/caption`
- `POST /ai/hashtags`
- `POST /ai/profile-bio`
- `POST /ai/moderate`

5. Adicionar rate limit.

6. Criar frontend:

- Painel IA no criar post.
- Painel IA no editar bio.
- Estado de fallback.

Testar:

- Provider fake sucesso.
- Provider fake falha.
- Fallback.
- Rate limit.
- Job registrado.

## Fase 9 - Search

1. Implementar busca textual:

- Usuarios por nome/username.
- Posts por titulo/descricao/hashtags.

2. Criar endpoint:

- `GET /search`

3. Criar tela:

- Busca com filtros.
- Seguir especificacao de `/search` em [15 - Telas Pro Derivadas](15-telas-pro.md).

4. Implementar busca semantica opcional se provider permitir.

Testar:

- Busca retorna usuarios ativos.
- Busca nao retorna post privado indevido.
- Busca semantica cai para textual quando indisponivel.

## Fase 9.5 - Telas Pro De Produto

Objetivo:

- Completar as telas que nao existem no Figma original, mantendo a mesma identidade visual.

Implementar:

- `/notifications`
- `/saved`
- `/posts/:postId`
- Estados de moderacao.
- Estados vazios e de erro.
- `/demo` se for usar no portfolio.

Regras:

- Consultar [15 - Telas Pro Derivadas](15-telas-pro.md).
- Reutilizar `PostCard`, `SidebarNav`, `EmptyState`, `ConfirmDialog` e `AiAssistPanel`.
- Nao criar paleta, radius ou layout paralelos.

Testar:

- Notificacoes vazias/carregadas.
- Salvos vazios/carregados.
- Detalhe de post com comentarios.
- Post inacessivel por privacidade.
- Moderacao em analise/oculto.

## Fase 10 - Observabilidade

1. Configurar Actuator.

2. Criar correlation id filter.

3. Padronizar logs.

4. Criar metricas customizadas:

- Login.
- Posts.
- Feed.
- IA.

5. Criar Docker Compose local com Prometheus/Grafana se desejar.

Testar:

- Health responde.
- Logs tem trace id.
- Erro retorna traceId.
- Metricas aparecem localmente.

## Fase 11 - Deploy

1. Criar Supabase project.

2. Criar Upstash Redis.

3. Criar Render service.

4. Criar Vercel project.

5. Configurar variaveis.

6. Rodar migrations.

7. Rodar seed demo.

8. Testar fluxo publico.

Pronto quando:

- Link frontend abre.
- Login demo funciona.
- API health UP.
- Criar post funciona.
- IA ou fallback funciona.

## Fase 12 - Portfolio Polish

1. Atualizar README com:

- Links reais.
- Screenshots.
- Stack.
- Arquitetura.
- Como rodar.
- Trade-offs.

2. Adicionar badges.

3. Adicionar diagramas.

4. Gravar GIF/video curto.

5. Revisar documentacao.

6. Criar release `v1.0.0`.

## Ordem De Prioridade Se O Tempo Apertar

Essencial:

1. Auth.
2. Perfil.
3. Posts.
4. Feed.
5. Follow.
6. Likes.
7. Deploy.

Depois:

1. Comentarios.
2. Notificacoes.
3. IA.
4. Cache.
5. Outbox.
6. Observabilidade completa.

Nao comece por:

- Kubernetes.
- Microservices.
- Chat realtime.
- Stories.
- Ranking complexo.

Essas coisas so fazem sentido depois do produto base estar vivo.
