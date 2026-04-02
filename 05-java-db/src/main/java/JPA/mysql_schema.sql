-- ============================================
-- Script de criação do banco de dados
-- Sistema de Gerenciamento de Eventos - MySQL
-- ============================================

-- Criar banco de dados
CREATE DATABASE IF NOT EXISTS eventos_db 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

-- Usar o banco de dados
USE eventos_db;

-- ============================================
-- Tabela: eventos
-- ============================================
CREATE TABLE IF NOT EXISTS eventos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    data DATE NOT NULL,
    local VARCHAR(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Tabela: participantes
-- ============================================
CREATE TABLE IF NOT EXISTS participantes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Tabela: inscricoes (relacionamento)
-- ============================================
CREATE TABLE IF NOT EXISTS inscricoes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
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
        ON DELETE CASCADE,
        
    INDEX idx_inscricoes_evento (evento_id),
    INDEX idx_inscricoes_participante (participante_id),
    INDEX idx_inscricoes_confirmada (confirmada)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Verificar tabelas criadas
-- ============================================
-- SHOW TABLES;
-- DESCRIBE eventos;
-- DESCRIBE participantes;
-- DESCRIBE inscricoes;
