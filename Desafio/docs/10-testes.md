# 10 - Testes

## Objetivo

Garantir que o TeachGram Pro seja confiavel, demonstravel e facil de evoluir. Os testes devem cobrir comportamento de negocio, seguranca, integracao com banco e fluxos reais do usuario.

Meta:

- Nao buscar 100% de cobertura artificial.
- Cobrir fluxos criticos.
- Proteger regras de permissao e privacidade.
- Validar que a demo publica nao quebra nos caminhos principais.

## Piramide De Testes

Camadas:

1. Unitarios: regras pequenas e services.
2. Integracao: JPA, controllers, security, banco e migrations.
3. Contrato/API: formato dos endpoints.
4. E2E: fluxos reais no navegador.
5. Performance basica: feed, login e criacao de post.

## Backend - Unit Tests

Ferramentas:

- JUnit 5.
- AssertJ.
- Mockito quando fizer sentido.

Testar:

- Validacao de regras de negocio.
- Services de dominio.
- Geracao de cursor.
- Politicas de privacidade.
- Fallback de IA.
- Rate limit service.

Casos obrigatorios:

### Identity

- Cadastro normal cria usuario ativo.
- Cadastro com email duplicado falha.
- Cadastro com username duplicado falha.
- Senha e enviada para encoder.
- Usuario deletado nao autentica.

### Posts

- Criar post exige titulo.
- Criar post respeita limite de 50 caracteres.
- Descricao respeita limite de 200 caracteres.
- Apenas dono edita.
- Apenas dono deleta.
- Post deletado nao edita.

### Social Graph

- Usuario nao segue a si mesmo.
- Follow duplicado nao duplica.
- Unfollow e idempotente.
- Usuario deletado nao pode ser seguido.

### Engagement

- Like duplica nao aumenta contador.
- Unlike decrementa contador.
- Comentario vazio falha.
- Save duplicado nao duplica.

### Feed

- Privacidade `PRIVATE` aparece apenas para dono.
- Privacidade `FOLLOWERS` aparece para seguidores.
- Cursor nao duplica itens.

### AI

- Provider falhando ativa fallback.
- Timeout ativa fallback.
- Output invalido ativa fallback.
- Rate limit bloqueia excesso.

## Backend - Integration Tests

Ferramentas:

- Spring Boot Test.
- Testcontainers com PostgreSQL.
- Flyway ativo.
- MockMvc ou WebTestClient.

Testar:

- Controllers com security.
- Repositories reais.
- Migrations.
- Constraints unicas.
- Transacoes.

Casos obrigatorios:

- `POST /auth/signup` persiste usuario e perfil.
- `POST /auth/login` retorna tokens.
- `GET /users/me` exige token.
- `PATCH /users/me` atualiza perfil.
- `POST /posts` cria post vinculado ao usuario autenticado.
- `GET /feed` respeita privacidade.
- `POST /posts/{id}/likes` exige autenticacao.
- Usuario A nao deleta post de usuario B.
- Soft delete remove usuario/post das listagens.

## Backend - Security Tests

Casos:

- Sem token em rota protegida retorna `401`.
- Token invalido retorna `401`.
- Token expirado retorna `401`.
- Role `USER` nao acessa admin.
- Usuario nao edita recurso alheio.
- CORS permite somente origem configurada.
- Rate limit em login retorna `429`.

## Backend - Modulith Tests

Spring Modulith deve ser usado para verificar estrutura.

Testes:

- Verificar limites entre modulos.
- Documentar modulos automaticamente se desejado.
- Garantir que modulo nao acessa repository de outro modulo indevidamente.

Conceito:

```java
@Test
void verifiesModularStructure() {
    ApplicationModules.of(TeachgramApplication.class).verify();
}
```

## Frontend - Unit E Component Tests

Ferramentas:

- Vitest.
- React Testing Library.

Testar:

- Formularios.
- Componentes de post.
- Estados de loading/erro.
- Hooks com mock de API.

Casos:

- Login mostra erro de senha incorreta.
- Signup valida email.
- PostCard mostra botoes de dono apenas para owner.
- Botao like chama mutation.
- AiAssistPanel mostra fallback.

## Frontend - E2E

Ferramenta:

- Playwright.

## Frontend - Paridade Visual Com Figma

Fonte de referencia:

- [14 - Design Figma Oficial](14-design-figma.md)
- [15 - Telas Pro Derivadas](15-telas-pro.md)

Viewports obrigatorios:

- Desktop: `1440x1024`.
- Mobile: `390x844`.

Casos de paridade:

- Login desktop deve seguir frame `1:471`.
- Login erro desktop deve seguir frame `1:1738`.
- Criar conta deve seguir frames `1:1941` e `1:1995`.
- Feed desktop deve seguir frame `1:615`.
- Feed mobile deve seguir frame `1:3043`.
- Nova publicacao mobile deve seguir as etapas `1:3317`, `2:3520`, `6:3857`.
- Telas Pro devem reutilizar tokens, cards, sidebar, radius e hierarquia definidos no design oficial.

Checklist visual:

- Logo com proporcao correta.
- Coral `#F37671` em botoes/links ativos.
- Inputs com 48px de altura, radius e borda coerentes.
- Cards do feed com radius 18 e sombra sutil.
- Sidebar desktop com botoes grandes e borda `#E2E2E2`.
- Mobile sem overflow horizontal.
- Textos nao sobrepoem imagens, botoes ou cards.
- Estados de erro aparecem no mesmo padrao dos frames de erro.

Fluxos obrigatorios:

### E2E-01 - Login Demo

1. Abrir `/login`.
2. Clicar em entrar como demo.
3. Ver feed.

Aceite:

- URL final e `/`.
- Nome do usuario aparece.
- Feed renderiza pelo menos um post ou empty state valido.

### E2E-02 - Criar Post

1. Logar.
2. Ir para criar post.
3. Preencher titulo, descricao e privacidade.
4. Publicar.
5. Ver post no feed ou perfil.

Aceite:

- Post aparece com titulo correto.

### E2E-03 - Curtir E Comentar

1. Abrir feed.
2. Curtir post.
3. Ver contador mudar.
4. Comentar.
5. Ver comentario.

### E2E-04 - Seguir Usuario

1. Abrir perfil de outro usuario.
2. Clicar seguir.
3. Ver botao mudar para seguindo.
4. Abrir lista de seguindo.
5. Ver usuario na lista.

### E2E-05 - IA Com Fallback

1. Abrir criar post.
2. Acionar gerar legenda.
3. Simular provider indisponivel.
4. Ver sugestao fallback.
5. Publicar manualmente.

## API Contract Tests

Objetivo:

- Garantir que frontend e backend falam o mesmo idioma.

Estratégias:

- Gerar OpenAPI pelo backend.
- Conferir exemplos principais.
- Opcional: gerar tipos TypeScript a partir do OpenAPI.

Casos:

- `PostResponse` contem `author`, `stats`, `viewerState`.
- Erro de validacao contem `errors`.
- Pagina contem `items`, `nextCursor`, `hasNext`.

## Performance Basica

Ferramenta:

- k6 opcional.
- Apache Bench ou script simples para estudo.

Cenarios:

- Login.
- Carregar feed.
- Criar post.
- Curtir post.

Metas para demo:

- Feed local p95 abaixo de 300 ms com seed pequeno.
- Login local p95 abaixo de 500 ms.
- Criar post local p95 abaixo de 500 ms sem IA.

No Render Free:

- Aceitar cold start.
- Depois de acordado, API deve responder de forma aceitavel para demo.

## Test Data

Seed de teste:

- Usuario A.
- Usuario B.
- Usuario C deletado.
- Posts publicos.
- Posts privados.
- Follow A -> B.
- Like A -> post B.

Regra:

- Testes nao devem depender de dados do ambiente demo.
- Integration tests criam seus proprios dados.

## CI

GitHub Actions recomendado:

Jobs:

- Backend tests.
- Frontend tests.
- Frontend build.
- Optional E2E smoke.

Pipeline:

```text
pull request
  -> backend mvn test
  -> frontend npm test
  -> frontend npm run build
main
  -> deploy frontend
  -> deploy backend
```

## Checklist Antes De Entregar

- Backend unit tests passam.
- Backend integration tests passam.
- Frontend build passa.
- Playwright smoke passa localmente.
- Swagger abre.
- Health check responde.
- Usuario demo funciona.
- Post privado nao vaza.
- IA com fallback funciona.
- README atualizado com links reais.
