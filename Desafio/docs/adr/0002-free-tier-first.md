# ADR 0002 - Arquitetura Compatível Com Custo Zero

## Status

Aceita.

## Contexto

O projeto deve ser acessivel por link no portfolio, mas sem custo mensal. Isso significa usar planos gratuitos e cotas de plataformas, aceitando limitacoes como cold start, pausas por inatividade, limites de storage, limites de comandos e ausencia de SLA.

Servicos escolhidos:

- Vercel Hobby para frontend.
- Render Free para backend.
- Supabase Free para PostgreSQL e Storage.
- Upstash Redis Free para cache/rate limit.
- Groq Cloud API para IA com cota/trial e fallback.

## Decisao

Desenhar o sistema como **free-tier-first**:

- O core social funciona sem IA.
- Cache melhora desempenho, mas banco continua fonte de verdade.
- Upload nao depende do filesystem do backend.
- Backend aceita cold start.
- Recursos externos tem fallback.
- README declara limitacoes honestamente.

## Consequencias Positivas

- Projeto pode ficar online sem custo mensal.
- Recrutador consegue acessar pelo portfolio.
- Arquitetura mostra consciencia de operacao real.
- Limites viram oportunidade de explicar trade-offs.

## Consequencias Negativas

- Sem SLA.
- Cold start no backend.
- Banco/storage limitados.
- IA pode falhar por cota.
- Performance online pode variar.

## Alternativas Consideradas

### Rodar Tudo Local E Mostrar Prints

Rejeitada.

Motivo:

- Menos impacto para portfolio. Um link publico chama mais atencao.

### Usar Cloud Paga

Rejeitada.

Motivo:

- O requisito do projeto e custo zero.

### Kubernetes Em Producao

Rejeitada.

Motivo:

- Custo e complexidade desnecessarios para demo.
- Kubernetes pode aparecer em laboratorio local ou roadmap.

## Regras Derivadas

- Nenhuma feature critica pode depender exclusivamente de servico com cota instavel.
- Toda integracao externa deve ter timeout.
- Toda integracao de IA deve ter fallback.
- Midia nao deve ser salva no filesystem do Render.
- Seed demo deve ser pequeno.
- Cache deve ter TTL curto.

## Criterios De Validacao

- App funciona quando `AI_ENABLED=false`.
- App funciona quando Redis esta indisponivel, ainda que mais lento.
- Upload usa URL externa ou storage persistente.
- README avisa sobre cold start.
- Demo publica executa fluxo principal.

