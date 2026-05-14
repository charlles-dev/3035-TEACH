# 14 - Design Figma Oficial

## Fonte De Verdade

O design oficial do frontend do TeachGram vem do arquivo Figma:

```text
Nome: Teach-3035---Desafio-Front-end-final
URL: https://www.figma.com/design/BNgj8foFAiNUa2EkoRk260/Teach-3035---Desafio-Front-end-final?node-id=0-1
File key: BNgj8foFAiNUa2EkoRk260
Node principal desktop: 0:1
Pagina desktop: 💻Desafio final WEB
Pagina mobile: 📱Desafio final WEB Responsivo
```

Este arquivo deve ser tratado como fonte de verdade para:

- Composicao visual.
- Paleta.
- Tipografia.
- Radius.
- Sombras.
- Sidebar.
- Cards de post.
- Formularios de autenticacao.
- Telas de conta, perfil, amigos e publicacao.

As telas Pro que nao existem no Figma devem herdar essa identidade visual. Nao criar uma segunda linguagem visual.

## Viewports De Referencia

Desktop:

```text
1440 x 1024
```

Mobile:

```text
390 x 844
```

Durante implementacao, validar nesses dois tamanhos primeiro. Depois adaptar responsivamente para larguras intermediarias.

## Frames Desktop Encontrados

| Tela | Frame ID | Uso |
| --- | --- | --- |
| Login | `1:471` | `/login` estado normal |
| Login erro | `1:1738` | `/login` com erro |
| Criar conta | `1:1941` | `/signup` estado normal |
| Criar conta erro | `1:1995` | `/signup` com erro |
| Foto de perfil | `1:577` | `/profile-photo` ou etapa pos-cadastro |
| Feed | `1:615` | `/` feed principal |
| Feed com modal de exclusao | `1:2332` | estado de confirmar exclusao de post |
| Nova publicacao | `1:736` | estado base de criacao de post |
| Nova publicacao | `1:861` | variacao de criacao de post |
| Nova publicacao | `1:1793` | variacao de criacao/carregamento |
| Editar publicacao | `1:985` | editar post |
| amigos | `1:1111` | `/friends` lista de amigos/conexoes |
| Amigo | `1:1500` | `/u/:username` perfil de outro usuario |
| Adicionar | `1:1416` | adicionar amigo/seguir |
| pagina de carregamento | `1:1402` | loading global |
| Configuracoes da conta | `1:1640` | `/settings/account` |
| Editar perfil | `1:1685` | `/settings/profile` |
| Configuracoes - excluir conta | `1:1588` | `/settings/delete` |

Observacao:

- Existem frames repetidos de `Feed` e `Nova publicacao`. Eles devem ser usados como estados/variacoes, nao como telas completamente diferentes.
- Antes de codar uma tela, extrair `get_design_context` do frame correspondente para confirmar medidas e assets.

## Frames Mobile Encontrados

| Tela | Frame ID | Uso |
| --- | --- | --- |
| Login | `1:2487` | `/login` mobile |
| Login erro | `1:2646` | `/login` erro mobile |
| Criar conta | `1:2956` | `/signup` mobile |
| Criar conta erro | `1:2998` | `/signup` erro mobile |
| Foto de perfil | `1:2622` | `/profile-photo` mobile |
| Tela de carregamento | `1:2688` | loading global mobile |
| Feed | `1:3043` | `/` feed mobile |
| Feed | `1:3105` | variacao de feed mobile |
| Feed | `1:3163` | variacao de feed mobile |
| Amigos | `1:2702` | `/friends` mobile |
| adicionar | `1:2865` | adicionar amigo/seguir mobile |
| amigo | `1:2897` | perfil de outro usuario mobile |
| Configuracoes | `1:2526` | configuracoes mobile |
| Configuracoes da conta | `1:2595` | `/settings/account` mobile |
| Editar perfil | `1:2567` | `/settings/profile` mobile |
| excluir conta | `1:2538` | `/settings/delete` mobile |
| Nova publicacao - link | `1:3317` | criar post, etapa link |
| Nova publicacao - carregamento | `2:3520` | criar post, loading |
| Nova publicacao - legenda | `6:3857` | criar post, legenda |
| Nova publicacao - editar publicacao | `6:3930` | editar post mobile |

## Mapeamento De Rotas Para Figma

| Rota | Desktop | Mobile | Tipo |
| --- | --- | --- | --- |
| `/login` | `1:471` | `1:2487` | oficial |
| `/login` erro | `1:1738` | `1:2646` | oficial |
| `/signup` | `1:1941` | `1:2956` | oficial |
| `/signup` erro | `1:1995` | `1:2998` | oficial |
| `/profile-photo` | `1:577` | `1:2622` | oficial |
| `/` | `1:615` | `1:3043` | oficial |
| `/new-post` | `1:736`, `1:861`, `1:1793` | `1:3317`, `2:3520`, `6:3857` | oficial |
| `/posts/:postId/edit` | `1:985` | `6:3930` | oficial |
| `/friends` | `1:1111` | `1:2702` | oficial |
| `/u/:username` | `1:1500` | `1:2897` | oficial |
| `/relationships/add` | `1:1416` | `1:2865` | oficial |
| `/settings` | `1:1640` | `1:2526` | oficial/derivada |
| `/settings/account` | `1:1640` | `1:2595` | oficial |
| `/settings/profile` | `1:1685` | `1:2567` | oficial |
| `/settings/delete` | `1:1588` | `1:2538` | oficial |
| `/search` | derivar do Feed + Amigos | derivar do Feed + Amigos | Pro |
| `/notifications` | derivar do Feed + Configuracoes | derivar do Feed + Configuracoes | Pro |
| `/saved` | derivar do Feed | derivar do Feed | Pro |
| `/posts/:postId` | derivar do PostCard do Feed | derivar do Feed mobile | Pro |
| `/forgot-password` | derivar de Login | derivar de Login | Pro |
| `/demo` | derivar de Feed | derivar de Feed | Pro opcional |

## Tokens Visuais Extraidos

### Cores

| Token sugerido | Valor | Uso |
| --- | --- | --- |
| `--color-brand-coral` | `#F37671` | botoes primarios, links ativos, bordas decorativas, checkbox |
| `--color-text-strong` | `#303030` | logo/texto principal/titulos |
| `--color-text-default` | `#666666` | labels e texto de formulario |
| `--color-text-muted` | `#8E8E8E` | feed, metadata, navegacao |
| `--color-text-soft` | `#A09F9F` | placeholders e botoes sociais |
| `--color-border-default` | `#E2E2E2` | cards de navegacao, bordas sutis |
| `--color-surface` | `#FFFFFF` | fundo principal e cards |
| `--color-media-placeholder` | `#DDDDDD` | fundo de midia antes da imagem |

### Tipografia

Fontes encontradas:

- `Inter`
- `Poppins`

Uso recomendado:

- `Inter` como fonte principal do app.
- `Poppins` apenas onde o Figma usa botoes sociais/textos especificos.

Escala observada:

| Uso | Tamanho | Peso |
| --- | --- | --- |
| Titulos de formulario | `20px` | `600` |
| Labels de formulario | `15px` | `400` |
| Placeholders | `15px` | `400` |
| Textos auxiliares | `12px` | `400` |
| Username no feed | `25px` desktop | `400` |
| Texto de post | `20px` desktop | `400` |
| Sidebar | `20px` desktop | `400` |

Regra:

- Nao usar letter spacing negativo novo alem do que vier do Figma. Se o framework dificultar, priorizar legibilidade.

### Radius

| Elemento | Radius |
| --- | --- |
| Inputs auth | `8px` |
| Botoes auth | `10px` |
| Cards de feed | `18px` |
| Sidebar buttons | `15px` |
| Imagens de post | cerca de `8.5px` |
| Avatar | circular |
| Hero image login | canto superior esquerdo extremamente arredondado, `359px` no desktop |

### Sombras

Botoes:

```css
box-shadow: 0 4px 21px -4px rgba(0, 0, 0, 0.4);
```

Botoes sociais:

```css
box-shadow: 0 4px 21px -4px rgba(0, 0, 0, 0.2);
```

Cards de feed:

```css
box-shadow: 0 0 4px 0 rgba(0, 0, 0, 0.25);
```

## Regras De Fidelidade

### Autenticacao Desktop

Manter:

- Layout dividido em 50/50.
- Lado esquerdo branco com logo Teachgram.
- Formulario centralizado no eixo do painel esquerdo.
- Lado direito com imagem grande, borda coral e canto superior esquerdo arredondado.
- Elementos decorativos translucidos sobre a imagem.
- Botao primario coral com sombra.
- Botoes Google/Apple brancos com sombra.
- Link de cadastro em coral sublinhado.

Nao fazer:

- Transformar em landing page.
- Colocar hero text em card.
- Trocar a imagem lateral por gradiente.
- Mudar a paleta para roxo/azul ou outra identidade.

### Feed Desktop

Manter:

- Logo no canto superior esquerdo.
- Sidebar vertical com botoes grandes, borda sutil e radius 15.
- Feed central com cards de 558px no desktop de referencia.
- Card branco com sombra sutil e radius 18.
- Avatar circular 74px.
- Imagem do post com radius pequeno.
- Elementos decorativos coral claro no lado direito.

Extensoes permitidas:

- Adicionar itens de sidebar para Busca, Notificacoes e Salvos.
- Se a sidebar ficar muito alta, reduzir altura dos itens proporcionalmente mantendo estilo.
- Em telas Pro, usar o mesmo card central do feed como container principal.

### Mobile

Manter:

- Viewport base 390x844.
- Feed em coluna unica.
- A navegacao deve usar o padrao do arquivo mobile, nao a sidebar desktop comprimida.
- Os estados de nova publicacao mobile devem seguir as etapas existentes: link, carregamento, legenda e editar.

## Assets

O MCP retorna URLs temporarias para assets. Elas expiram e nao devem ser hardcoded em documentacao final de implementacao.

Na implementacao:

- Baixar/exportar assets definitivos do Figma.
- Salvar em `frontend/src/assets/figma/`.
- Nomear assets por uso, nao pelo UUID temporario.

Assets obrigatorios:

- Logo Teachgram.
- Icone Teachgram.
- Imagem hero do auth.
- Icones Google/Apple.
- Icones de sidebar.
- Icone de like/coracao.
- Elementos decorativos coral.
- Avatares e imagens de exemplo para seed/demo.

## Componentes Visuais Base

Componentes que devem nascer diretamente do Figma:

- `TeachgramLogo`
- `AuthShell`
- `AuthInput`
- `PrimaryButton`
- `SocialLoginButton`
- `RememberMeCheckbox`
- `AuthSideImage`
- `SidebarNav`
- `SidebarNavItem`
- `PostCard`
- `ProfileAvatar`
- `PostActionButton`
- `KebabMenu`
- `ConfirmDialog`
- `SettingsCard`
- `LoadingScreen`

Componentes Pro devem reutilizar esses blocos:

- `SearchPanel`
- `NotificationCard`
- `SavedPostsList`
- `AiAssistPanel`
- `ModerationNotice`
- `EmptyState`

## Criterios De Paridade Visual

Antes de considerar uma tela pronta:

- Desktop confere em 1440x1024.
- Mobile confere em 390x844.
- Cores principais batem com tokens.
- Logo tem proporcao igual.
- Inputs e botoes tem mesma altura, radius e sombra.
- Cards do feed tem mesma largura visual no desktop.
- Sidebar usa mesma posicao, espacamento e iconografia.
- Textos nao quebram de forma estranha.
- Imagens mantem object-fit cover e radius.
- Estados de erro ficam no mesmo lugar visual dos frames de erro.

