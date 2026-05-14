# ADR 0003 - IA Plugavel Com Fallback

## Status

Aceita.

## Contexto

O projeto deve usar IA para ficar mais chamativo e demonstrar conhecimento atual, mas precisa permanecer gratuito e estavel para portfolio. Endpoints gratuitos ou com cota podem mudar, limitar requisicoes ou ficar indisponiveis.

A decisao atual e usar Groq Cloud diretamente como provider principal, com `groq/compound` para geracao e `groq/compound-mini` para tarefas mais rapidas. A Groq expõe uma API compativel com OpenAI em `https://api.groq.com/openai/v1`, o que reduz atrito tecnico e facilita troca futura de provider.

## Decisao

Criar uma camada de IA plugavel:

- Interface `AiProvider`.
- Implementacao `GroqCompoundAiProvider`.
- Implementacao `FallbackAiProvider`.
- Composicao `CompositeAiProvider`.
- Persistencia em `ai_jobs`.
- Moderacao em `moderation_events`.

Nenhuma tela deve depender de IA para completar o fluxo principal.

## Consequencias Positivas

- Trocar Groq por outro provider fica simples.
- App continua funcionando sem chave.
- Rate limit e timeout ficam centralizados.
- Fallback evita demo quebrada.
- `ai_jobs` gera material tecnico para entrevista.

## Consequencias Negativas

- Mais codigo que chamar API direto.
- Precisa tratar output invalido.
- Respostas de fallback sao menos impressionantes.
- Exige testes especificos.

## Alternativas Consideradas

### Chamar Groq Direto No Frontend

Rejeitada.

Motivos:

- Exporia API key.
- Dificultaria rate limit.
- Nao permitiria auditoria.

### Acoplar Backend A Um Modelo Especifico

Rejeitada.

Motivos:

- Provider/modelo pode mudar.
- Dificulta testes.
- Dificulta fallback.

### Remover IA

Rejeitada.

Motivo:

- IA e diferencial forte para portfolio, desde que bem limitada.

## Regras Derivadas

- Toda chamada de IA passa pelo backend.
- Toda chamada tem timeout.
- Toda chamada registra job.
- Toda chamada tem fallback.
- Prompt nao deve conter senha, token, email ou telefone.
- Frontend sempre permite edicao manual.

## Criterios De Validacao

- `/ai/caption` retorna sugestao real ou fallback.
- Provider falhando nao retorna 500 no fluxo normal.
- Rate limit retorna `429`.
- `ai_jobs` registra sucesso, falha e fallback.
- README explica que IA depende de cota gratuita/trial.
