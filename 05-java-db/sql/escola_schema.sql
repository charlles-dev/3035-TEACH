-- ============================================
-- Script de criação do banco de dados
-- Sistema Escolar (Cursos, Alunos, Matrículas)
-- ============================================

-- Criar banco de dados (executar separadamente)
-- CREATE DATABASE escola_db;

-- Conectar ao banco escola_db antes de continuar
-- \c escola_db

-- ============================================
-- Tabela: cursos
-- ============================================
CREATE TABLE IF NOT EXISTS cursos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    carga_horaria INT NOT NULL
);

-- ============================================
-- Tabela: alunos
-- ============================================
CREATE TABLE IF NOT EXISTS alunos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    data_nascimento DATE
);

-- ============================================
-- Tabela: matriculas (relacionamento ManyToOne)
-- ============================================
CREATE TABLE IF NOT EXISTS matriculas (
    id BIGSERIAL PRIMARY KEY,
    aluno_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,
    data_matricula DATE,
    
    CONSTRAINT fk_matricula_aluno 
        FOREIGN KEY (aluno_id) 
        REFERENCES alunos(id) 
        ON DELETE CASCADE,
        
    CONSTRAINT fk_matricula_curso 
        FOREIGN KEY (curso_id) 
        REFERENCES cursos(id) 
        ON DELETE CASCADE
);

-- ============================================
-- Índices para otimização das listagens
-- ============================================
CREATE INDEX IF NOT EXISTS idx_matriculas_aluno ON matriculas(aluno_id);
CREATE INDEX IF NOT EXISTS idx_matriculas_curso ON matriculas(curso_id);
