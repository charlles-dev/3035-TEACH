# 08 - Seguranca

## Objetivo

Garantir que o TeachGram Pro seja seguro o suficiente para uma demo publica e demonstre boas praticas em entrevista.

Nao e objetivo:

- Certificacao.
- Compliance completo.
- Protecao contra todos os ataques de escala corporativa.

E objetivo:

- Evitar erros comuns.
- Proteger credenciais.
- Controlar acesso.
- Reduzir abuso de free tier.
- Mostrar consciencia de LGPD e privacidade.

## Autenticacao

Modelo:

- Access token JWT curto.
- Refresh token opaco persistido com hash.
- Logout revoga refresh token.

Access token:

- Expiracao sugerida: 15 minutos.
- Contem `sub`, `username`, `role`, `iat`, `exp`.
- Nao contem dados sensiveis.

Refresh token:

- Expiracao sugerida: 7 a 30 dias.
- Salvo no banco como hash.
- Rotacionado a cada refresh.
- Revogado no logout.

## Armazenamento De Token No Frontend

Para portfolio:

- Opcao simples: access token em memoria e refresh token em cookie httpOnly se backend e frontend permitirem.
- Alternativa pragmaticamente mais simples: localStorage com riscos documentados.

Recomendacao:

- Se tiver tempo, usar cookie httpOnly para refresh token.
- Se usar localStorage, declarar no README que e escolha de demo e reforcar protecoes de XSS.

## Senhas

Regras:

- Nunca salvar senha em texto puro.
- Usar BCrypt ou Argon2.
- Configurar custo razoavel para free tier.
- Nunca retornar `password_hash`.
- Nunca logar senha.

Validacao:

- Minimo 8 caracteres.
- Pelo menos uma letra.
- Pelo menos um numero.
- Recomendado permitir caracteres especiais.

Mensagem de erro:

- Nao indicar se email existe no login.
- No cadastro, pode indicar duplicidade de email/username.

## Autorizacao

Regras:

- Usuario so edita/deleta o proprio perfil.
- Usuario so edita/deleta os proprios posts.
- Usuario so ve post privado se for dono.
- Usuario so ve post followers se seguir o autor ou for dono.
- Admin tecnico nao deve ser necessario para fluxo normal.

Padrao:

- Validar permissao no service, nao apenas no controller.
- Tests devem cobrir `403`.

## CORS

Ambiente local:

```text
http://localhost:5173
```

Ambiente demo:

```text
https://teachgram-pro.vercel.app
```

Regras:

- Nao usar `*` com credenciais.
- Permitir apenas metodos necessarios.
- Permitir headers `Authorization`, `Content-Type`, `X-Request-Id`.

## Rate Limit

Endpoints criticos:

- `/auth/login`
- `/auth/signup`
- `/auth/refresh`
- `/ai/*`
- `/posts`
- `/comments`

Sugestao:

- Login: 5 tentativas por minuto por IP e identifier.
- Signup: 5 por hora por IP.
- IA: limite por usuario e por IP.
- Comentarios: 30 por hora por usuario.

Implementacao:

- Redis/Upstash em demo.
- Bucket4j ou implementacao simples com Redis.

Resposta:

- `429 Too Many Requests`.
- Header `Retry-After` quando possivel.

## Validacao De Entrada

Backend:

- Bean Validation em DTOs.
- Nunca confiar no frontend.
- Sanitizar strings para tamanho e formato.
- Validar URLs de midia.

Frontend:

- Zod para feedback rapido.
- Mesmas regras principais do backend.

Campos:

- `title`: 1 a 50.
- `description`: ate 200.
- `comment`: 1 a 500.
- `bio`: ate 500.
- `username`: 3 a 40, letras, numeros, ponto e underline.
- `email`: formato valido.

## Protecao Contra XSS

Regras:

- React escapa texto por padrao.
- Nao usar `dangerouslySetInnerHTML` para conteudo de usuario.
- Links de midia devem ser tratados como URL, nao HTML.
- Sanitizar qualquer markdown futuro.

## Protecao Contra SQL Injection

Regras:

- Usar JPA repositories e query parameters.
- Nunca concatenar input em SQL.
- Em queries nativas, usar parametros nomeados.

## Protecao De Midia

Primeira versao:

- Aceitar URL externa.
- Validar protocolo `https`.
- Limitar tamanho da URL.

Versao portfolio:

- Supabase Storage.
- Buckets publicos para demo ou URLs assinadas se quiser privacidade.
- Validar tipo de arquivo.
- Limitar tamanho.

Riscos:

- Hotlinking.
- Conteudo externo quebrado.
- Conteudo ofensivo hospedado fora.

Mitigacao:

- Mostrar fallback image quando URL falhar.
- Moderacao opcional.
- Aviso em README sobre demo.

## LGPD Basica

Dados pessoais tratados:

- Nome.
- Email.
- Telefone.
- Foto.
- Bio.
- Conteudo de posts.

Medidas:

- Usuario pode excluir conta logicamente.
- Senha e token protegidos.
- Nao expor email/telefone no perfil publico por padrao.
- Nao enviar email/telefone para IA.
- Logs nao devem conter dados sensiveis.

Para evolucao:

- Exportar dados do usuario.
- Exclusao fisica agendada.
- Politica de privacidade.
- Consentimento para IA.

## Secrets

Nunca commitar:

- `GROQ_API_KEY`
- `JWT_SECRET`
- `DATABASE_URL`
- `REDIS_URL`
- `SUPABASE_SERVICE_ROLE_KEY`

Arquivos permitidos:

- `.env.example` com nomes sem valores reais.

Variaveis minimas:

```text
DATABASE_URL=
DATABASE_USERNAME=
DATABASE_PASSWORD=
JWT_SECRET=
JWT_ACCESS_TOKEN_TTL=
JWT_REFRESH_TOKEN_TTL=
REDIS_URL=
REDIS_TOKEN=
GROQ_API_KEY=
AI_ENABLED=
CORS_ALLOWED_ORIGINS=
```

## Headers De Seguranca

Frontend:

- `Content-Security-Policy` quando possivel.
- `X-Content-Type-Options: nosniff`.
- `Referrer-Policy`.

Backend:

- Spring Security headers.
- Desabilitar stack trace em responses.

## Auditoria

Eventos a auditar:

- Signup.
- Login sucesso/falha agregada.
- Logout.
- Delete account.
- Create/update/delete post.
- Mudanca de privacidade.
- Falhas de permissao.
- Uso de IA.
- Moderacao.

Nao auditar com dados sensiveis:

- Senhas.
- Tokens.
- Conteudo completo de prompts.

## Checklist De Seguranca

Antes de publicar demo:

- Senhas com hash.
- JWT secret forte.
- CORS restrito.
- Swagger publico sem endpoints sensiveis.
- Actuator restrito ou limitado.
- Rate limit em login e IA.
- `.env` fora do Git.
- Usuario demo sem role admin.
- Banco sem dados pessoais reais.
- Logs sem tokens.
- Post privado testado contra acesso de terceiros.

## Testes De Seguranca

Casos:

- Login com senha errada retorna `401`.
- Usuario A nao edita post de usuario B.
- Usuario A nao ve post privado de B.
- Token expirado falha.
- Refresh token revogado falha.
- Rate limit bloqueia excesso.
- Campos invalidos retornam `400`.
- SQL injection basica em busca nao quebra query.
- XSS em comentario aparece como texto, nao executa.

