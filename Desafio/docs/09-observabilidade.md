# 09 - Observabilidade

## Objetivo

Observabilidade no TeachGram Pro deve ajudar em tres coisas:

- Diagnosticar problemas durante desenvolvimento.
- Mostrar maturidade tecnica no portfolio.
- Monitorar minimamente a demo publica em free tier.

Nao precisa ser uma stack corporativa completa, mas deve ser coerente.

## Componentes

Backend:

- Spring Boot Actuator.
- Micrometer.
- Logs estruturados.
- Correlation ID.
- Health checks.

Local:

- Prometheus.
- Grafana.
- Docker Compose.

Demo:

- Logs do Render.
- Health endpoint.
- Metricas basicas via Actuator quando seguro.

## Health Checks

Endpoints:

- `/actuator/health`
- `/actuator/info`

Health deve verificar:

- Aplicacao.
- Banco.
- Redis, se configurado.
- Storage, se implementado.

Politica:

- Health publico pode retornar apenas `UP`/`DOWN`.
- Detalhes completos apenas em ambiente local ou protegido.

Exemplo publico:

```json
{
  "status": "UP"
}
```

Exemplo local:

```json
{
  "status": "UP",
  "components": {
    "db": { "status": "UP" },
    "redis": { "status": "UP" }
  }
}
```

## Logs

Formato recomendado:

- Timestamp.
- Level.
- Logger.
- Trace/correlation id.
- User id quando autenticado.
- Method.
- Path.
- Status.
- Duration.

Exemplo conceitual:

```json
{
  "timestamp": "2026-05-13T18:30:00Z",
  "level": "INFO",
  "traceId": "8e7f2f4e2f5a4a0e",
  "userId": "4acfc4b4-cc48-44e5-94e1-d625f1e8d5a1",
  "method": "POST",
  "path": "/api/v1/posts",
  "status": 201,
  "durationMs": 42
}
```

Nao logar:

- Senha.
- Token.
- Refresh token.
- API keys.
- Prompt completo com dados sensiveis.

## Correlation ID

Regra:

- Se request vier com `X-Request-Id`, reutilizar.
- Se nao vier, gerar.
- Incluir em response.
- Incluir em logs.

Beneficio:

- Quando o frontend mostrar erro, pode exibir codigo curto para debug.

Mensagem de UI:

```text
Nao conseguimos concluir a acao. Codigo: 8e7f2f4e2f5a4a0e
```

## Metricas

Metricas essenciais:

- Requests por endpoint.
- Latencia por endpoint.
- Erros 4xx/5xx.
- Logins com sucesso/falha.
- Posts criados.
- Likes criados.
- Comentarios criados.
- Chamadas de IA.
- Falhas de IA.
- Fallbacks de IA.
- Cache hit/miss.

Nomes conceituais:

```text
teachgram.auth.login.success
teachgram.auth.login.failure
teachgram.posts.created
teachgram.feed.generated
teachgram.ai.requests
teachgram.ai.fallbacks
teachgram.notifications.created
```

## Dashboards Locais

Dashboard 1: API Overview

- Requests por minuto.
- Latencia p95.
- Taxa de erro.
- Endpoints mais chamados.

Dashboard 2: Social Activity

- Posts criados.
- Likes.
- Comentarios.
- Follows.
- Notificacoes.

Dashboard 3: AI

- Requests por tipo.
- Latencia media.
- Fallbacks.
- Erros por provider.
- Rate limits.

Dashboard 4: Infra

- Banco UP/DOWN.
- Redis UP/DOWN.
- Uso de conexoes.
- Tempo de resposta do health.

## Alertas

Em free tier, alertas podem ser manuais/simples.

Sugestao:

- GitHub Actions ou UptimeRobot opcional pingando health.
- Alertar se health ficar DOWN.
- Alertar se Render suspender por limite.

Nao depender disso para nota do curso, mas e bom para portfolio.

## Auditoria De Negocio

`audit_events` complementa logs tecnicos.

Eventos:

- `USER_REGISTERED`
- `USER_LOGIN_SUCCEEDED`
- `USER_LOGIN_FAILED`
- `USER_DELETED`
- `POST_CREATED`
- `POST_DELETED`
- `POST_PRIVACY_CHANGED`
- `FOLLOW_CREATED`
- `AI_CAPTION_REQUESTED`
- `CONTENT_MODERATED`

Uso:

- Debug.
- Historico.
- Demonstrar responsabilidade com acoes sensiveis.

## Observabilidade De IA

Registrar:

- Provider.
- Modelo.
- Tipo da chamada.
- Latencia.
- Status.
- Fallback usado.
- Codigo de erro.

Nao registrar:

- Prompt completo.
- API key.
- Dados pessoais.

Metricas:

- `ai_requests_total`
- `ai_failures_total`
- `ai_fallbacks_total`
- `ai_latency_ms`

## Observabilidade No Frontend

Minimo:

- Mostrar mensagens de erro amigaveis.
- Logar erros no console apenas em desenvolvimento.
- Ter boundary de erro global.

Opcional:

- Vercel Web Analytics se estiver dentro da cota/necessidade.
- Sentry free tier, se quiser adicionar depois.

Eventos de UI uteis:

- Login falhou.
- Criar post falhou.
- IA indisponivel.
- Feed nao carregou.

## Actuator

Endpoints recomendados:

Local:

```text
health
info
metrics
prometheus
loggers
```

Demo:

```text
health
info
```

`prometheus` na demo somente se protegido ou se nao expuser dados sensiveis.

## Info Endpoint

Exemplo:

```json
{
  "app": {
    "name": "TeachGram Pro",
    "version": "1.0.0",
    "environment": "demo"
  },
  "build": {
    "commit": "abc123",
    "time": "2026-05-13T18:30:00Z"
  }
}
```

## Runbook Basico

### API Nao Responde

Verificar:

1. Render service status.
2. Logs recentes.
3. `/actuator/health`.
4. Conexao com Supabase.
5. Variaveis de ambiente.

### Login Falhando

Verificar:

1. Usuario existe.
2. `status = ACTIVE`.
3. Hash de senha.
4. `JWT_SECRET`.
5. Relogio/expiracao de token.

### Feed Vazio

Verificar:

1. Usuario segue alguem.
2. Existem posts publicos.
3. Posts nao estao deletados.
4. Privacidade.
5. Cache Redis.
6. Query de cursor.

### IA Falhando

Verificar:

1. `AI_ENABLED`.
2. `GROQ_API_KEY`.
3. Rate limit.
4. Timeout.
5. Logs `ai_jobs`.
6. Fallback funcionando.

## Criterios De Aceite

- Health endpoint responde localmente.
- Logs possuem correlation id.
- Erros retornam traceId para o cliente.
- Falhas de IA aparecem em `ai_jobs`.
- Pelo menos um dashboard local documentado no README/diagrams.
- Actuator demo nao expoe informacao sensivel.

