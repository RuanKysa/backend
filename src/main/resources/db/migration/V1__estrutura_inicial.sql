-- Estrutura inicial versionada e segura para a instalacao preexistente.
CREATE TABLE IF NOT EXISTS responsaveis (id varchar(255) PRIMARY KEY, nome varchar(255) NOT NULL, tipo varchar(255) NOT NULL, email varchar(255), telefone varchar(255), especialidade varchar(255), data_admissao date, anexo_criminal text);
CREATE TABLE IF NOT EXISTS oficinas (id varchar(255) PRIMARY KEY, nome varchar(255) NOT NULL, descricao varchar(2000), categoria varchar(255) NOT NULL, responsavel_id varchar(255) REFERENCES responsaveis(id), idade_minima integer, idade_maxima integer, vagas_totais integer NOT NULL, vagas_disponiveis integer NOT NULL, data_inicio date NOT NULL, data_fim date NOT NULL, local varchar(255), sala varchar(255), status varchar(255) NOT NULL, data_criacao timestamp NOT NULL, data_atualizacao timestamp, criado_por varchar(255));
CREATE TABLE IF NOT EXISTS horarios (id varchar(255) PRIMARY KEY, dia_semana varchar(255) NOT NULL, hora_inicio varchar(255) NOT NULL, hora_fim varchar(255) NOT NULL, vagas integer NOT NULL, oficina_id varchar(255) REFERENCES oficinas(id));
CREATE TABLE IF NOT EXISTS agentes_cidadania (id varchar(255) PRIMARY KEY, nome varchar(255) NOT NULL, data_nascimento date, cpf varchar(255), email varchar(255), telefone varchar(255), endereco varchar(500), data_inicio date NOT NULL, status varchar(255) NOT NULL);
CREATE TABLE IF NOT EXISTS agente_oficina (agente_id varchar(255) NOT NULL REFERENCES agentes_cidadania(id), oficina_id varchar(255) NOT NULL REFERENCES oficinas(id), PRIMARY KEY (agente_id, oficina_id));
CREATE TABLE IF NOT EXISTS oficina_materiais (oficina_id varchar(255) NOT NULL REFERENCES oficinas(id), material varchar(255));

CREATE TABLE IF NOT EXISTS matriculas (
 id varchar(255) PRIMARY KEY, nome_completo varchar(255) NOT NULL, data_nascimento date NOT NULL, idade integer, turnoscfv varchar(255), naturalidade varchar(255), municipio varchar(255), uf varchar(255), pais varchar(255), rg varchar(255), rguf varchar(255), data_expedicao date, cpf varchar(255), cad_unico_aluno varchar(255), cad_unico_responsavel varchar(255), etnia varchar(255), programa_social varchar(255), quantas_pessoas_residencia integer, nome_mae varchar(255), rg_mae varchar(255), uf_mae varchar(255), nome_pai varchar(255), rg_pai varchar(255), uf_pai varchar(255), nome_responsavel varchar(255), parentesco varchar(255), endereco varchar(255), numero_endereco varchar(255), complemento varchar(255), bairro varchar(255), posto_de_saude varchar(255), telefone varchar(255), telefone_outro varchar(255), escola varchar(255), serie varchar(255), turno varchar(255), possui_deficiencia boolean NOT NULL, tipo_deficiencia varchar(255), observacoes text, usa_remedios_controlados boolean, observacao_remedios text, agentes_cidadania boolean, data_inicio_agente date, utiliza_transporte boolean NOT NULL, local_embarque varchar(255), local_desembarque varchar(255), almoco boolean NOT NULL, encaminhado_por varchar(255), data_encaminhamento date, status varchar(255) NOT NULL, data_cadastro timestamp NOT NULL, data_atualizacao timestamp, assinatura text, matricula_assinada boolean NOT NULL
);
CREATE TABLE IF NOT EXISTS documentos_anexo (id varchar(255) PRIMARY KEY, tipo varchar(255) NOT NULL, nome_arquivo varchar(255) NOT NULL, url text, data_upload timestamp NOT NULL, matricula_id varchar(255) REFERENCES matriculas(id));
CREATE TABLE IF NOT EXISTS alunos_oficina (id varchar(255) PRIMARY KEY, matricula_id varchar(255), participante_avulso_id varchar(255), origem varchar(30), nome_completo varchar(255) NOT NULL, idade integer, turno varchar(255), telefone varchar(255), nome_responsavel varchar(255), observacoes varchar(1000), data_inscricao timestamp NOT NULL, oficina_id varchar(255) NOT NULL, horario_id varchar(255) NOT NULL, status varchar(50) NOT NULL);
ALTER TABLE alunos_oficina ADD COLUMN IF NOT EXISTS participante_avulso_id varchar(255);
ALTER TABLE alunos_oficina ADD COLUMN IF NOT EXISTS origem varchar(30);
ALTER TABLE alunos_oficina ALTER COLUMN matricula_id DROP NOT NULL;
UPDATE alunos_oficina SET origem = CASE WHEN matricula_id IS NULL THEN 'AVULSO' ELSE 'MATRICULA' END WHERE origem IS NULL;
CREATE TABLE IF NOT EXISTS distribuicao_alunos (id varchar(255) PRIMARY KEY, aluno_id varchar(255) NOT NULL, oficina_id varchar(255) NOT NULL, horario_id varchar(255) NOT NULL, data_inscricao timestamp NOT NULL, status varchar(255) NOT NULL, observacoes varchar(1000));
CREATE TABLE IF NOT EXISTS presencas (id varchar(255) PRIMARY KEY, aluno_oficina_id varchar(255) NOT NULL, oficina_id varchar(255) NOT NULL, data_aula date NOT NULL, status varchar(255) NOT NULL, observacao varchar(500), data_registro timestamp NOT NULL, registrado_por varchar(255), CONSTRAINT uk_presenca_aluno_data UNIQUE (aluno_oficina_id, data_aula));
CREATE TABLE IF NOT EXISTS usuarios (id varchar(255) PRIMARY KEY, nome varchar(160) NOT NULL, email varchar(180) NOT NULL UNIQUE, senha_hash varchar(255) NOT NULL, perfil varchar(30) NOT NULL, status varchar(30) NOT NULL, data_criacao timestamp NOT NULL, ultimo_acesso timestamp);

CREATE INDEX IF NOT EXISTS idx_matriculas_nome ON matriculas(nome_completo);
CREATE INDEX IF NOT EXISTS idx_matriculas_cpf ON matriculas(cpf);
CREATE INDEX IF NOT EXISTS idx_matriculas_status ON matriculas(status);
CREATE INDEX IF NOT EXISTS idx_documentos_matricula ON documentos_anexo(matricula_id);
CREATE INDEX IF NOT EXISTS idx_alunos_oficina_horario ON alunos_oficina(oficina_id, horario_id);
CREATE INDEX IF NOT EXISTS idx_alunos_oficina_avulso ON alunos_oficina(participante_avulso_id);
CREATE INDEX IF NOT EXISTS idx_presencas_oficina_data ON presencas(oficina_id, data_aula);
CREATE INDEX IF NOT EXISTS idx_presencas_aluno ON presencas(aluno_oficina_id);
