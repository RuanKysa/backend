WITH repetidas AS (
    SELECT id, ROW_NUMBER() OVER (PARTITION BY matricula_id, horario_id ORDER BY data_inscricao, id) AS ordem
    FROM alunos_oficina WHERE matricula_id IS NOT NULL AND status <> 'CANCELADO'
)
UPDATE alunos_oficina SET status = 'CANCELADO'
WHERE id IN (SELECT id FROM repetidas WHERE ordem > 1);

WITH repetidas AS (
    SELECT id, ROW_NUMBER() OVER (PARTITION BY participante_avulso_id, horario_id ORDER BY data_inscricao, id) AS ordem
    FROM alunos_oficina WHERE participante_avulso_id IS NOT NULL AND status <> 'CANCELADO'
)
UPDATE alunos_oficina SET status = 'CANCELADO'
WHERE id IN (SELECT id FROM repetidas WHERE ordem > 1);

CREATE UNIQUE INDEX IF NOT EXISTS uk_inscricao_matricula_horario_ativa
    ON alunos_oficina(matricula_id, horario_id)
    WHERE matricula_id IS NOT NULL AND status <> 'CANCELADO';

CREATE UNIQUE INDEX IF NOT EXISTS uk_inscricao_avulso_horario_ativa
    ON alunos_oficina(participante_avulso_id, horario_id)
    WHERE participante_avulso_id IS NOT NULL AND status <> 'CANCELADO';

CREATE INDEX IF NOT EXISTS idx_inscricao_horario_status
    ON alunos_oficina(horario_id, status);
