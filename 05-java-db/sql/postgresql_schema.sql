-- ============================================
-- Script de criação do banco de dados
-- Sistema de Gerenciamento de Eventos - PostgreSQL
-- ============================================

-- Criar banco de dados (executar separadamente)
-- CREATE DATABASE eventos_db;

-- Conectar ao banco eventos_db antes de continuar
-- \c eventos_db

-- ============================================
-- Tabela: eventos
-- ============================================
CREATE TABLE IF NOT EXISTS eventos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    data DATE NOT NULL,
    local VARCHAR(200) NOT NULL
);

-- ============================================
-- Tabela: participantes
-- ============================================
CREATE TABLE IF NOT EXISTS participantes (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

-- ============================================
-- Tabela: inscricoes (relacionamento)
-- ============================================
CREATE TABLE IF NOT EXISTS inscricoes (
    id BIGSERIAL PRIMARY KEY,
    evento_id BIGINT NOT NULL,
    participante_id BIGINT NOT NULL,
    data_inscricao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    confirmada BOOLEAN NOT NULL DEFAULT FALSE,
    
    CONSTRAINT fk_inscricao_evento 
        FOREIGN KEY (evento_id) 
        REFERENCES eventos(id) 
        ON DELETE CASCADE,
        
    CONSTRAINT fk_inscricao_participante 
        FOREIGN KEY (participante_id) 
        REFERENCES participantes(id) 
        ON DELETE CASCADE
);

-- ============================================
-- Índices para melhor performance
-- ============================================
CREATE INDEX IF NOT EXISTS idx_inscricoes_evento 
    ON inscricoes(evento_id);

CREATE INDEX IF NOT EXISTS idx_inscricoes_participante 
    ON inscricoes(participante_id);

CREATE INDEX IF NOT EXISTS idx_inscricoes_confirmada 
    ON inscricoes(confirmada);

-- ============================================
-- Verificar tabelas criadas
-- ============================================
-- SELECT table_name FROM information_schema.tables 
-- WHERE table_schema = 'public' AND table_type = 'BASE TABLE';
