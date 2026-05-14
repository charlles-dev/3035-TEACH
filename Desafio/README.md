# TeachGram Pro

TeachGram Pro e uma rede social fullstack inspirada no desafio final da Teach 3035, evoluida para um projeto de portfolio profissional. A proposta e manter a ideia original, uma API e uma interface de rede social com usuarios, posts, login, feed e amigos, mas com arquitetura, documentacao e criterios de qualidade proximos de um produto real.

O objetivo deste repositorio e entregar uma implementacao fullstack funcional, acompanhada de documentacao completa para evolucao, apresentacao em portfolio e deploy gratuito quando as credenciais forem configuradas.

## Links Do Projeto

- Demo publica: `https://teachgram-pro.vercel.app` quando publicada.
- API publica: `https://teachgram-pro-api.onrender.com` quando publicada.
- Swagger/OpenAPI: `https://teachgram-pro-api.onrender.com/swagger-ui/index.html` quando publicado.
- Documentacao tecnica: [docs/00-visao-geral.md](docs/00-visao-geral.md).

## Stack Recomendada

Backend:

- Java 21.
- Spring Boot 3.x.
- Maven.
- Spring Security com JWT.
- Spring Data JPA.
- Spring Modulith.
- PostgreSQL.
- Redis via Upstash no deploy e Redis local no Docker Compose.
- Flyway para migrations.
- OpenAPI/Swagger.
- Micrometer + Actuator.

Frontend:

- React.
- TypeScript.
- Vite.
- React Router.
- TanStack Query.
- React Hook Form + Zod.
- CSS com tokens visuais extraidos do Figma.
- Playwright para testes E2E.

Infra e deploy custo zero:

- Vercel Hobby para frontend.
- Render Free para backend.
- Supabase Free para PostgreSQL e Storage.
- Upstash Redis Free para cache e rate limit.
- Groq Cloud API com `groq/compound` como provider de IA, sempre com fallback local.

## Diferenciais Para Portfolio

- Arquitetura modular usando Spring Modulith, com modulos de dominio bem separados.
- Feed com cursor pagination, cache, eventos internos e possibilidade de fanout.
- Social graph com follows/amigos e regras de privacidade.
- IA plugavel para legenda, hashtags, bio, moderacao e sugestoes.
- Observabilidade com logs estruturados, health checks, metricas e dashboards locais.
- Testes unitarios, integracao, seguranca, contrato e E2E.
- ADRs explicando decisoes arquiteturais.
- Deploy publico em free tiers com limitacoes documentadas.

## Escopo Funcional

O TeachGram Pro deve permitir:

- Criar conta, login, refresh de token e logout.
- Editar perfil, bio, foto e dados de contato.
- Criar, editar, excluir e alterar privacidade de posts.
- Exibir feed cronologico com posts publicos e posts de pessoas conectadas.
- Curtir, comentar e salvar posts.
- Seguir ou adicionar usuarios como amigos, dependendo da regra escolhida.
- Listar perfis, amigos, posts e notificacoes com paginacao.
- Usar IA para melhorar conteudo sem quebrar o fluxo principal quando a cota acabar.

## Estrutura Da Documentacao

- [Visao geral](docs/00-visao-geral.md)
- [Requisitos funcionais](docs/01-requisitos-funcionais.md)
- [Arquitetura](docs/02-arquitetura.md)
- [Modelagem de dados](docs/03-modelagem-dados.md)
- [API REST](docs/04-api-rest.md)
- [Frontend](docs/05-frontend.md)
- [Feed e social graph](docs/06-feed-social-graph.md)
- [IA](docs/07-ia.md)
- [Seguranca](docs/08-seguranca.md)
- [Observabilidade](docs/09-observabilidade.md)
- [Testes](docs/10-testes.md)
- [Deploy custo zero](docs/11-deploy-custo-zero.md)
- [Roadmap](docs/12-roadmap.md)
- [Guia de implementacao](docs/13-guia-implementacao.md)
- [Design Figma oficial](docs/14-design-figma.md)
- [Telas Pro derivadas](docs/15-telas-pro.md)
- [Checklist deploy real](docs/16-checklist-deploy-real.md)
- [ADRs](docs/adr)
- [Diagramas](docs/diagrams/README.md)
- [Estrutura do projeto](PROJECT_STRUCTURE.md)

## Design Oficial

O frontend deve seguir o Figma oficial do desafio:

```text
Teach-3035---Desafio-Front-end-final
https://www.figma.com/design/BNgj8foFAiNUa2EkoRk260/Teach-3035---Desafio-Front-end-final?node-id=0-1
```

As telas existentes no Figma sao a fonte de verdade para autenticacao, feed, amigos, perfil, criacao de publicacao e configuracoes. As telas novas do TeachGram Pro, como busca, notificacoes, salvos e assistente de IA, devem seguir a mesma paleta, os mesmos cards, a mesma navegacao e a mesma hierarquia visual. A especificacao completa esta em [docs/14-design-figma.md](docs/14-design-figma.md) e [docs/15-telas-pro.md](docs/15-telas-pro.md).

## Como Rodar Localmente

Suba a infraestrutura local:

```bash
cd infra
docker compose up -d
```

Em outro terminal, suba o backend:

```bash
cd backend
./mvnw spring-boot:run
```

No Windows PowerShell, use `.\mvnw.cmd spring-boot:run`.

Em outro terminal, suba o frontend:

```bash
cd frontend
npm ci
npm run dev
```

URLs locais:

- Frontend: `http://localhost:5173`
- Backend: `http://localhost:8081`
- Swagger: `http://localhost:8081/swagger-ui/index.html`
- Actuator health: `http://localhost:8081/actuator/health`
- Prometheus: `http://localhost:9090`
- Grafana: `http://localhost:3000`

## Verificacoes Locais

```bash
cd backend
./mvnw test
./mvnw -DskipTests package

cd ../frontend
npm audit --audit-level=high
npm run build

cd ../infra
docker compose config --quiet
```

## Usuario Demo

Para facilitar avaliacao por recrutadores, o ambiente de demo deve possuir seed controlado:

```text
email: demo@teachgram.pro
senha: Demo@123456
perfil: @demo
```

O usuario demo deve ter permissao normal, posts, conexoes e notificacoes suficientes para mostrar o produto sem exigir cadastro.

## Referencias Oficiais De Infra

- [Vercel Hobby plan](https://vercel.com/docs/accounts/plans)
- [Render free instances](https://render.com/docs/free)
- [Supabase pricing](https://supabase.com/pricing)
- [Upstash Redis pricing](https://upstash.com/pricing/redis)
- [Groq Compound](https://console.groq.com/docs/compound)
- [Groq OpenAI compatibility](https://console.groq.com/docs/openai)
