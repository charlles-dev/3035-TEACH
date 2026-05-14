# 12 - Roadmap

## Visao

O roadmap separa o projeto em fases para permitir implementacao manual sem travar em complexidade. Cada fase deve terminar com software rodando.

## Fase 0 - Preparacao

Objetivo:

- Estruturar repositorio e ambiente.

Entregas:

- Monorepo.
- Backend Spring Boot.
- Frontend React.
- Docker Compose com Postgres e Redis.
- Flyway.
- Health check.
- README inicial.

Aceite:

- Backend sobe localmente.
- Frontend sobe localmente.
- Banco conecta.
- `/actuator/health` responde.

## Fase 1 - MVP Do Desafio

Objetivo:

- Atender os requisitos originais do curso.

Entregas:

- Cadastro.
- Login.
- CRUD de usuario.
- CRUD de post.
- JPA 1:N usuario-post.
- Soft delete.
- Post publico/privado.
- Listagem de usuarios.
- Listagem de posts por usuario.
- Interface responsiva base.

Aceite:

- Usuario cria conta.
- Usuario loga.
- Usuario cria post.
- Usuario edita post.
- Usuario deleta post.
- Usuario edita/exclui conta.

## Fase 2 - Rede Social Real

Objetivo:

- Sair do CRUD e virar produto.

Entregas:

- Follow/seguidores.
- Feed cronologico.
- Likes unicos por usuario.
- Comentarios.
- Saves.
- Perfil publico.
- Busca de usuarios.
- Notificacoes sociais.

Aceite:

- Usuario segue outro.
- Feed mostra posts relevantes.
- Like nao duplica.
- Comentario aparece.
- Notificacao e criada.

## Fase 3 - Arquitetura Pro

Objetivo:

- Demonstrar maturidade de backend.

Entregas:

- Spring Modulith organizado.
- Eventos internos.
- Auditoria.
- Outbox simplificada.
- Redis cache/rate limit.
- Cursor pagination.
- Testcontainers.
- Swagger completo.

Aceite:

- Teste de modularidade passa.
- Feed usa cursor.
- Rate limit funciona.
- Logs tem trace id.

## Fase 4 - IA

Objetivo:

- Adicionar recursos chamativos sem quebrar custo zero.

Entregas:

- `AiProvider`.
- Groq Compound provider.
- Fallback provider.
- Gerar legenda.
- Hashtags.
- Melhorar bio.
- Moderacao.
- `ai_jobs`.

Aceite:

- IA responde quando provider disponivel.
- Fallback responde quando provider falha.
- App continua funcionando sem IA.

## Fase 5 - Deploy Publico

Objetivo:

- Tornar o projeto acessivel por link.

Entregas:

- Frontend na Vercel.
- Backend no Render.
- Banco Supabase.
- Redis Upstash.
- Variaveis seguras.
- Seed demo.
- README com links.

Aceite:

- Recrutador acessa link.
- Login demo funciona.
- Fluxo principal funciona online.

## Fase 6 - Polish De Portfolio

Objetivo:

- Melhorar apresentacao e conversa tecnica.

Entregas:

- Screenshots.
- Diagramas.
- GIF curto ou video demo.
- README refinado.
- ADRs completos.
- Test badges.
- Runbook.
- Roadmap futuro.

Aceite:

- GitHub explica o projeto em ate 2 minutos.
- Documentacao sustenta perguntas tecnicas.

## Fase 7 - Evolucoes Futuras

Possibilidades:

- Chat em tempo real.
- Stories.
- Upload real com processamento de imagem.
- Recomendacao com embeddings.
- Moderacao multimodal.
- Painel admin.
- Mobile com React Native.
- Microservices seletivos.
- Kubernetes local com Helm.
- Observabilidade com OpenTelemetry.

Regra:

- So evoluir para microservices quando houver motivo tecnico claro: escala independente, times separados, deploy independente ou isolamento de falhas.

