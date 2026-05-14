# Diagramas

Este arquivo contem diagramas Mermaid para usar no GitHub, README, documentacao tecnica e apresentacoes.

## Arquitetura Geral

```mermaid
flowchart LR
    user["Usuario / Recrutador"] --> frontend["Frontend React<br/>Vercel Hobby"]
    frontend --> api["Backend Spring Boot<br/>Render Free"]
    api --> db["PostgreSQL<br/>Supabase Free"]
    api --> redis["Redis<br/>Upstash Free"]
    api --> storage["Storage de Midia<br/>Supabase Storage"]
    api --> groq["Groq Compound<br/>IA com cota/trial"]
    api --> logs["Logs / Actuator<br/>Render + Local"]

    groq -. fallback .-> api
```

## Spring Modulith

```mermaid
flowchart TB
    shared["shared"]
    identity["identity"]
    profiles["profiles"]
    posts["posts"]
    media["media"]
    social["socialgraph"]
    engagement["engagement"]
    feed["feed"]
    notifications["notifications"]
    search["search"]
    ai["ai"]
    audit["audit"]

    identity --> shared
    profiles --> shared
    posts --> shared
    media --> shared
    social --> shared
    engagement --> shared
    feed --> shared
    notifications --> shared
    search --> shared
    ai --> shared
    audit --> shared

    identity -- UserRegisteredEvent --> profiles
    identity -- UserDeletedEvent --> audit
    posts -- PostCreatedEvent --> feed
    posts -- PostCreatedEvent --> ai
    posts -- PostDeletedEvent --> feed
    social -- UserFollowedEvent --> feed
    social -- UserFollowedEvent --> notifications
    engagement -- PostLikedEvent --> notifications
    engagement -- PostCommentedEvent --> notifications
    profiles -- ProfileUpdatedEvent --> search
    posts -- PostCreatedEvent --> search
    ai -- ContentModeratedEvent --> posts
```

## Entidades Principais

```mermaid
erDiagram
    USER_ACCOUNTS ||--|| USER_PROFILES : has
    USER_ACCOUNTS ||--o{ REFRESH_TOKENS : owns
    USER_ACCOUNTS ||--o{ POSTS : creates
    POSTS ||--o{ MEDIA_ASSETS : contains
    USER_ACCOUNTS ||--o{ FOLLOWS : follower
    USER_ACCOUNTS ||--o{ FOLLOWS : followed
    POSTS ||--o{ POST_LIKES : receives
    USER_ACCOUNTS ||--o{ POST_LIKES : creates
    POSTS ||--o{ POST_COMMENTS : receives
    USER_ACCOUNTS ||--o{ POST_COMMENTS : writes
    POSTS ||--o{ SAVED_POSTS : saved
    USER_ACCOUNTS ||--o{ SAVED_POSTS : saves
    USER_ACCOUNTS ||--o{ NOTIFICATIONS : receives
    POSTS ||--o{ FEED_ITEMS : appears_in
    USER_ACCOUNTS ||--o{ FEED_ITEMS : owns
    USER_ACCOUNTS ||--o{ AI_JOBS : requests
    POSTS ||--o{ MODERATION_EVENTS : moderated
    USER_ACCOUNTS ||--o{ AUDIT_EVENTS : acts
```

## Fluxo De Login

```mermaid
sequenceDiagram
    actor U as Usuario
    participant F as Frontend
    participant A as AuthController
    participant S as AuthenticationService
    participant DB as PostgreSQL

    U->>F: informa identifier e senha
    F->>A: POST /auth/login
    A->>S: authenticate(identifier, password)
    S->>DB: buscar usuario por email ou username
    DB-->>S: user_account
    S->>S: validar senha e status
    S->>DB: salvar refresh token hash
    S-->>A: tokens + usuario
    A-->>F: 200 OK
    F-->>U: redireciona para feed
```

## Criacao De Post Com IA

```mermaid
sequenceDiagram
    actor U as Usuario
    participant F as Frontend
    participant AI as AI API
    participant N as Groq Compound
    participant P as Posts API
    participant DB as PostgreSQL
    participant E as Eventos

    U->>F: abre criar post
    U->>F: pede legenda
    F->>AI: POST /ai/caption
    AI->>N: request com timeout
    alt provider disponivel
        N-->>AI: sugestoes
        AI-->>F: sugestoes reais
    else provider falha
        AI-->>F: fallback
    end
    U->>F: revisa e publica
    F->>P: POST /posts
    P->>DB: salvar post
    P->>E: PostCreatedEvent
    E->>DB: feed, audit, moderation jobs
    P-->>F: 201 Created
```

## Feed Direto MVP

```mermaid
flowchart TD
    request["GET /feed"] --> auth["Identificar viewer"]
    auth --> follows["Buscar usuarios seguidos"]
    follows --> query["Consultar posts visiveis<br/>publicos + proprios + followers"]
    query --> order["Ordenar por created_at desc, id desc"]
    order --> cursor["Aplicar cursor pagination"]
    cursor --> response["Retornar PostResponse page"]
```

## Feed Materializado Portfolio

```mermaid
sequenceDiagram
    participant P as Posts Module
    participant E as Event Bus
    participant S as SocialGraph
    participant F as Feed Module
    participant DB as PostgreSQL
    participant R as Redis

    P->>DB: salva post
    P->>E: PostCreatedEvent
    E->>F: listener recebe evento
    F->>S: busca seguidores do autor
    S-->>F: seguidores
    F->>DB: cria feed_items
    F->>R: invalida caches afetados
```

## Notificacoes

```mermaid
flowchart LR
    like["PostLikedEvent"] --> notif["notifications"]
    comment["PostCommentedEvent"] --> notif
    follow["UserFollowedEvent"] --> notif
    notif --> db["notifications table"]
    db --> api["GET /notifications"]
    api --> ui["NotificationsPage"]
```

## Deploy Custo Zero

```mermaid
flowchart TB
    github["GitHub Repository"]
    github --> vercel["Vercel<br/>frontend deploy"]
    github --> render["Render<br/>backend deploy"]
    render --> supabase["Supabase<br/>PostgreSQL + Storage"]
    render --> upstash["Upstash<br/>Redis"]
    render --> groq["Groq Compound<br/>AI provider"]

    render -. cold start .-> note["Primeira chamada pode demorar"]
    groq -. cota indisponivel .-> fallback["Fallback AI Provider"]
```

## Fluxo De Privacidade De Post

```mermaid
flowchart TD
    start["Viewer solicita post/feed"] --> deleted{"Post deletado?"}
    deleted -- sim --> deny["Nao retorna"]
    deleted -- nao --> visibility{"Visibility"}
    visibility -- PUBLIC --> allow["Permite"]
    visibility -- PRIVATE --> owner{"Viewer e autor?"}
    owner -- sim --> allow
    owner -- nao --> deny
    visibility -- FOLLOWERS --> follower{"Viewer segue autor?"}
    follower -- sim --> allow
    follower -- nao --> owner2{"Viewer e autor?"}
    owner2 -- sim --> allow
    owner2 -- nao --> deny
```
