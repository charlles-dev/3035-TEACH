# 15 - Telas Pro Derivadas

## Objetivo

Este documento especifica as telas que nao existem no Figma original, mas sao necessarias para o TeachGram Pro. Elas devem ser implementadas como extensoes conservadoras do design oficial, usando os tokens e componentes de [14 - Design Figma Oficial](14-design-figma.md).

Regra principal:

- Nao criar uma nova identidade visual.
- Toda tela Pro deve parecer que poderia estar no mesmo arquivo Figma do desafio.

## Principios Para Telas Pro

- Usar fundo branco.
- Usar coral `#F37671` apenas para acao principal, estados ativos e destaques.
- Usar cards brancos com borda `#E2E2E2` ou sombra sutil.
- Usar textos cinza `#8E8E8E` para conteudo secundario.
- Reutilizar sidebar desktop do Feed.
- Reutilizar navegacao mobile do Figma responsivo.
- Preferir containers centrais com largura parecida com o card do Feed.
- Estados vazios devem ser simples e discretos.

## Layout Base Para Desktop Pro

Use o frame `Feed` como referencia:

- Logo no canto superior esquerdo.
- Sidebar vertical a esquerda.
- Conteudo principal centralizado.
- Elementos decorativos coral claro no lado direito quando houver espaco.

Area central recomendada:

```text
left: 441px
width: 558px
```

Quando a tela precisar de mais largura, usar no maximo:

```text
left: 380px
width: 680px
```

Nao ocupar a tela toda com tabelas ou dashboards. O produto e uma rede social.

## Layout Base Para Mobile Pro

Use os frames mobile `Feed`, `Amigos` e `Nova publicacao` como referencia:

- Header compacto.
- Conteudo em coluna unica.
- Cards com largura quase total, respeitando margem lateral.
- Navegacao inferior ou a estrutura mobile ja prevista no Figma.
- Formularios com inputs de 48px de altura.

## `/search` - Busca E Explore

Origem visual:

- Derivar de `Feed`.
- Reutilizar sidebar.
- Reutilizar card/lista de `Amigos` para resultados de usuarios.
- Reutilizar `PostCard` para resultados de posts.

Objetivo:

- Buscar usuarios e posts.
- Servir como Explore leve.
- Mostrar fallback de busca semantica quando IA estiver indisponivel.

Elementos:

- Titulo: `Buscar`.
- Input de busca com placeholder `Busque por usuarios, posts ou hashtags`.
- Filtros: `Tudo`, `Usuarios`, `Posts`.
- Secao `Usuarios`.
- Secao `Posts`.
- Opcional: `Sugestoes para voce`.

Estados:

- Inicial: mostrar sugestoes de perfis e posts recentes.
- Digitando: debounce visual sem travar input.
- Carregando: skeletons de cards.
- Sem resultados: card vazio com texto curto.
- Erro: card com botao `Tentar novamente`.
- Semantica indisponivel: aviso discreto, mantendo busca textual.

Regras:

- Resultado de usuario usa avatar circular, nome, username e botao seguir.
- Resultado de post usa o mesmo `PostCard` compacto.
- Posts privados nao aparecem.
- Usuarios deletados nao aparecem.

Criterios de aceite:

- Busca por username retorna usuario.
- Busca por termo de post retorna posts publicos.
- Busca vazia nao dispara request agressivo.
- Busca semantica falhando nao quebra a tela.

## `/notifications` - Notificacoes

Origem visual:

- Derivar de `Feed` para shell.
- Derivar de cards de sidebar/configuracoes para itens.

Objetivo:

- Listar interacoes sociais e avisos de moderacao/IA.

Elementos:

- Titulo: `Notificacoes`.
- Lista por ordem cronologica.
- Item com avatar/icone, mensagem, tempo e estado lido/nao lido.
- Botao ou acao `Marcar todas como lidas`.

Tipos:

- Novo seguidor.
- Curtida.
- Comentario.
- Post em analise.
- Post ocultado por moderacao.
- IA indisponivel quando relevante.

Estados:

- Loading.
- Lista vazia.
- Erro.
- Nao lidas destacadas com borda ou ponto coral.

Regras:

- Clicar em notificacao leva ao recurso relacionado.
- Marcar como lida remove destaque sem remover item.
- Notificacao de moderacao deve usar texto neutro.

Textos recomendados:

```text
@maria curtiu sua publicacao.
@joao comentou no seu post.
Seu post esta em analise.
Nao conseguimos usar IA agora. Voce ainda pode publicar manualmente.
```

Criterios de aceite:

- Lista notificacoes com paginacao.
- Marcar como lida atualiza UI.
- Notificacao de post abre post.
- Estado vazio aparece quando nao ha notificacoes.

## `/saved` - Posts Salvos

Origem visual:

- Derivar do `Feed`.
- Reutilizar `PostCard`.

Objetivo:

- Mostrar posts salvos pelo usuario.

Elementos:

- Titulo: `Salvos`.
- Lista de posts salvos.
- Estado vazio.

Estados:

- Loading.
- Vazio.
- Erro.
- Post salvo que ficou privado/deletado.

Regra para post indisponivel:

- Nao mostrar conteudo privado/deletado.
- Mostrar card discreto: `Esta publicacao nao esta mais disponivel.`

Criterios de aceite:

- Post salvo aparece.
- Remover save atualiza lista.
- Post inacessivel nao vaza dados.

## `/posts/:postId` - Detalhe De Post E Comentarios

Origem visual:

- Derivar do card de Feed.

Objetivo:

- Mostrar um post com comentarios.

Elementos:

- Post principal.
- Campo de comentario.
- Lista de comentarios.
- Botao voltar.
- Menu de editar/excluir se dono.

Desktop:

- Card central levemente maior que feed se necessario.
- Comentarios abaixo do post no mesmo fluxo.

Mobile:

- Post em coluna unica.
- Campo de comentario fixo no final da area visivel apenas se nao atrapalhar.

Estados:

- Post loading.
- Comentarios loading.
- Post nao encontrado.
- Post privado/inacessivel.
- Comentario vazio.
- Falha ao comentar.

Criterios de aceite:

- Usuario ve comentarios de post acessivel.
- Usuario comenta.
- Dono edita/exclui.
- Terceiro nao ve post privado.

## `/new-post` Com Assistente IA

Origem visual:

- Usar frames `Nova publicacao`.
- Mobile deve seguir `Nova publicacao - link`, `carregamento`, `legenda` e `editar publicacao`.

Objetivo:

- Criar post com suporte de IA sem bloquear publicacao manual.

Elementos base:

- Campo de link/imagem.
- Campo titulo.
- Campo descricao/legenda.
- Privacidade.
- Botao `Publicar`.

Elementos IA:

- Botao `Sugerir legenda`.
- Botao `Gerar hashtags`.
- Estado `Gerando...`.
- Lista de sugestoes.
- Botao `Aplicar`.
- Botao `Descartar`.
- Aviso de fallback.

Estados de IA:

### Idle

Texto:

```text
Use IA para sugerir uma legenda, ou escreva manualmente.
```

### Loading

Texto:

```text
Gerando sugestao...
```

Visual:

- Skeleton ou spinner discreto.
- Nao bloquear edicao manual.

### Sucesso

Mostrar sugestoes em cards pequenos.

Acoes:

- `Aplicar`
- `Copiar`
- `Descartar`

### Fallback

Texto:

```text
IA indisponivel agora. Geramos uma sugestao simples para voce continuar.
```

### Rate limit

Texto:

```text
Voce atingiu o limite gratuito de IA por agora. Continue escrevendo manualmente.
```

### Erro

Texto:

```text
Nao conseguimos gerar a sugestao. Tente novamente mais tarde.
```

Criterios de aceite:

- Usuario publica sem usar IA.
- Usuario aplica sugestao.
- Falha da IA nao impede publicar.
- Rate limit mostra mensagem clara.

## `/ai/studio` - Laboratorio IA Opcional

Status:

- Opcional para portfolio.

Origem visual:

- Derivar de `Configuracoes` e `Nova publicacao`.

Objetivo:

- Mostrar de forma chamativa as capacidades de IA do projeto.

Abas:

- Legenda.
- Hashtags.
- Bio.
- Moderacao.

Regras:

- Nao precisa existir no MVP.
- Se existir, deve usar os mesmos endpoints de IA.
- Deve mostrar provider/fallback de forma transparente.

Criterios:

- Usuario testa geracao de legenda sem criar post.
- Usuario testa hashtags.
- Usuario entende quando fallback foi usado.

## `/forgot-password` - Recuperar Senha

Origem visual:

- Derivar de `Login`, porque o link `Esqueci minha senha` ja existe.

Objetivo:

- Completar o fluxo iniciado pelo Figma.

Elementos:

- Logo Teachgram.
- Titulo: `Recuperar senha`.
- Texto curto.
- Input e-mail.
- Botao `Enviar instrucao`.
- Link `Voltar para login`.
- Imagem lateral no desktop.

Estados:

- E-mail invalido.
- Enviando.
- Sucesso generico.
- Erro.

Texto de sucesso:

```text
Se existir uma conta com esse e-mail, enviaremos as instrucoes.
```

Regra de seguranca:

- Nunca revelar se o e-mail existe.

Criterios de aceite:

- Formulario valida e-mail.
- Sucesso usa mensagem generica.
- Usuario consegue voltar ao login.

## `/demo` - Pagina Demo Opcional

Origem visual:

- Derivar de `Feed`.

Objetivo:

- Ajudar recrutadores a entenderem a demo sem ler README.

Elementos:

- Card central `Modo demo`.
- Credenciais demo.
- Lista curta de fluxos para testar.
- Status do backend.
- Aviso de cold start/free tier.

Texto recomendado:

```text
Esta demo usa free tiers. A primeira chamada pode demorar alguns segundos.
```

Criterios:

- Link para login demo.
- Health status carregado da API.
- Nao expor secrets.

## Estados De Moderacao

Origem visual:

- Derivar de `ConfirmDialog`, cards de feed e notificacoes.

### Post Em Analise

No post card:

```text
Em analise
```

Visual:

- Badge discreto com borda coral clara.
- Post aparece apenas para o dono ate decisao.

### Post Oculto

Texto:

```text
Este post foi ocultado por seguranca. Revise o conteudo e tente novamente.
```

Acoes:

- `Editar post`
- `Excluir`

### Comentario Em Analise

Texto:

```text
Comentario em analise.
```

Criterios:

- Conteudo oculto nao aparece para terceiros.
- Dono recebe feedback claro.
- Notificacao registra o evento.

## Empty States

### Feed Vazio

Texto:

```text
Seu feed ainda esta vazio.
Siga amigos ou crie sua primeira publicacao.
```

Acoes:

- `Encontrar amigos`
- `Criar publicacao`

### Amigos Vazio

Texto:

```text
Voce ainda nao segue ninguem.
```

Acao:

- `Buscar usuarios`

### Busca Sem Resultado

Texto:

```text
Nenhum resultado encontrado.
```

### Notificacoes Vazias

Texto:

```text
Nada novo por aqui.
```

### Salvos Vazio

Texto:

```text
Posts salvos vao aparecer aqui.
```

## Error States

Padrao visual:

- Card branco.
- Texto cinza.
- Botao coral para retry.
- `traceId` pequeno quando vier da API.

Texto generico:

```text
Nao conseguimos carregar agora.
```

Acao:

```text
Tentar novamente
```

## Componentes Novos Necessarios

### `SearchPanel`

Responsabilidade:

- Input, filtros e resultados.

Usa:

- `AuthInput` adaptado para busca.
- `PostCard`.
- `UserListItem`.
- `EmptyState`.

### `NotificationCard`

Responsabilidade:

- Exibir notificacao individual.

Props:

- `type`
- `message`
- `read`
- `createdAt`
- `actor`
- `resource`

### `AiAssistPanel`

Responsabilidade:

- Controlar UX de IA.

Estados:

- `idle`
- `loading`
- `success`
- `fallback`
- `rateLimited`
- `error`

### `ModerationNotice`

Responsabilidade:

- Mostrar status de analise/ocultacao.

Tipos:

- `reviewRequired`
- `hidden`
- `providerError`

### `EmptyState`

Responsabilidade:

- Estados vazios reutilizaveis.

Estilo:

- Card simples.
- Texto centralizado.
- Acao opcional.

## Criterios De Aceite Das Telas Pro

- Todas usam tokens de `docs/14-design-figma.md`.
- Todas funcionam em desktop 1440x1024 e mobile 390x844.
- Todas possuem loading, erro e vazio quando aplicavel.
- Nenhuma feature Pro exige criar outro design system.
- IA nunca bloqueia o fluxo manual.
- Conteudo privado/moderado nao vaza.
- Sidebar/navegacao mantem a linguagem do Figma.

