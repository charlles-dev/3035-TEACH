# 🤝 Guia de Contribuição

Este guia explica como você pode contribuir com o projeto e participar do desenvolvimento.

## 📋 Índice

- [Como Contribuir](#como-contribuir)
- [Padrões de Código](#padrões-de-código)
- [Processo de Pull Request](#processo-de-pull-request)
- [Reportando Issues](#reportando-issues)
- [Code Review](#code-review)

## 🚀 Como Contribuir

1. **Fork o Repositório**
   - Acesse o [repositório principal](https://github.com/seu-usuario/3035-TEACH)
   - Clique no botão "Fork"

2. **Clone seu Fork**
   ```bash
   git clone https://github.com/seu-usuario/3035-TEACH.git
   cd 3035-TEACH
   ```

3. **Crie uma Branch**
   ```bash
   git checkout -b feature/nome-da-feature
   ```

4. **Faça suas Alterações**
   - Siga os padrões de código
   - Adicione testes quando necessário
   - Atualize a documentação

5. **Commit suas Mudanças**
   ```bash
   git add .
   git commit -m "feat: adiciona nova funcionalidade"
   ```

6. **Push para seu Fork**
   ```bash
   git push origin feature/nome-da-feature
   ```

7. **Crie um Pull Request**
   - Vá para o repositório original
   - Clique em "New Pull Request"
   - Selecione sua branch

## 📝 Padrões de Código

### Convenções de Commit

Seguimos o [Conventional Commits](https://www.conventionalcommits.org/):

- `feat:` - Nova funcionalidade
- `fix:` - Correção de bug
- `docs:` - Documentação
- `style:` - Formatação
- `refactor:` - Refatoração
- `test:` - Testes
- `chore:` - Manutenção

### Estilo de Código

#### Frontend (React/TypeScript)
- Use ESLint e Prettier
- Componentes funcionais e hooks
- Tipagem estrita com TypeScript

#### Backend (Java)
- Siga o Google Java Style Guide
- Use lombok para reduzir boilerplate
- Documentação JavaDoc em classes públicas

## 📥 Processo de Pull Request

1. **Antes de Submeter**
   - Execute todos os testes
   - Atualize a documentação
   - Verifique o lint/formatação

2. **Template do PR**
   ```markdown
   ## Descrição
   Descreva as mudanças feitas

   ## Tipo de mudança
   - [ ] Bug fix
   - [ ] Nova feature
   - [ ] Breaking change
   - [ ] Documentação

   ## Checklist
   - [ ] Testes adicionados/atualizados
   - [ ] Documentação atualizada
   - [ ] Lint passou
   ```

3. **Review Process**
   - Dois aprovadores necessários
   - CI deve passar
   - Conflicts resolvidos

## 🐛 Reportando Issues

### Template de Bug Report

```markdown
**Descrição do Bug**
Uma descrição clara do bug

**Para Reproduzir**
1. Vá para '...'
2. Clique em '....'
3. Veja o erro

**Comportamento Esperado**
O que deveria acontecer

**Screenshots**
Se aplicável

**Ambiente**
- OS: [Windows/Mac/Linux]
- Browser: [Chrome/Firefox/Safari]
- Versão: [versão]
```

### Template de Feature Request

```markdown
**Problema**
Descrição do problema que a feature resolve

**Solução Proposta**
Descrição da solução sugerida

**Alternativas Consideradas**
Outras soluções que você considerou

**Contexto Adicional**
Qualquer outro contexto relevante
```

## 👀 Code Review

### Checklist do Revisor

- [ ] O código segue os padrões do projeto
- [ ] A documentação foi atualizada
- [ ] Os testes cobrem os casos principais
- [ ] Não há código redundante
- [ ] A performance é aceitável
- [ ] Boas práticas de segurança

### Feedback Construtivo

- Seja específico e objetivo
- Explique o "porquê"
- Sugira melhorias
- Seja respeitoso

## 🏆 Reconhecimento

Todos os contribuidores são listados em [CONTRIBUTORS.md](./CONTRIBUTORS.md)

## ❓ Dúvidas

- Consulte o [FAQ](./FAQ)
- Abra uma issue com a tag `question`
- Contate os mantenedores