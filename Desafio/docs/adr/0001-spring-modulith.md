# ADR 0001 - Usar Spring Modulith Em Vez De Microservices

## Status

Aceita.

## Contexto

O TeachGram Pro nasceu de um desafio fullstack com Spring Boot, JPA, PostgreSQL e React. A ideia e evoluir o projeto para portfolio profissional sem perder viabilidade de implementacao e deploy gratuito.

Uma opcao seria quebrar tudo em microservices:

- Auth service.
- Profile service.
- Post service.
- Feed service.
- Notification service.
- AI service.

Isso deixaria o diagrama chamativo, mas aumentaria muito custo operacional, complexidade de deploy, observabilidade, mensageria, versionamento de contratos e debugging.

Como o projeto precisa rodar em free tiers e ser desenvolvido manualmente por uma pessoa, microservices seriam mais demonstracao artificial do que necessidade real.

## Decisao

Implementar o backend como **modular monolith usando Spring Modulith**.

Modulos principais:

- `identity`
- `profiles`
- `posts`
- `media`
- `socialgraph`
- `engagement`
- `feed`
- `notifications`
- `search`
- `ai`
- `audit`
- `shared`

Cada modulo deve ter responsabilidade clara, limites internos e comunicacao preferencial por eventos.

## Consequencias Positivas

- Um unico deploy backend.
- Menor custo.
- Menos pontos de falha.
- Mais facil rodar localmente.
- Testes de integracao mais simples.
- Separacao de dominio ainda fica forte.
- Spring Modulith permite validar dependencias entre modulos.
- Futuro caminho para extrair microservices continua aberto.

## Consequencias Negativas

- Todos os modulos escalam juntos.
- Um bug grave pode afetar o deploy inteiro.
- Nao ha deploy independente por dominio.
- Disciplina modular depende de testes e revisao.

## Alternativas Consideradas

### Microservices Desde O Inicio

Rejeitada.

Motivos:

- Overengineering para uma demo de portfolio.
- Free tier nao sustenta bem varios servicos sempre ativos.
- Exigiria API gateway, service discovery ou configuracao manual complexa.
- Mais dificil demonstrar produto funcionando por link.

### Monolito Sem Modularidade

Rejeitada.

Motivos:

- Ficaria parecido com CRUD academico.
- Dificultaria evolucao.
- Menos interessante para entrevista.

### BaaS Com Supabase Auth E Functions

Rejeitada como arquitetura principal.

Motivos:

- Reduziria protagonismo do Java/Spring Boot.
- Fugiria da stack central do curso.
- Ainda pode ser usado para Postgres e Storage.

## Criterios De Validacao

- Teste `ApplicationModules.of(...).verify()` passa.
- Modulos nao acessam repositories internos de outros modulos.
- Eventos conectam efeitos secundarios.
- README explica por que Modulith foi escolhido.
- O projeto roda com um unico backend deployado no Render.

