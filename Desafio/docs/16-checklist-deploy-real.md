# 16 - Checklist Deploy Real

Este checklist transforma o TeachGram Pro local em uma demo publica pronta para portfolio. Marque item por item antes de divulgar o link.

## 1. Repositorio

- [ ] Revisar `git status` e garantir que somente arquivos do `Desafio/` entram no commit.
- [ ] Conferir `.gitignore`: `node_modules`, `dist`, `target`, logs, `.env` e arquivos `.pid` devem ficar fora do Git.
- [ ] Criar commit com mensagem clara, por exemplo `feat: implement teachgram pro`.
- [ ] Subir para GitHub em um repositorio publico ou privado com acesso para avaliacao.
- [ ] Confirmar que nenhum secret real foi commitado.

## 2. Backend Local Antes Do Cloud

- [ ] Rodar `cd backend && ./mvnw test`.
- [ ] Rodar `cd backend && ./mvnw -DskipTests package`.
- [ ] Rodar `cd infra && docker compose config --quiet`.
- [ ] Subir `docker compose up -d postgres redis`.
- [ ] Abrir `http://localhost:8081/actuator/health` e confirmar `UP`.
- [ ] Fazer login demo com `demo@teachgram.pro` e `Demo@123456`.
- [ ] Confirmar feed com posts seed.

## 3. Supabase

- [x] Criar projeto Supabase.
- [x] Copiar connection string PostgreSQL.
- [x] Definir usuario, senha e host para Render.
- [x] Opcional: criar bucket `teachgram-media`.
- [x] Opcional: definir policy de leitura publica para midias de demo.
- [ ] Testar conexao local apontando `DATABASE_URL` para Supabase.

## 4. Upstash Redis

- [x] Criar database Redis.
- [x] Copiar `REDIS_URL` ou credenciais equivalentes.
- [ ] Configurar TTL/rate limit no backend se for usar Redis remoto.
- [ ] Validar que o app funciona sem Redis caso a cota falhe.

## 5. Groq AI

- [ ] Criar API key no GroqCloud.
- [x] Configurar no Render: `GROQ_API_KEY`.
- [x] Configurar `AI_PROVIDER=groq`.
- [x] Configurar `GROQ_BASE_URL=https://api.groq.com/openai/v1`.
- [x] Configurar `GROQ_TEXT_MODEL=groq/compound`.
- [x] Configurar `GROQ_FAST_MODEL=groq/compound-mini`.
- [x] Manter `AI_FALLBACK_ENABLED=true`.
- [ ] Testar `/api/v1/ai/caption` com chave real.
- [ ] Testar o mesmo endpoint sem chave ou com chave invalida e confirmar fallback sem `500`.

## 6. Render Backend

- [ ] Criar Web Service no Render via Blueprint (usar `infra/render.yaml`).
- [x] Root directory: `Desafio/backend` (configurado no `render.yaml`).
- [x] Build via Docker: `Dockerfile` na raiz do backend.
- [x] Start command: `java -Dserver.port=${PORT:-8080} -jar app.jar` (via Dockerfile).
- [x] Health check path: `/actuator/health` (configurado no `render.yaml`).
- [x] Definir `SPRING_PROFILES_ACTIVE=demo` (configurado no `render.yaml`).
- [ ] Definir `JWT_SECRET` forte no painel do Render (minimo 32 caracteres).
- [ ] Definir `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD` no painel do Render.
- [ ] Definir `REDIS_URL` no painel do Render.
- [ ] Definir `GROQ_API_KEY` no painel do Render.
- [ ] Definir `CORS_ALLOWED_ORIGINS` com a URL final da Vercel.
- [ ] Fazer deploy e confirmar logs sem erro de Flyway/JPA.
- [ ] Abrir `/actuator/health` publico e confirmar `UP`.

## 7. Vercel Frontend

- [ ] Criar projeto na Vercel apontando para o repositorio.
- [x] Root directory: `Desafio/frontend` (configurado no `vercel.json`).
- [x] Framework: Vite (configurado no `vercel.json`).
- [x] Build command: `npm run build` (configurado no `vercel.json`).
- [x] Output directory: `dist` (configurado no `vercel.json`).
- [ ] Definir `VITE_API_BASE_URL=https://<url-do-render>/api/v1` no painel da Vercel.
- [x] Definir `VITE_DEMO_EMAIL=demo@teachgram.pro`.
- [ ] Fazer deploy e abrir a URL publica.

## 8. Smoke Test Publico

- [ ] Abrir frontend publico.
- [ ] Fazer login demo.
- [ ] Carregar feed.
- [ ] Criar post com texto e URL de imagem.
- [ ] Curtir e descurtir post.
- [ ] Comentar post.
- [ ] Salvar e remover post salvo.
- [ ] Buscar usuario/post.
- [ ] Abrir notificacoes.
- [ ] Abrir perfil publico.
- [ ] Seguir usuario.
- [ ] Gerar legenda com Groq ou fallback.
- [ ] Confirmar que erro de IA nao impede criar post manualmente.
- [ ] Abrir Swagger publico.

## 9. Seguranca E Privacidade

- [ ] Confirmar que `GROQ_API_KEY`, `JWT_SECRET` e senhas nao aparecem no frontend.
- [ ] Confirmar CORS restrito ao dominio da Vercel.
- [ ] Confirmar cookies seguros em producao.
- [ ] Confirmar que actuator expoe somente `health` e `info`.
- [ ] Confirmar que logs nao imprimem tokens, senhas ou prompts completos.
- [ ] Revisar endpoints que precisam de auth.
- [ ] Fazer uma revisao manual de rate limit de login e IA.

## 10. README Final

- [ ] Trocar links placeholder por URLs reais.
- [ ] Adicionar aviso de cold start do Render.
- [ ] Explicar que IA usa Groq com fallback.
- [ ] Manter credenciais demo visiveis.
- [ ] Adicionar screenshots se quiser deixar o portfolio mais forte.

## Status Esperado Para Divulgacao

O projeto esta pronto para divulgar quando todos estes itens estiverem verdadeiros:

- [ ] Frontend publico abre.
- [ ] Backend publico responde `UP`.
- [ ] Login demo funciona.
- [ ] Feed e criacao de post funcionam.
- [ ] IA real ou fallback funciona.
- [ ] Nenhum secret foi commitado.
- [ ] README tem links reais.
