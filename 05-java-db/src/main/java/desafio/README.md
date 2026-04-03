# Escola System - Advanced Architecture CLI

Bem-vindo ao repositório do Sistema de Escola! Este projeto foi completamente refatorado para incorporar as **Práticas Ouro** do mercado de Backend Java. Aqui, consolidamos persistência de dados (JPA), validação impecável e separação cirúrgica de responsabilidades.

## 🛠️ Tecnologias Principais

- **Java 14+ (Java 25 recomendado)** suportando Records de DTO nativos.
- **Hibernate & JPA** com geração automática de tabelas e Constraints.
- **Docker & PostgreSQL** rodando como container isolado.
- **Hibernate Validator** para checagem em nível de objeto (Email, NotBlank, Min).
- **JLine3** para uma interface de terminal fluida, interativa e colorida.

---

## 🏛️ Desenhando a Arquitetura (Padrão Sênior)

Este sistema não é um simples amontoado de lógica. Ele foi estruturado baseando-se em Padrões Integrados de Aplicações Corporativas:

1. **Camada de Visão / Interação (`MainDesafio.java`)**:
   Só serve para interagir com o usuário, coletar textos brutos da tela preta do console e jogar no Serviço. Sem acessos diretos ao Banco!

2. **Camada Lógica (`EscolaService.java`)**:
   O coração puro da aplicação. O `Service` aplica regras rígidas ("Não é permitido aluno sem email!", "Aluno já cursa isso!"). Tudo é passado por um crivo rigoroso via instâncias do `ValidatorFactory.getValidator()`. Só o que sobrevive a essa barreira entra no nosso querido DAO.

3. **Camada de Recuperação de Dados (`EscolaDAO.java`)**:
   Puramente transacional. Os métodos do DAO pegam as Entidades purificadas do *Service* e lidam direto com o `EntityManager`. Retornando os acessos atômicos via JPQL.

4. **DTOs Otimizados (Java Records)**:
   Os DAOs puxam informações hiperpesadas com infinitas ramificações. Em vez de enviar o Banco inteiro na memória para o terminal (*Memory Leak*), o `Service` cria cópias leves e estáticas na memória `AlunoRecordDTO`, cortando relações imutáveis e permitindo listagens instantâneas.

5. **Soft-Delete (O pulo do gato)**:
   O projeto agora conta com Cancelamento. O "Deletar" do mundo corporativo nunca executa algo como `DELETE FROM table`. Com a variável de Status (*Ativa / Cancelada*), as matrículas que perdem validade mantém o rastro do tempo, preservando a lógica temporal da empresa.

---

## 🚀 Como Rodar o Banco Isolado

Fizemos uma separação inteligente para não conflitar com projetos anteriores deste Repositório. O sistema da Escola possui um container próprio.

Para evitar consumir recursos além do necessário do seu computador, inicie o container direcionado no Powershell:

```bash
docker compose up -d postgres_escola
```

> **Atenção:** Ele rodará silenciosamente na porta **5434**. As credenciais estão protegidas e autoconfiguradas no `persistence.xml` (sob a tag `<persistence-unit name="escolaPU">`).

Em seguida, basta rodar a classe principal pela sua IDE ou via terminal nativo no `MainDesafio.java`.

### A interface do Terminal

Essa versão abraça os novos modos criados:

- **[9] Atualizar Dados de Curso** (Utilizando `.merge()` da JPA)
- **[10] Cancelar Matrícula**  (Executando nosso Soft Delete)
- **Todas as listagens** agora utilizam filtros `WHERE status = 'ATIVA'` e `Records` encapsulados.
