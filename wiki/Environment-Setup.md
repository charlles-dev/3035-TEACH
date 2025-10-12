# 💻 Configuração do Ambiente

Este guia detalha a configuração completa do ambiente de desenvolvimento para o curso.

## 🔧 Ferramentas Necessárias

### Controle de Versão
- Git 2.x ou superior
- Conta no GitHub

### Desenvolvimento Frontend
- Node.js (LTS - 18.x ou superior)
- npm ou yarn
- VS Code com extensões:
  - ESLint
  - Prettier
  - ES7+ React/Redux/React-Native snippets
  - vscode-styled-components

### Desenvolvimento Backend
- JDK 17 ou superior
- Maven 3.x ou Gradle 7.x
- IntelliJ IDEA ou VS Code com extensões:
  - Extension Pack for Java
  - Spring Boot Extension Pack
  - Debugger for Java

### Banco de Dados
- PostgreSQL 14.x ou superior
- DBeaver ou pgAdmin 4

## 📥 Processo de Instalação

### Windows

1. **Git**
   ```powershell
   winget install --id Git.Git -e --source winget
   ```

2. **Node.js**
   ```powershell
   winget install OpenJS.NodeJS.LTS
   ```

3. **JDK**
   ```powershell
   winget install Microsoft.OpenJDK.17
   ```

4. **VS Code**
   ```powershell
   winget install Microsoft.VisualStudioCode
   ```

5. **PostgreSQL**
   ```powershell
   winget install PostgreSQL.PostgreSQL
   ```

### macOS

1. **Homebrew**
   ```bash
   /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
   ```

2. **Ferramentas de Desenvolvimento**
   ```bash
   brew install git node@18 openjdk@17 postgresql
   brew install --cask visual-studio-code
   ```

### Linux (Ubuntu/Debian)

1. **Ferramentas Básicas**
   ```bash
   sudo apt update && sudo apt upgrade -y
   sudo apt install git curl wget
   ```

2. **Node.js**
   ```bash
   curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
   sudo apt install -y nodejs
   ```

3. **JDK**
   ```bash
   sudo apt install openjdk-17-jdk
   ```

4. **PostgreSQL**
   ```bash
   sudo apt install postgresql postgresql-contrib
   ```

## ⚙️ Configuração Pós-instalação

### Git

```bash
git config --global user.name "Seu Nome"
git config --global user.email "seu@email.com"
```

### Node.js

```bash
npm install -g npm@latest
npm install -g typescript ts-node
```

### Java

```bash
# Adicione ao seu ~/.bashrc ou ~/.zshrc
export JAVA_HOME=/path/to/your/jdk
export PATH=$JAVA_HOME/bin:$PATH
```

### Maven

```bash
# Adicione ao seu ~/.bashrc ou ~/.zshrc
export M2_HOME=/path/to/maven
export PATH=$M2_HOME/bin:$PATH
```

## 🔍 Verificação da Instalação

Execute os seguintes comandos para verificar se tudo foi instalado corretamente:

```bash
git --version
node --version
npm --version
java --version
mvn --version
psql --version
code --version
```

## 🛡️ Configurações de Segurança

### Configuração do SSH para GitHub

1. Gerar chave SSH:
   ```bash
   ssh-keygen -t ed25519 -C "seu@email.com"
   ```

2. Adicionar ao agente SSH:
   ```bash
   eval "$(ssh-agent -s)"
   ssh-add ~/.ssh/id_ed25519
   ```

3. Adicionar a chave pública ao GitHub:
   - Copie o conteúdo de `~/.ssh/id_ed25519.pub`
   - Adicione em GitHub → Settings → SSH Keys

## 📋 Checklist Final

- [ ] Git instalado e configurado
- [ ] Node.js instalado com npm
- [ ] JDK instalado e JAVA_HOME configurado
- [ ] Maven/Gradle instalado
- [ ] PostgreSQL instalado
- [ ] VS Code/IntelliJ com extensões
- [ ] Chaves SSH configuradas
- [ ] Repositório clonado localmente

## 🆘 Solução de Problemas

### Problemas Comuns

1. **Java não encontrado**
   - Verifique JAVA_HOME
   - Adicione Java ao PATH

2. **Node.js: comando não encontrado**
   - Reinstale Node.js
   - Verifique PATH

3. **Erro de permissão no PostgreSQL**
   - Configure pg_hba.conf
   - Redefina senha do postgres

### Suporte

Em caso de problemas:
- Consulte o [FAQ](./FAQ)
- Abra uma issue no GitHub
- Contate o suporte técnico