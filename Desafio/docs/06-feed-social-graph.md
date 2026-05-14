# 06 - Feed E Social Graph

## Objetivo

O feed e o social graph sao o coracao da rede social. Eles devem ir alem de listar posts por data, mas sem tentar recriar toda a complexidade real do Instagram na primeira versao.

Meta:

- MVP: feed cronologico correto e simples.
- Portfolio: feed com eventos, cache, cursor pagination e materializacao opcional.
- Futuro: ranking mais sofisticado e recomendacao.

## Modelo De Relacionamento

Escolha recomendada: `follow` dirigido.

Motivo:

- Mais simples que amizade bilateral.
- Mais parecido com Instagram.
- Permite feed baseado em "quem eu sigo".
- Pode simular amigos tratando follow reciproco como amizade.

Estados:

- `ACTIVE`: usuario segue outro.
- `BLOCKED`: reservado para futuro.

Regras:

- Usuario nao segue a si mesmo.
- Usuario deletado nao pode seguir nem ser seguido.
- Relacao duplicada nao existe.
- Unfollow deve ser idempotente.

## Amizade Vs Follow

O desafio fala em amigos. Para transformar em experiencia tipo Instagram:

- Usar `follow` como relacao principal.
- Exibir "seguindo" e "seguidores" na interface.
- Chamar de "amigos" em telas se quiser manter linguagem do desafio.
- Considerar amizade quando `A segue B` e `B segue A`.

Isso mostra maturidade: voce atende o desafio e ainda escolhe uma modelagem mais flexivel.

## Feed MVP

Consulta direta no banco:

- Buscar posts publicos.
- Incluir posts dos usuarios seguidos.
- Incluir posts do proprio usuario.
- Excluir posts deletados.
- Respeitar privacidade.
- Ordenar por `created_at DESC, id DESC`.

Pseudo-regra:

```text
post aparece no feed se:
  post.deleted_at is null
  e author.status = ACTIVE
  e (
    post.author_id = viewer_id
    ou post.visibility = PUBLIC
    ou (post.visibility = FOLLOWERS e viewer segue author)
  )
```

Vantagens:

- Simples.
- Facil de testar.
- Otimo para comecar.

Limites:

- Pode ficar pesado com muitos usuarios/posts.
- Recalcula feed a cada request.

## Feed Portfolio

Adicionar `feed_items`.

Ideia:

- Quando um post e criado, eventos decidem em quais feeds ele entra.
- Quando usuario segue alguem, posts recentes do seguido entram no feed do seguidor.
- Quando post fica privado/deletado, itens sao removidos ou ignorados.

Tabela:

- `owner_id`: usuario dono do feed.
- `post_id`: post.
- `author_id`: autor.
- `reason`: motivo.
- `score`: ranking futuro.
- `created_at`: momento de entrada.

Fluxos:

### Post Criado

1. `posts` publica `PostCreatedEvent`.
2. `feed` recebe evento.
3. Se post for publico/followers, busca seguidores do autor.
4. Cria `feed_items` para seguidores.
5. Cria `feed_item` para o proprio autor.
6. Invalida cache de feed.

### Follow Criado

1. `socialgraph` publica `UserFollowedEvent`.
2. `feed` busca posts recentes do usuario seguido.
3. Cria itens para o novo seguidor.
4. Invalida feed do seguidor.

### Post Deletado

1. `posts` publica `PostDeletedEvent`.
2. `feed` remove ou ignora `feed_items`.
3. Cache e invalidado.

### Privacidade Alterada

1. `posts` publica `PostPrivacyChangedEvent`.
2. `feed` remove itens que nao podem mais aparecer.
3. Se virou publico, pode distribuir novamente.

## Ranking

Primeira versao:

- Ordem cronologica.

Versao portfolio:

- Ainda ordenar primariamente por data.
- Usar `score` apenas como campo pronto para evolucao.

Formula simples futura:

```text
score =
  recency_score
  + like_count * 0.15
  + comment_count * 0.35
  + author_affinity * 0.50
```

Nao implementar ranking complexo antes do feed cronologico estar solido.

## Afinidade

Afinidade pode ser calculada futuramente por:

- Usuario segue autor.
- Usuario curtiu posts do autor.
- Usuario comentou posts do autor.
- Usuario salvou posts do autor.

Para portfolio, documentar a possibilidade e suficiente. Implementar apenas se o MVP ja estiver robusto.

## Cursor Pagination

Feed deve usar cursor, nao offset.

Motivo:

- Offset degrada com listas grandes.
- Offset duplica/pula itens quando novos posts entram.
- Cursor e padrao mais profissional para timelines.

Cursor recomendado:

- `createdAt`.
- `postId` ou `feedItemId`.

Ordenacao:

```sql
ORDER BY created_at DESC, id DESC
```

Condicao para proxima pagina:

```sql
WHERE (created_at, id) < (:cursorCreatedAt, :cursorId)
```

## Cache Do Feed

Cache recomendado:

- Key: `feed:{userId}:{cursorHash}:{limit}`
- TTL: 30 a 120 segundos.

Invalidar quando:

- Usuario cria post.
- Usuario segue/deixa de seguir.
- Post do feed e deletado.
- Privacidade muda.
- Usuario bloqueia outro futuro.

Estrategia:

- Cache curto e simples.
- Se cache falhar, consultar banco.
- Nunca depender de cache para permissao.

## Privacidade

Regras por visibilidade:

### PUBLIC

Pode aparecer:

- Feed global.
- Feed de seguidores.
- Perfil publico.
- Busca.

### FOLLOWERS

Pode aparecer:

- Autor.
- Seguidores.

Nao aparece:

- Busca publica para terceiros.

### PRIVATE

Pode aparecer:

- Apenas autor.

Nao aparece:

- Feed de terceiros.
- Busca.
- Perfil visto por terceiros.

## Bloqueio Futuro

Nao implementar no MVP, mas deixar design preparado.

Se houver bloqueio:

- Usuario bloqueado nao ve perfil.
- Posts nao aparecem no feed.
- Follow e removido.
- Novas interacoes sao impedidas.

## Descoberta

Feed principal nao deve ser o unico lugar para descobrir conteudo.

Tela `Search/Explore` pode mostrar:

- Perfis sugeridos.
- Posts publicos recentes.
- Posts com maior engajamento.
- Resultado de busca textual.
- Resultado semantico se IA estiver ativa.

## Eventos Necessarios

Eventos de entrada:

- `PostCreatedEvent`
- `PostDeletedEvent`
- `PostPrivacyChangedEvent`
- `UserFollowedEvent`
- `UserUnfollowedEvent`
- `UserDeletedEvent`

Eventos de saida:

- `FeedItemCreatedEvent`
- `FeedRebuiltEvent`

## Testes Essenciais

Casos obrigatorios:

- Feed mostra post publico.
- Feed nao mostra post deletado.
- Feed nao mostra post privado de terceiro.
- Feed mostra post privado do proprio usuario.
- Follow faz posts futuros aparecerem.
- Unfollow remove posts futuros do feed.
- Cursor nao duplica itens.
- Cache nao ignora regra de privacidade.

## Estrategia De Implementacao

Fase 1:

- Implementar follows.
- Implementar feed por consulta direta.
- Implementar cursor.

Fase 2:

- Adicionar cache Redis.
- Invalidar cache por eventos.

Fase 3:

- Adicionar `feed_items`.
- Processar eventos de fanout.
- Manter consulta direta como fallback.

Fase 4:

- Adicionar ranking simples e explore.

