# 00 - Visao Geral

## Proposta

TeachGram Pro e uma evolucao do desafio final fullstack da Teach 3035. O desafio base pede uma rede social simples com Java, Spring Boot, PostgreSQL, JPA, login, usuarios, posts, relacionamento usuario-post, feed, amigos e interface React.

A versao Pro preserva esse nucleo, mas transforma o projeto em um produto demonstravel para portfolio:

- O backend continua sendo Java/Spring Boot para respeitar a proposta do curso.
- A arquitetura passa a ser modular, testavel e documentada.
- A interface continua seguindo o visual do Figma do desafio, mas ganha fluxos completos.
- O deploy fica publico e acessivel por link.
- Recursos avancados, como IA, cache, observabilidade e eventos, aparecem sem exigir custo mensal.

## Narrativa Para Portfolio

Descricao curta:

> TeachGram Pro e uma rede social fullstack com Spring Boot, React TypeScript, PostgreSQL, Redis e IA plugavel, construida com arquitetura modular, feed social, seguranca JWT, observabilidade e deploy em free tiers.

Descricao para entrevista:

> O projeto nasceu de um desafio academico simples e foi evoluido para um sistema com preocupacoes reais de produto: separacao modular de dominio, autenticacao segura, modelagem social, feed paginado, cache, eventos internos, IA com fallback, testes automatizados, documentacao de arquitetura e deploy publico. A ideia foi mostrar dominio da stack ensinada no curso e tambem maturidade para tomar decisoes de engenharia.

## Objetivos

Objetivos principais:

- Entregar uma rede social funcional acessivel por link.
- Mostrar dominio de Java, Spring Boot, JPA, Spring Security e React TypeScript.
- Demonstrar capacidade de arquitetura alem do CRUD.
- Manter custo operacional zero usando cotas gratuitas.
- Permitir implementacao manual por fases, sem decisoes grandes pendentes.
- Servir como conversa tecnica em entrevistas.

Objetivos secundarios:

- Criar documentacao forte para GitHub.
- Deixar claro o que e demo, o que e producao real e o que precisaria mudar para escala.
- Ter uma base que possa evoluir para microservices no futuro, sem comecar com complexidade desnecessaria.

## Publico-Alvo

Usuarios do produto:

- Pessoas que querem postar fotos, videos, textos curtos e interagir com amigos.
- Visitantes/recrutadores que acessam a demo para entender o projeto rapidamente.

Publico tecnico:

- Recrutadores.
- Tech leads.
- Professores/avaliadores.
- Outros devs lendo o GitHub.

## Principios Do Produto

- O feed deve carregar rapido e ser navegavel.
- O usuario demo deve conseguir mostrar o app sem cadastro.
- A IA deve ajudar a criar conteudo, mas nunca bloquear o uso principal.
- A interface deve parecer produto, nao tela de CRUD.
- Erros devem ser claros e previsiveis.
- O projeto deve ser facil de rodar localmente.

## Principios De Arquitetura

- Modularidade antes de microservices.
- Dominios separados por responsabilidade.
- Banco relacional como fonte de verdade.
- Redis como acelerador, nao como fonte primaria.
- Eventos internos para desacoplar modulos.
- IA isolada atras de uma interface de provider.
- Deploy gratis com degradacao consciente.
- Observabilidade desde o inicio.

## Escopo Do Produto

Inclui:

- Cadastro e login.
- Perfil de usuario.
- Feed.
- Posts com foto/video/link.
- Comentarios.
- Curtidas.
- Salvamentos.
- Seguir/adicionar amigos.
- Busca.
- Notificacoes.
- IA para sugestoes e moderacao.
- Admin tecnico minimo via logs, metricas e Swagger.

Nao inclui na primeira versao:

- Chat em tempo real.
- Stories.
- Reels com processamento de video.
- Sistema de anuncios.
- Pagamentos.
- App mobile nativo.
- Recomendacao com machine learning treinado proprio.
- Kubernetes em producao.

Esses pontos podem aparecer no roadmap como evolucao, mas nao devem bloquear a versao de portfolio.

## Definicao De Sucesso

O projeto e considerado bem-sucedido quando:

- Um recrutador consegue acessar a demo por link.
- O usuario demo consegue navegar pelo feed, abrir perfil, criar post, curtir, comentar e seguir alguem.
- A IA consegue sugerir legenda/hashtags quando a cota esta disponivel.
- Se a IA falhar, o usuario recebe feedback e continua usando o app.
- O README explica claramente arquitetura, stack, trade-offs e como rodar.
- O Swagger documenta os endpoints principais.
- Os testes principais passam localmente e no CI.
- O deploy deixa claro limites de free tier.

## Como Ler Esta Documentacao

Ordem recomendada:

1. [Requisitos funcionais](01-requisitos-funcionais.md)
2. [Arquitetura](02-arquitetura.md)
3. [Modelagem de dados](03-modelagem-dados.md)
4. [API REST](04-api-rest.md)
5. [Frontend](05-frontend.md)
6. [Design Figma Oficial](14-design-figma.md)
7. [Telas Pro Derivadas](15-telas-pro.md)
8. [Guia de implementacao](13-guia-implementacao.md)

Para entrevista, leia tambem:

- [ADRs](adr)
- [Diagramas](diagrams/README.md)
- [Deploy custo zero](11-deploy-custo-zero.md)
