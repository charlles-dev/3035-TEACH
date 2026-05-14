# 02 - Arquitetura

## Decisao Principal

O TeachGram Pro deve ser implementado como **modular monolith com Spring Modulith**, nao como microservices na primeira versao.

Motivos:

- O projeto precisa rodar em free tier.
- Um unico deploy backend e mais simples de operar.
- O dominio ainda esta evoluindo.
- Spring Modulith permite separar responsabilidades sem custo operacional de microservices.
- A arquitetura ainda pode evoluir para servicos separados quando houver necessidade real.

ADR relacionado: [0001 - Spring Modulith](adr/0001-spring-modulith.md).

## Visao Geral

Camadas:

- Frontend React no Vercel.
- Backend Spring Boot no Render.
- PostgreSQL no Supabase.
- Storage de midia no Supabase Storage.
- Redis no Upstash.
- Groq Compound como provider externo de IA.

Fluxo geral:

1. Usuario acessa frontend.
2. Frontend chama API REST.
3. API valida JWT e regras de permissao.
4. Modulo de dominio executa caso de uso.
5. Banco recebe alteracoes transacionais.
6. Eventos internos notificam feed, notificacoes, auditoria e IA.
7. Redis acelera leituras e rate limit.
8. Observabilidade registra logs, metricas e health checks.

## Estrutura Recomendada Do Monorepo

```text
teachgram-pro/
  backend/
    pom.xml
    src/main/java/com/teachgram/
      TeachgramApplication.java
      identity/
      profiles/
      posts/
      media/
      socialgraph/
      engagement/
      feed/
      notifications/
      search/
      ai/
      audit/
      shared/
    src/main/resources/
      application.yml
      application-local.yml
      application-demo.yml
      db/migration/
  frontend/
    package.json
    src/
      app/
      pages/
      features/
      components/
      lib/
      styles/
  infra/
    docker-compose.yml
    render.yaml
    vercel.json
  docs/
```

## Modulos Backend

### shared

Responsabilidade:

- Tipos comuns.
- `ProblemDetail` factory.
- Configuracoes globais.
- Pagination/cursor helpers.
- Security helpers sem regra de negocio especifica.
- Exceptions base.

Nao deve conter:

- Services de dominio.
- Repositories de dominio.
- Regras especificas de usuario, post ou feed.

### identity

Responsabilidade:

- Cadastro.
- Login.
- Tokens.
- Hash de senha.
- Refresh token.
- Usuario autenticado.

Entidades principais:

- `UserAccount`
- `RefreshToken`

Eventos publicados:

- `UserRegisteredEvent`
- `UserLoggedInEvent`
- `UserDeletedEvent`

Dependencias permitidas:

- `shared`
- `audit` por evento, nao chamada direta obrigatoria.

### profiles

Responsabilidade:

- Perfil publico.
- Bio.
- Foto.
- Nome exibido.
- Edicao de dados publicos.

Entidades principais:

- `UserProfile`

Eventos publicados:

- `ProfileUpdatedEvent`

Consome:

- `UserRegisteredEvent`
- `UserDeletedEvent`

### posts

Responsabilidade:

- Criacao, edicao e exclusao logica de posts.
- Privacidade.
- Dados textuais do post.

Entidades principais:

- `Post`

Eventos publicados:

- `PostCreatedEvent`
- `PostUpdatedEvent`
- `PostDeletedEvent`
- `PostPrivacyChangedEvent`

### media

Responsabilidade:

- Referencias de imagem/video.
- Validacao de URL.
- Integracao futura com Supabase Storage.

Entidades principais:

- `MediaAsset`

Observacao:

- Na primeira versao, o sistema pode aceitar links externos.
- Em versao portfolio, adicionar upload para Supabase Storage melhora a experiencia.

### socialgraph

Responsabilidade:

- Follow/following.
- Lista de seguidores.
- Lista de seguindo.
- Bloqueios futuros.

Entidades principais:

- `Follow`

Eventos publicados:

- `UserFollowedEvent`
- `UserUnfollowedEvent`

### engagement

Responsabilidade:

- Likes.
- Comentarios.
- Saves.
- Contadores.

Entidades principais:

- `PostLike`
- `PostComment`
- `SavedPost`

Eventos publicados:

- `PostLikedEvent`
- `PostUnlikedEvent`
- `PostCommentedEvent`
- `PostSavedEvent`

### feed

Responsabilidade:

- Timeline.
- Feed por perfil.
- Materializacao opcional de feed.
- Cursor pagination.

Entidades principais:

- `FeedItem`

Consome:

- `PostCreatedEvent`
- `PostDeletedEvent`
- `PostPrivacyChangedEvent`
- `UserFollowedEvent`

### notifications

Responsabilidade:

- Criar notificacoes sociais.
- Marcar como lidas.
- Listar notificacoes.

Entidades principais:

- `Notification`

Consome:

- `UserFollowedEvent`
- `PostLikedEvent`
- `PostCommentedEvent`

### search

Responsabilidade:

- Busca textual.
- Busca semantica opcional.
- Indexacao simples.

Entidades principais:

- `SearchDocument`

Consome:

- `PostCreatedEvent`
- `PostUpdatedEvent`
- `ProfileUpdatedEvent`

### ai

Responsabilidade:

- Interface de provider de IA.
- Groq Compound provider.
- Fallback provider.
- Moderacao.
- Jobs assincromos.

Entidades principais:

- `AiJob`
- `ModerationEvent`

Eventos publicados:

- `AiJobCompletedEvent`
- `ContentModeratedEvent`

### audit

Responsabilidade:

- Auditoria de acoes importantes.
- Logs de negocio.
- Trilha basica para LGPD e seguranca.

Entidades principais:

- `AuditEvent`

Consome:

- Eventos de todos os modulos relevantes.

## Comunicacao Entre Modulos

Preferencia:

1. Chamada direta apenas para consultas simples e interfaces publicas de modulo.
2. Eventos internos para efeitos colaterais.
3. Jobs assincromos para IA e tarefas lentas.

Regra:

- Um modulo nao deve acessar repository de outro modulo.
- Um modulo nao deve manipular entidade JPA de outro modulo diretamente.
- Expor DTOs ou interfaces de aplicacao quando precisar de comunicacao direta.

## Eventos Internos

Use eventos para:

- Criar perfil apos cadastro.
- Atualizar feed apos post.
- Criar notificacao apos like/comentario/follow.
- Registrar auditoria.
- Disparar moderacao/IA.

Eventos devem carregar:

- Identificador do recurso.
- Identificador do usuario causador.
- Timestamp.
- Dados minimos para o consumidor buscar o restante, se necessario.

Exemplo conceitual:

```java
public record PostCreatedEvent(
    UUID postId,
    UUID authorId,
    Instant occurredAt
) {}
```

## Transacoes

Regras:

- Casos de uso que alteram banco devem ser `@Transactional`.
- Eventos internos devem ser publicados dentro da transacao, mas consumidores que geram efeitos secundarios devem preferir `@TransactionalEventListener`.
- Jobs de IA nao devem segurar transacao aberta.
- Escritas em feed materializado devem ser idempotentes.

## Cache

Redis deve ser usado para:

- Rate limit.
- Cache curto de feed.
- Cache curto de perfil publico.
- Cache de contadores populares.
- Blacklist opcional de tokens revogados.

Redis nao deve ser usado como fonte de verdade para:

- Posts.
- Usuarios.
- Likes.
- Follows.
- Notificacoes.

Politica recomendada:

- Feed: TTL de 30 a 120 segundos.
- Perfil publico: TTL de 60 a 300 segundos.
- Rate limit: janela de 1 minuto e 1 hora.
- Invalide cache por evento quando possivel.

## Assincronia

No deploy gratuito, evite depender de RabbitMQ em producao, porque manter mais um servico pode complicar.

Abordagem recomendada:

- Local: Docker Compose pode incluir RabbitMQ para estudo.
- Demo: usar eventos internos do Spring + tabelas de job/outbox.
- Futuro: externalizar eventos para RabbitMQ, Kafka ou CloudAMQP.

## Outbox Simplificada

Para portfolio, implementar uma tabela `outbox_events` ou usar suporte de eventos persistentes do Spring Modulith e suficiente para mostrar maturidade.

Uso:

- Registrar evento importante no mesmo commit da acao principal.
- Um scheduler/worker processa eventos pendentes.
- Falhas ficam registradas para retry.

## Seguranca Arquitetural

- API stateless com JWT.
- Refresh tokens persistidos e revogaveis.
- BCrypt ou Argon2 para senha.
- CORS restrito ao dominio do frontend.
- Rate limit em login, signup e endpoints de IA.
- Secrets apenas em variaveis de ambiente.
- Responses nunca retornam senha, token hash ou dados internos.

## Observabilidade

Obrigatorio:

- `/actuator/health`
- `/actuator/info`
- Logs com correlation id.
- Tempo de resposta por request.
- Logs de falha de provider de IA.

Recomendado:

- Micrometer.
- Prometheus local.
- Grafana local.
- Dashboard simples para latencia, erros, uso de IA, cache hit/miss e status do banco.

## Ambientes

### local

Objetivo: desenvolvimento completo.

Recursos:

- Postgres local via Docker.
- Redis local via Docker.
- RabbitMQ opcional.
- Provider de IA real se houver chave, fallback caso contrario.
- Logs detalhados.

### test

Objetivo: testes automatizados.

Recursos:

- Testcontainers para Postgres.
- Redis fake ou container.
- IA mockada.
- Flyway ativo.

### demo

Objetivo: deploy publico gratuito.

Recursos:

- Render Free.
- Supabase.
- Upstash.
- Groq Compound com fallback.
- Logs sem dados sensiveis.

## Trade-Offs

O que a arquitetura ganha:

- Deploy simples.
- Separacao de dominio.
- Baixo custo.
- Boa historia para entrevista.
- Caminho claro para escalar.

O que ela nao entrega ainda:

- Escala horizontal independente por modulo.
- Filas robustas de producao.
- Realtime forte.
- Processamento pesado de imagem/video.
- SLA.

Essas limitacoes devem ser assumidas no README e na entrevista. O ponto forte e mostrar que a decisao foi consciente.

