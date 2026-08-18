CREATE TABLE IF NOT EXISTS participantes_avulsos (
    id varchar(255) PRIMARY KEY,
    nome_completo varchar(255) NOT NULL,
    idade integer,
    turno varchar(255),
    telefone varchar(255),
    nome_responsavel varchar(255),
    observacoes varchar(1000),
    data_criacao timestamp NOT NULL,
    data_atualizacao timestamp
);

UPDATE alunos_oficina
SET participante_avulso_id = gen_random_uuid()::text
WHERE origem = 'AVULSO' AND participante_avulso_id IS NULL;

INSERT INTO participantes_avulsos
    (id, nome_completo, idade, turno, telefone, nome_responsavel, observacoes, data_criacao)
SELECT DISTINCT ON (participante_avulso_id)
    participante_avulso_id, nome_completo, idade, turno, telefone,
    nome_responsavel, observacoes, COALESCE(data_inscricao, CURRENT_TIMESTAMP)
FROM alunos_oficina
WHERE origem = 'AVULSO' AND participante_avulso_id IS NOT NULL
ON CONFLICT (id) DO NOTHING;

CREATE INDEX IF NOT EXISTS idx_participantes_avulsos_nome
    ON participantes_avulsos(nome_completo);
