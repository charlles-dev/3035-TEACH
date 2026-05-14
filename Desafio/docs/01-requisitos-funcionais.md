# 01 - Requisitos Funcionais

## Convencoes

Prioridade:

- `P0`: obrigatorio para a primeira versao apresentavel.
- `P1`: importante para portfolio profissional.
- `P2`: avancado, pode entrar depois sem quebrar a arquitetura.

Status esperado:

- Todo requisito deve possuir criterio de aceite.
- Todo criterio de aceite deve ser testavel manualmente ou automaticamente.

## Modulo Identity

Responsabilidade: cadastro, login, tokens, senha e identidade autenticada.

### RF-ID-01 - Cadastro De Usuario

Prioridade: `P0`

O sistema deve permitir criar uma conta com:

- Nome completo.
- Username unico.
- Email unico e valido.
- Telefone unico.
- Senha.
- Link de foto de perfil opcional.
- Bio/descricao opcional.

Regras:

- `name`, `username`, `email` e `password` sao obrigatorios.
- `email` deve conter formato valido.
- `username` deve ser normalizado para busca, sem espacos.
- `password` deve ser armazenado com hash seguro.
- Usuario criado deve iniciar como ativo e nao deletado.

Criterios de aceite:

- Dado um email novo, cadastro retorna `201 Created`.
- Dado email ja cadastrado, retorna `409 Conflict`.
- Dado username ja cadastrado, retorna `409 Conflict`.
- Dado email invalido, retorna `400 Bad Request`.
- A senha nunca aparece em responses.

### RF-ID-02 - Login

Prioridade: `P0`

O sistema deve permitir login usando `identifier` e `password`, onde `identifier` pode ser email ou username.

Regras:

- Usuario deletado nao pode logar.
- Senha incorreta retorna erro generico.
- Login bem-sucedido retorna access token, refresh token e dados basicos do usuario.

Criterios de aceite:

- Login com email valido retorna `200 OK`.
- Login com username valido retorna `200 OK`.
- Senha errada retorna `401 Unauthorized`.
- Usuario deletado retorna `401 Unauthorized`.

### RF-ID-03 - Refresh E Logout

Prioridade: `P1`

O sistema deve renovar access tokens com refresh token e permitir logout.

Regras:

- Refresh token deve ter expiracao maior que access token.
- Logout invalida o refresh token atual.
- Access token expirado nao deve permitir chamadas protegidas.

Criterios de aceite:

- Refresh token valido gera novo access token.
- Refresh token revogado retorna `401 Unauthorized`.
- Logout retorna `204 No Content`.

## Modulo Profiles

Responsabilidade: dados publicos e editaveis do usuario.

### RF-PRO-01 - Perfil Publico

Prioridade: `P0`

O sistema deve exibir perfil publico por username.

Dados exibidos:

- Nome.
- Username.
- Bio.
- Foto.
- Contadores de posts, seguidores e seguindo/amigos.
- Posts publicos do perfil.

Regras:

- Usuario deletado nao aparece.
- Posts privados aparecem apenas para o dono.

Criterios de aceite:

- Perfil existente retorna `200 OK`.
- Perfil inexistente retorna `404 Not Found`.
- Visitante nao autenticado pode ver perfil publico se a estrategia permitir acesso publico.

### RF-PRO-02 - Editar Perfil

Prioridade: `P0`

O usuario autenticado deve editar seus dados.

Campos editaveis:

- Nome.
- Username.
- Telefone.
- Email.
- Bio.
- Link de foto.

Regras:

- Email, username e telefone continuam unicos.
- Troca de email pode exigir login novamente em versao futura.
- Username alterado deve atualizar URLs futuras.

Criterios de aceite:

- Dono altera perfil e recebe dados atualizados.
- Outro usuario nao altera perfil alheio.
- Username duplicado retorna `409 Conflict`.

### RF-PRO-03 - Excluir Conta

Prioridade: `P0`

O usuario deve conseguir excluir logicamente sua conta.

Regras:

- `deleted = true`.
- Dados permanecem no banco para integridade historica.
- Login fica bloqueado.
- Perfil some de buscas.
- Posts podem ficar ocultos por padrao.

Criterios de aceite:

- Delete retorna `204 No Content`.
- Login posterior falha.
- Usuario nao aparece em listagens.

## Modulo Posts

Responsabilidade: criacao, edicao, exclusao e consulta de posts.

### RF-POST-01 - Criar Post

Prioridade: `P0`

Usuario autenticado deve criar post com:

- Titulo de ate 50 caracteres.
- Descricao de ate 200 caracteres.
- Link de foto opcional.
- Link de video opcional.
- Privacidade obrigatoria.

Regras:

- `title` e `private` sao obrigatorios.
- Post pertence ao usuario autenticado.
- Post inicia com `deleted = false`.
- Conteudo pode ser enviado para moderacao assincroma.

Criterios de aceite:

- Post valido retorna `201 Created`.
- Titulo ausente retorna `400 Bad Request`.
- Titulo acima de 50 caracteres retorna `400 Bad Request`.
- Descricao acima de 200 caracteres retorna `400 Bad Request`.

### RF-POST-02 - Editar Post

Prioridade: `P0`

Dono do post deve editar titulo, descricao, midias e privacidade.

Regras:

- Somente dono edita.
- Post deletado nao edita.
- `updatedAt` deve ser atualizado.

Criterios de aceite:

- Dono edita e recebe post atualizado.
- Outro usuario recebe `403 Forbidden`.
- Post inexistente retorna `404 Not Found`.

### RF-POST-03 - Deletar Post

Prioridade: `P0`

Dono do post deve excluir logicamente um post.

Regras:

- `deleted = true`.
- Post deletado sai do feed.
- Comentarios e likes permanecem para auditoria, mas nao aparecem publicamente.

Criterios de aceite:

- Dono deleta e recebe `204 No Content`.
- Post deletado nao aparece no feed.

### RF-POST-04 - Alterar Privacidade

Prioridade: `P0`

Dono do post deve alternar entre publico e privado.

Regras:

- Publico aparece para usuarios autorizados no feed.
- Privado aparece apenas para o dono.

Criterios de aceite:

- Post privado nao aparece no feed de terceiros.
- Dono visualiza seus posts privados no perfil.

## Modulo Social Graph

Responsabilidade: relacoes entre usuarios.

### RF-SOC-01 - Seguir Usuario

Prioridade: `P0`

Usuario autenticado deve seguir outro usuario.

Regras:

- Usuario nao pode seguir a si mesmo.
- Relacao duplicada nao deve ser criada.
- Usuario deletado nao pode ser seguido.
- A primeira versao usa modelo `follow`, mais simples que amizade bilateral.

Criterios de aceite:

- Follow valido retorna `201 Created`.
- Follow duplicado retorna `200 OK` ou `409 Conflict`, escolhendo comportamento consistente.
- Seguir a si mesmo retorna `400 Bad Request`.

### RF-SOC-02 - Deixar De Seguir

Prioridade: `P0`

Usuario autenticado deve desfazer follow.

Criterios de aceite:

- Unfollow existente retorna `204 No Content`.
- Unfollow inexistente tambem pode retornar `204 No Content` para idempotencia.

### RF-SOC-03 - Listar Conexoes

Prioridade: `P0`

O sistema deve listar seguidores e seguindo com paginacao.

Criterios de aceite:

- Lista retorna dados resumidos de perfil.
- Listagem ignora usuarios deletados.

## Modulo Engagement

Responsabilidade: curtidas, comentarios e salvamentos.

### RF-ENG-01 - Curtir Post

Prioridade: `P0`

Usuario autenticado deve curtir posts visiveis.

Regras:

- Um usuario so curte o mesmo post uma vez.
- Curtida pode ser removida.
- Contador deve ser consistente.

Criterios de aceite:

- Curtir post retorna contador atualizado.
- Curtir novamente nao duplica.
- Descurtir decrementa contador.

### RF-ENG-02 - Comentar Post

Prioridade: `P1`

Usuario autenticado deve comentar em post visivel.

Regras:

- Comentario deve ter texto entre 1 e 500 caracteres.
- Comentario deletado deve ser soft delete.
- Comentario pode gerar notificacao.

Criterios de aceite:

- Comentario valido retorna `201 Created`.
- Comentario vazio retorna `400 Bad Request`.

### RF-ENG-03 - Salvar Post

Prioridade: `P1`

Usuario autenticado deve salvar posts para ver depois.

Criterios de aceite:

- Save valido retorna `201 Created`.
- Save duplicado nao duplica registro.
- Lista de salvos mostra posts visiveis.

## Modulo Feed

Responsabilidade: timeline e descoberta.

### RF-FEED-01 - Feed Cronologico

Prioridade: `P0`

O feed deve listar posts publicos e posts de usuarios seguidos, em ordem decrescente de criacao.

Regras:

- Usar cursor pagination.
- Excluir posts deletados.
- Respeitar privacidade.
- Incluir dados resumidos do autor e contadores.

Criterios de aceite:

- Primeiro load retorna no maximo `limit` itens.
- Cursor retorna proxima pagina sem duplicar posts.
- Post privado de terceiro nao aparece.

### RF-FEED-02 - Feed Do Perfil

Prioridade: `P0`

Perfil deve listar posts do usuario.

Regras:

- Visitantes veem apenas posts publicos.
- Dono ve publicos e privados.

Criterios de aceite:

- Dono enxerga privado.
- Terceiro nao enxerga privado.

## Modulo Notifications

Responsabilidade: notificacoes de interacoes sociais.

### RF-NOT-01 - Criar Notificacao

Prioridade: `P1`

O sistema deve criar notificacoes para:

- Novo seguidor.
- Curtida em post.
- Comentario em post.

Regras:

- Nao notificar o proprio autor quando ele interage com seu proprio post.
- Notificacoes podem ser criadas por eventos internos.

Criterios de aceite:

- Curtida de terceiro gera notificacao.
- Comentario de terceiro gera notificacao.
- Usuario consegue marcar notificacao como lida.

## Modulo Search

Responsabilidade: busca textual e descoberta.

### RF-SEA-01 - Busca De Usuarios

Prioridade: `P0`

Usuario deve buscar perfis por nome ou username.

Criterios de aceite:

- Busca parcial retorna perfis ativos.
- Usuario deletado nao aparece.

### RF-SEA-02 - Busca De Posts

Prioridade: `P1`

Usuario deve buscar posts por titulo, descricao e hashtags.

Criterios de aceite:

- Resultado respeita privacidade.
- Resultado ignora posts deletados.

## Modulo AI

Responsabilidade: assistencias inteligentes com fallback.

### RF-AI-01 - Gerar Legenda

Prioridade: `P1`

Usuario deve pedir sugestao de legenda a partir de tema, imagem opcional e tom.

Regras:

- Provider principal: Groq Compound.
- Timeout curto.
- Fallback local retorna mensagem simples quando cota/provider falhar.
- Resposta deve ser revisavel pelo usuario antes de publicar.

Criterios de aceite:

- Provider disponivel retorna sugestao.
- Provider indisponivel nao quebra tela de criar post.

### RF-AI-02 - Sugerir Hashtags

Prioridade: `P1`

Sistema deve sugerir hashtags com base em titulo/descricao.

Criterios de aceite:

- Retorna lista curta, sem duplicatas.
- Nao publica automaticamente.

### RF-AI-03 - Moderar Conteudo

Prioridade: `P1`

Sistema deve classificar conteudo textual e opcionalmente imagem.

Regras:

- Moderacao pode ser assincroma.
- Conteudo suspeito fica com status `REVIEW_REQUIRED` ou `HIDDEN`.
- Usuario recebe feedback generico.

Criterios de aceite:

- Conteudo claramente inadequado nao aparece publicamente.
- Falha no provider nao bloqueia todos os posts, mas marca evento para revisao.

## Modulo Observability

Responsabilidade: saude, metricas e diagnostico.

### RF-OBS-01 - Health Checks

Prioridade: `P0`

API deve expor health check.

Criterios de aceite:

- `/actuator/health` retorna status.
- Falha de banco aparece no health detalhado em ambiente autorizado.

### RF-OBS-02 - Logs Estruturados

Prioridade: `P1`

Eventos importantes devem gerar logs com correlation id.

Criterios de aceite:

- Request log contem method, path, status e duration.
- Erros possuem stack trace no backend, sem vazar detalhes para o cliente.

