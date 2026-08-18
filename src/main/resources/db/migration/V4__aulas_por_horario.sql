CREATE TABLE IF NOT EXISTS aulas (id varchar(255) PRIMARY KEY, oficina_id varchar(255) NOT NULL, horario_id varchar(255) NOT NULL, data_aula date NOT NULL, tema varchar(255), observacoes varchar(1000), status varchar(255) NOT NULL, criado_por varchar(255), data_criacao timestamp NOT NULL, CONSTRAINT uk_aula_oficina_horario_data UNIQUE (oficina_id, horario_id, data_aula));
ALTER TABLE presencas ADD COLUMN IF NOT EXISTS horario_id varchar(255);
ALTER TABLE presencas ADD COLUMN IF NOT EXISTS aula_id varchar(255);
UPDATE presencas p SET horario_id = ao.horario_id FROM alunos_oficina ao WHERE ao.id = p.aluno_oficina_id AND p.horario_id IS NULL;
INSERT INTO aulas (id, oficina_id, horario_id, data_aula, status, criado_por, data_criacao)
SELECT gen_random_uuid()::text, p.oficina_id, p.horario_id, p.data_aula, 'REALIZADA', MAX(p.registrado_por), MIN(p.data_registro)
FROM presencas p WHERE p.horario_id IS NOT NULL GROUP BY p.oficina_id, p.horario_id, p.data_aula
ON CONFLICT (oficina_id, horario_id, data_aula) DO NOTHING;
UPDATE presencas p SET aula_id = a.id FROM aulas a WHERE a.oficina_id = p.oficina_id AND a.horario_id = p.horario_id AND a.data_aula = p.data_aula AND p.aula_id IS NULL;
ALTER TABLE presencas ALTER COLUMN horario_id SET NOT NULL;
ALTER TABLE presencas ALTER COLUMN aula_id SET NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS uk_presenca_aluno_aula ON presencas(aluno_oficina_id, aula_id);
CREATE INDEX IF NOT EXISTS idx_aulas_horario_data ON aulas(horario_id, data_aula);
