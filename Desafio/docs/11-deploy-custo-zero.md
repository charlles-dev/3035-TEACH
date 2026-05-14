# 11 - Deploy Custo Zero

## Objetivo

Publicar o TeachGram Pro com link acessivel para portfolio usando cotas gratuitas. O deploy deve ser honesto sobre limites: funciona para demonstracao, nao para producao real com alto trafego.

## Stack De Deploy

| Parte | Plataforma | Motivo |
| --- | --- | --- |
| Frontend | Vercel Hobby | Deploy simples de React/Vite, HTTPS e CI/CD |
| Backend | Render Free | Web service Java acessivel por URL publica |
| Banco | Supabase Free | PostgreSQL gerenciado e storage |
| Cache/rate limit | Upstash Redis Free | Redis serverless com REST e baixa configuracao |
| IA | Groq Cloud API | `groq/compound`, API OpenAI-compatible e fallback local |

Referencias oficiais:

- [Vercel Account Plans](https://vercel.com/docs/accounts/plans)
- [Render Deploy for Free](https://render.com/docs/free)
- [Supabase Pricing](https://supabase.com/pricing)
- [Supabase Storage Pricing](https://supabase.com/docs/guides/storage/pricing)
- [Upstash Redis Pricing](https://upstash.com/pricing/redis)
- [Groq Compound](https://console.groq.com/docs/compound)
- [Groq OpenAI compatibility](https://console.groq.com/docs/openai)

## Limitacoes Importantes

### Vercel Hobby

Caracteristicas:

- Plano gratuito para projetos pessoais.
- CI/CD integrado.
- HTTPS automatico.
- Uso sujeito a limites do plano.

Riscos:

- Projeto pode ser pausado ao exceder uso incluido.
- Recursos de time/empresa podem exigir plano pago.

Mitigacao:

- Frontend estatico com chamadas eficientes.
- Evitar assets gigantes.
- Usar imagens otimizadas quando possivel.

### Render Free

Caracteristicas:

- Web services gratuitos para preview/hobby.
- Render informa que instancias free podem dormir apos inatividade.
- Filesystem e efemero.

Riscos:

- Cold start de cerca de um minuto.
- Servico pode suspender ao estourar limites.
- Arquivos locais somem em redeploy/spin down.

Mitigacao:

- Mostrar aviso no README: primeira chamada pode demorar.
- Nao salvar uploads no filesystem do backend.
- Usar Supabase Storage para midia.
- Health check simples.

### Supabase Free

Caracteristicas:

- PostgreSQL gerenciado.
- Storage.
- Auth disponivel, embora o projeto use Spring Security.
- Limites de database/storage/egress.
- Projetos free podem pausar por inatividade conforme regras atuais da plataforma.

Riscos:

- Banco pequeno.
- Inatividade pausa projeto.
- Sem SLA de producao.

Mitigacao:

- Seed pequeno.
- Limpar dados antigos se necessario.
- Usar indices corretamente.
- Nao guardar midias pesadas.

### Upstash Redis Free

Caracteristicas:

- Plano gratuito para prototipos.
- Limites de storage, comandos e bandwidth.

Riscos:

- Estourar comandos com cache mal desenhado.
- Latencia por ser serverless/remoto.

Mitigacao:

- TTL curto.
- Cache apenas em pontos de alto valor.
- Rate limit simples.
- App funciona sem cache em modo degradado.

### Groq Compound

Caracteristicas:

- API OpenAI-compatible com base URL `https://api.groq.com/openai/v1`.
- `groq/compound` usa multiplas chamadas de ferramenta por requisicao.
- `groq/compound-mini` e recomendado para respostas mais rapidas e tarefas simples.

Riscos:

- Cota pode acabar.
- Modelo pode mudar/disponibilidade pode variar.
- Latencia externa.

Mitigacao:

- `AiProvider` plugavel.
- Fallback obrigatorio.
- Rate limit por usuario.
- Timeout curto.
- Variavel `AI_ENABLED=false` para desligar sem quebrar app.

## Variaveis De Ambiente

### Backend

```text
SPRING_PROFILES_ACTIVE=demo
SERVER_PORT=8080

DATABASE_URL=jdbc:postgresql://...
DATABASE_USERNAME=...
DATABASE_PASSWORD=...

JWT_SECRET=...
JWT_ACCESS_TOKEN_TTL=PT15M
JWT_REFRESH_TOKEN_TTL=P30D

CORS_ALLOWED_ORIGINS=https://teachgram-pro.vercel.app

REDIS_ENABLED=true
REDIS_URL=...
REDIS_TOKEN=...

AI_ENABLED=true
AI_PROVIDER=groq
GROQ_API_KEY=...
GROQ_BASE_URL=https://api.groq.com/openai/v1
GROQ_TEXT_MODEL=groq/compound
GROQ_FAST_MODEL=groq/compound-mini
GROQ_MODEL_VERSION=
AI_TIMEOUT_MS=8000

SUPABASE_STORAGE_ENABLED=true
SUPABASE_URL=...
SUPABASE_SERVICE_ROLE_KEY=...
SUPABASE_PUBLIC_BUCKET=teachgram-media
```

### Frontend

```text
VITE_API_BASE_URL=https://teachgram-pro-api.onrender.com/api/v1
VITE_APP_NAME=TeachGram Pro
VITE_DEMO_EMAIL=demo@teachgram.pro
```

Nunca expor no frontend:

- `JWT_SECRET`
- `GROQ_API_KEY`
- `SUPABASE_SERVICE_ROLE_KEY`
- Senhas reais.

## Banco Supabase

Passos:

1. Criar projeto Supabase.
2. Copiar connection string do Postgres.
3. Configurar migrations Flyway no backend.
4. Criar bucket `teachgram-media` se upload for implementado.
5. Configurar policies de storage com cuidado.

Recomendacao:

- Para demo simples, imagens externas sao suficientes.
- Para portfolio mais forte, usar Supabase Storage para upload.

## Backend Render

Build command:

```bash
cd backend && ./mvnw clean package -DskipTests
```

Start command:

```bash
java -jar backend/target/*.jar
```

Observacoes:

- Render injeta `PORT`; Spring deve usar essa porta se necessario.
- Configurar health check path: `/actuator/health`.
- Nao depender de arquivo local persistente.

## Frontend Vercel

Config:

- Framework: Vite.
- Root directory: `frontend`.
- Build command: `npm run build`.
- Output directory: `dist`.

Variavel:

- `VITE_API_BASE_URL`.

## Redis Upstash

Uso:

- Rate limit.
- Cache curto.

Configuracao:

- Criar database Redis.
- Copiar REST URL/token ou Redis URL.
- Guardar no Render como secret.

## IA Groq

Uso:

- Criar API key no GroqCloud.
- Configurar variavel no Render.
- Testar endpoint localmente antes do deploy.

Regras:

- Nao chamar Groq do frontend.
- Toda chamada passa pelo backend.
- Backend aplica rate limit.
- Backend registra `ai_jobs`.

## CI/CD

Fluxo recomendado:

1. Push no GitHub.
2. GitHub Actions roda testes.
3. Vercel deploya frontend automaticamente.
4. Render deploya backend automaticamente.
5. Smoke test manual ou automatizado valida demo.

Branches:

- `main`: demo estavel.
- `develop`: integracao.
- `feature/*`: trabalho local.

## Checklist Pre-Deploy

- `README.md` com links reais.
- `.env.example` criado.
- Migrations executam localmente.
- Seed demo funciona.
- Swagger abre localmente.
- Health check local UP.
- Frontend aponta para API correta.
- CORS contem dominio Vercel.
- Render com variaveis configuradas.
- Supabase com banco ativo.
- Upstash configurado.
- IA com fallback testado.

## Checklist Pos-Deploy

- Abrir frontend.
- Login demo.
- Carregar feed.
- Criar post.
- Curtir post.
- Comentar post.
- Abrir perfil.
- Seguir usuario.
- Gerar legenda com IA ou fallback.
- Abrir Swagger.
- Abrir health check.
- Conferir logs do Render.

## Mensagem Honesta Para README

Texto recomendado:

```text
Esta demo usa free tiers. A primeira chamada ao backend pode demorar por cold start do Render, e recursos de IA podem cair para fallback quando a cota gratuita/trial estiver indisponivel.
```

Isso nao enfraquece o projeto. Pelo contrario: mostra maturidade e transparencia.
