# Infraestrutura Local

Esta pasta contem os arquivos de apoio para rodar o TeachGram Pro localmente e publicar a demo.

## Local

```bash
cd infra
docker compose up -d
```

Servicos:

- PostgreSQL: `localhost:55432`
- Redis: `localhost:6379`
- RabbitMQ: `localhost:5672`
- RabbitMQ Management: `http://localhost:15672`
- Prometheus: `http://localhost:9090`
- Grafana: `http://localhost:3000`

## Deploy

- `render.yaml`: blueprint inicial para o backend.
- `vercel.json`: configuracao inicial do frontend.
- `github-actions/teachgram-ci.yml.example`: exemplo de pipeline.
