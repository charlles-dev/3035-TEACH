# Estrutura Do Projeto

```text
Desafio/
  README.md
  .env.example
  backend/
    pom.xml
    Dockerfile
    src/main/java/dev/charlles/teachgram/
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
    index.html
    vite.config.ts
    src/
      App.tsx
      main.tsx
      styles/
      assets/figma/
      app/
      components/
      features/
      pages/
      lib/
  infra/
    docker-compose.yml
    render.yaml
    vercel.json
    prometheus/prometheus.yml
    github-actions/teachgram-ci.yml.example
  docs/
```

## Proximo Passo

Comece por [docs/13-guia-implementacao.md](docs/13-guia-implementacao.md), fase 0.5, para implementar a base visual do Figma antes de codar as regras de negocio.

