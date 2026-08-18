-- Script de inicialização do banco de dados

-- Criar banco de dados (se não existir)
-- CREATE DATABASE matricula_db;

-- Conectar ao banco de dados
\c matricula_db;

-- Criar extensão para UUID (se necessário)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Comentários sobre as tabelas

COMMENT ON DATABASE matricula_db IS 'Banco de dados para o sistema de matrículas';

-- A estrutura e as alterações são gerenciadas pelo Flyway.
-- Consulte src/main/resources/db/migration e não adicione tabelas manualmente aqui.

-- Índices adicionais para melhorar performance de consultas

-- Será criado após a primeira execução da aplicação:

-- CREATE INDEX IF NOT EXISTS idx_matriculas_nome ON matriculas(nome_completo);
-- CREATE INDEX IF NOT EXISTS idx_matriculas_cpf ON matriculas(cpf);
-- CREATE INDEX IF NOT EXISTS idx_matriculas_status ON matriculas(status);
-- CREATE INDEX IF NOT EXISTS idx_matriculas_data_cadastro ON matriculas(data_cadastro);
-- CREATE INDEX IF NOT EXISTS idx_documentos_matricula_id ON documentos_anexo(matricula_id);
-- CREATE INDEX IF NOT EXISTS idx_documentos_tipo ON documentos_anexo(tipo);

-- Índices para otimização de consultas de presença/chamada
-- CREATE INDEX IF NOT EXISTS idx_presencas_oficina_data ON presencas(oficina_id, data_aula);
-- CREATE INDEX IF NOT EXISTS idx_presencas_aluno ON presencas(aluno_oficina_id);
-- CREATE INDEX IF NOT EXISTS idx_presencas_data ON presencas(data_aula);
-- CREATE INDEX IF NOT EXISTS idx_presencas_status ON presencas(status);
