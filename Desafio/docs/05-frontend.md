# 05 - Frontend

## Objetivo

O frontend deve entregar uma experiencia de rede social, nao uma colecao de formularios. Ele deve seguir o visual do Figma do desafio, mas evoluir os fluxos para parecer um produto completo.

Documentos visuais obrigatorios:

- [14 - Design Figma Oficial](14-design-figma.md)
- [15 - Telas Pro Derivadas](15-telas-pro.md)

Antes de implementar qualquer tela, conferir se ela vem de um frame oficial do Figma ou se e uma tela Pro derivada.

Stack recomendada:

- React.
- TypeScript.
- Vite.
- React Router.
- TanStack Query.
- React Hook Form.
- Zod.
- Tailwind CSS ou CSS Modules.
- Playwright.

## Principios De UX

- Primeira tela depois do login deve ser o feed.
- A navegacao principal deve ser sempre acessivel.
- Erros devem aparecer perto da acao que falhou.
- Estados vazios devem orientar o usuario.
- Loading deve ser visual, mas discreto.
- Acoes destrutivas exigem confirmacao.
- O app deve funcionar bem no mobile.
- IA deve aparecer como assistente de criacao, nao como requisito para postar.

## Rotas

### Mapa De Rotas E Origem Visual

| Rota | Tela | Origem visual |
| --- | --- | --- |
| `/login` | Login | Figma desktop `1:471`, mobile `1:2487` |
| `/login` erro | Login com erro | Figma desktop `1:1738`, mobile `1:2646` |
| `/signup` | Criar conta | Figma desktop `1:1941`, mobile `1:2956` |
| `/signup` erro | Criar conta com erro | Figma desktop `1:1995`, mobile `1:2998` |
| `/profile-photo` | Foto de perfil | Figma desktop `1:577`, mobile `1:2622` |
| `/` | Feed | Figma desktop `1:615`, mobile `1:3043` |
| `/new-post` | Nova publicacao | Figma desktop `1:736`, `1:861`, `1:1793`; mobile `1:3317`, `2:3520`, `6:3857` |
| `/posts/:postId/edit` | Editar publicacao | Figma desktop `1:985`, mobile `6:3930` |
| `/friends` | Amigos/conexoes | Figma desktop `1:1111`, mobile `1:2702` |
| `/u/:username` | Perfil de amigo | Figma desktop `1:1500`, mobile `1:2897` |
| `/relationships/add` | Adicionar amigo/seguir | Figma desktop `1:1416`, mobile `1:2865` |
| `/settings` | Configuracoes | Figma desktop `1:1640`, mobile `1:2526` |
| `/settings/account` | Configuracoes da conta | Figma desktop `1:1640`, mobile `1:2595` |
| `/settings/profile` | Editar perfil | Figma desktop `1:1685`, mobile `1:2567` |
| `/settings/delete` | Excluir conta | Figma desktop `1:1588`, mobile `1:2538` |
| `/search` | Busca/Explore | Tela Pro derivada |
| `/notifications` | Notificacoes | Tela Pro derivada |
| `/saved` | Posts salvos | Tela Pro derivada |
| `/posts/:postId` | Detalhe de post | Tela Pro derivada |
| `/ai/studio` | Laboratorio IA opcional | Tela Pro derivada |
| `/forgot-password` | Recuperar senha | Tela Pro derivada do Login |
| `/demo` | Guia demo/status | Tela Pro opcional |

Rotas publicas:

| Rota | Tela | Observacao |
| --- | --- | --- |
| `/login` | Login | email/username + senha |
| `/signup` | Cadastro | cria usuario |
| `/forgot-password` | Recuperar senha | derivada do Login |
| `/u/:username` | Perfil publico | pode exigir auth se configurado |

Rotas autenticadas:

| Rota | Tela | Observacao |
| --- | --- | --- |
| `/` | Feed | timeline principal |
| `/new-post` | Criar post | formulario com IA |
| `/search` | Busca | usuarios e posts |
| `/notifications` | Notificacoes | lista e marcar lida |
| `/me` | Meu perfil | atalhos de edicao |
| `/profile-photo` | Foto de perfil | etapa pos-cadastro ou edicao |
| `/settings/profile` | Editar perfil | dados pessoais |
| `/settings/account` | Conta | senha, logout, delete |
| `/settings/delete` | Excluir conta | confirmacao destrutiva |
| `/friends` | Conexoes | seguindo/seguidores |
| `/relationships/add` | Adicionar amigo | buscar e seguir usuario |
| `/saved` | Posts salvos | P1 |
| `/posts/:postId` | Detalhe de post | comentarios e acoes |
| `/posts/:postId/edit` | Editar post | dono do post |
| `/ai/studio` | Laboratorio IA | opcional para portfolio |

Rotas tecnicas opcionais:

| Rota | Tela | Observacao |
| --- | --- | --- |
| `/demo` | Guia rapido | explica usuario demo e features |
| `/health` | Status simples | consome health publico |

## Layout Base

Desktop:

- Sidebar esquerda com logo, feed, busca, criar, notificacoes, perfil e logout.
- Coluna central para feed/conteudo.
- Coluna direita opcional com sugestoes de perfis e card do usuario demo.

Mobile:

- Header superior compacto.
- Navegacao inferior com icones.
- Botao de criar post destacado.
- Feed em coluna unica.

Componentes globais:

- `AppShell`
- `TopBar`
- `SidebarNav`
- `BottomNav`
- `ProtectedRoute`
- `AuthLayout`
- `ToastProvider`
- `ConfirmDialog`
- `Skeleton`
- `EmptyState`

Componentes derivados diretamente do Figma:

- `TeachgramLogo`
- `AuthShell`
- `AuthInput`
- `PrimaryButton`
- `SocialLoginButton`
- `RememberMeCheckbox`
- `AuthSideImage`
- `SidebarNavItem`
- `PostCard`
- `ProfileAvatar`
- `PostActionButton`
- `KebabMenu`
- `SettingsCard`
- `LoadingScreen`

Componentes Pro derivados:

- `SearchPanel`
- `NotificationCard`
- `SavedPostList`
- `AiAssistPanel`
- `ModerationNotice`
- `EmptyState`
- `ConfirmDialog`

## Organizacao De Pastas

```text
src/
  app/
    App.tsx
    router.tsx
    providers.tsx
  pages/
    LoginPage.tsx
    SignupPage.tsx
    FeedPage.tsx
    ProfilePage.tsx
    NewPostPage.tsx
    SearchPage.tsx
    NotificationsPage.tsx
    SettingsProfilePage.tsx
    ConnectionsPage.tsx
  features/
    auth/
    users/
    posts/
    feed/
    relationships/
    engagement/
    notifications/
    ai/
  components/
    ui/
    layout/
  lib/
    api.ts
    auth-store.ts
    query-client.ts
    validation.ts
    errors.ts
```

## Estado Global

Use estado global minimo.

TanStack Query:

- Dados vindos da API.
- Feed.
- Perfil.
- Posts.
- Notificacoes.
- Busca.

Store leve:

- Access token.
- Usuario autenticado.
- Preferencias visuais simples.

Evitar:

- Duplicar dados da API em store global.
- Guardar feed manualmente fora do cache do TanStack Query.
- Misturar regra de negocio no componente visual.

## Cliente HTTP

Responsabilidades de `api.ts`:

- Definir `baseUrl`.
- Adicionar `Authorization`.
- Tratar `401` com tentativa de refresh.
- Converter `ProblemDetail` em erro amigavel.
- Anexar `X-Request-Id` se desejado.

Fluxo de erro:

1. API retorna `ProblemDetail`.
2. Cliente transforma em `ApiError`.
3. Tela decide se mostra toast, erro inline ou redireciona.

## Telas

### Login

Campos:

- Email/username.
- Senha.

Acoes:

- Entrar.
- Ir para cadastro.
- Entrar como demo.

Estados:

- Loading no botao.
- Erro de credenciais.
- Erro de rate limit.
- API indisponivel.

Criterios:

- Enter envia formulario.
- Token salvo com seguranca adequada para demo.
- Usuario autenticado vai para feed.

### Cadastro

Campos:

- Nome.
- Username.
- Email.
- Telefone.
- Senha.
- Confirmacao de senha.
- Foto opcional.
- Bio opcional.

Validacoes:

- Email valido.
- Senha com tamanho minimo e complexidade basica.
- Username sem espacos.
- Confirmacao igual a senha.

Estados:

- Username/email duplicado.
- Campos obrigatorios.
- Sucesso redireciona para feed.

### Feed

Elementos:

- Composer compacto para criar post.
- Lista infinita ou botao "carregar mais".
- Cards de post.
- Sidebar de sugestoes no desktop.
- Empty state para novo usuario.

Post card:

- Avatar.
- Nome.
- Username.
- Tempo relativo.
- Menu do dono.
- Midia.
- Titulo.
- Descricao.
- Curtir.
- Comentar.
- Salvar.
- Compartilhar link futuro.

Estados:

- Skeleton inicial.
- Erro com retry.
- Feed vazio.
- Atualizacao otimista de like/save.

### Criar Post

Campos:

- Titulo.
- Descricao.
- Foto URL ou upload futuro.
- Video URL opcional.
- Privacidade.

Assistente IA:

- Gerar legenda.
- Sugerir hashtags.
- Melhorar texto.
- Moderar antes de publicar opcionalmente.

UX:

- IA deve preencher sugestao em area revisavel.
- Usuario escolhe aplicar ou descartar.
- Falha na IA mostra fallback sem bloquear submit.

### Perfil

Elementos:

- Header com foto, nome, username e bio.
- Contadores.
- Botao seguir/deixar de seguir.
- Botao editar se for dono.
- Grid/lista de posts.
- Abas: posts, salvos do proprio usuario, sobre.

Regras:

- Dono ve posts privados.
- Terceiros veem apenas autorizados.
- Usuario deletado mostra 404 amigavel.

### Editar Perfil

Campos:

- Nome.
- Username.
- Email.
- Telefone.
- Bio.
- Foto.

Acoes:

- Salvar.
- Cancelar.
- Excluir conta.

Excluir conta:

- Dialog de confirmacao.
- Texto claro.
- Exigir digitar username ou senha em versao avancada.

### Conexoes

Abas:

- Seguindo.
- Seguidores.

Itens:

- Avatar.
- Nome.
- Username.
- Botao seguir/deixar.

Estados:

- Lista vazia.
- Paginacao.
- Loading por botao.

### Busca

Origem visual:

- Tela Pro derivada de `Feed` e `Amigos`.
- Ver detalhes em [15 - Telas Pro Derivadas](15-telas-pro.md).

Campos:

- Input de busca.
- Filtro `Tudo`, `Usuarios`, `Posts`.

Comportamento:

- Debounce de 300 ms.
- Resultado textual padrao.
- Busca semantica opcional quando provider ativo.

Estados:

- Sem termo.
- Carregando.
- Sem resultados.
- Fallback quando semantica falha.

### Notificacoes

Origem visual:

- Tela Pro derivada de `Feed` e `Configuracoes`.
- Ver detalhes em [15 - Telas Pro Derivadas](15-telas-pro.md).

Elementos:

- Lista por data.
- Indicador de nao lida.
- Acoes de marcar como lida.
- Link para recurso relacionado.

Tipos:

- Novo seguidor.
- Curtida.
- Comentario.
- Moderacao/IA.

### Posts Salvos

Origem visual:

- Tela Pro derivada de `Feed`.

Elementos:

- Titulo `Salvos`.
- Lista de `PostCard`.
- Estado vazio.
- Estado de post indisponivel.

Regras:

- Post privado/deletado nao vaza dados.
- Usuario pode remover item salvo.

### Detalhe De Post

Origem visual:

- Tela Pro derivada do `PostCard` do Feed.

Elementos:

- Post principal.
- Campo de comentario.
- Lista de comentarios.
- Botao voltar.
- Menu de dono.

Estados:

- Loading do post.
- Loading dos comentarios.
- Post nao encontrado.
- Post inacessivel por privacidade.

### Recuperar Senha

Origem visual:

- Tela Pro derivada de `Login`.

Elementos:

- Logo Teachgram.
- Titulo `Recuperar senha`.
- Input e-mail.
- Botao `Enviar instrucao`.
- Link `Voltar para login`.
- Imagem lateral no desktop.

Regra:

- Mensagem de sucesso deve ser generica para nao revelar se o email existe.

## Componentes De Dominio

### PostCard

Props:

- `post`
- `onLike`
- `onSave`
- `onComment`
- `onEdit`
- `onDelete`

Regras:

- Botoes de editar/deletar apenas para dono.
- Like/save com optimistic update.
- Respeitar estado de loading por acao.

### ProfileHeader

Props:

- `profile`
- `viewerState`
- `onFollowToggle`

Regras:

- Mostrar botao editar para dono.
- Mostrar seguir para terceiros.

### AiAssistPanel

Props:

- `mode`: `caption`, `hashtags`, `bio`.
- `input`
- `onApply`

Estados:

- Idle.
- Loading.
- Sucesso.
- Fallback.
- Erro.

## Validacao De Formularios

Schemas Zod recomendados:

- `loginSchema`
- `signupSchema`
- `profileUpdateSchema`
- `postCreateSchema`
- `commentCreateSchema`
- `aiCaptionSchema`

Exemplo conceitual:

```ts
const postCreateSchema = z.object({
  title: z.string().min(1).max(50),
  description: z.string().max(200).optional(),
  visibility: z.enum(["PUBLIC", "PRIVATE", "FOLLOWERS"]),
  media: z.array(mediaSchema).max(4)
});
```

## Integracao Com API

Hooks recomendados:

- `useLogin`
- `useSignup`
- `useMe`
- `useFeed`
- `useProfile`
- `useCreatePost`
- `useUpdatePost`
- `useDeletePost`
- `useLikePost`
- `useSavePost`
- `useFollowUser`
- `useNotifications`
- `useAiCaption`
- `useAiHashtags`

Cache keys:

```text
["me"]
["feed", cursor]
["profile", username]
["profile-posts", username, cursor]
["notifications", cursor]
["search", type, query]
```

Invalidacoes:

- Criou post: invalidar `feed`, `profile-posts`.
- Curtiu post: atualizar cache do post e feed.
- Comentou: atualizar post e comentarios.
- Seguiu usuario: invalidar perfil, conexoes e feed.
- Editou perfil: invalidar `me`, `profile`.

## Acessibilidade

Requisitos:

- Todo botao icon-only precisa de `aria-label`.
- Inputs precisam de label.
- Erros precisam ser lidos por leitores de tela quando possivel.
- Contraste suficiente.
- Navegacao por teclado em modais.
- Imagens devem aceitar alt text.

## Responsividade

Breakpoints sugeridos:

- Mobile: ate 767 px.
- Tablet: 768 a 1023 px.
- Desktop: 1024 px ou mais.

Regras:

- Feed mobile ocupa largura total.
- Sidebar some no mobile e vira bottom nav.
- Cards nunca devem estourar horizontalmente.
- Midias usam `aspect-ratio`.
- Formularios devem ter largura confortavel no desktop e full width no mobile.

## Criterios De Aceite Do Frontend

- Usuario demo acessa o feed sem cadastro manual.
- Login exibe erro de senha incorreta.
- Cadastro exibe erro de email duplicado.
- Feed carrega posts e proxima pagina.
- Usuario cria post e ve o post no feed.
- Usuario curte e descurte com contador atualizado.
- Usuario comenta e ve comentario.
- Usuario segue outro perfil e botao muda estado.
- Usuario edita perfil e ve atualizacao no header.
- Usuario usa IA para legenda; se falhar, recebe fallback.
- App funciona em mobile e desktop.
