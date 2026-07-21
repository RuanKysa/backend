# Sistema de Chamada/Presença

Este documento descreve os endpoints para gerenciar a presença de alunos nas oficinas.

## Visão Geral

O sistema de chamada permite:
- Listar alunos de uma oficina para fazer chamada
- Registrar presença, falta ou falta justificada
- Consultar histórico de presença por oficina ou por aluno
- Obter estatísticas de frequência dos alunos

## Entidade Presenca

```java
{
    "id": "UUID",
    "alunoOficinaId": "UUID",
    "oficinaId": "UUID",
    "dataAula": "2026-04-26",
    "status": "PRESENTE | FALTA | JUSTIFICADA",
    "observacao": "Texto opcional",
    "dataRegistro": "2026-04-26T10:30:00",
    "registradoPor": "Nome do professor"
}
```

## Endpoints

### 1. Listar Alunos para Chamada

**Endpoint:** `GET /api/chamadas/oficinas/{oficinaId}/alunos`

**Parâmetros:**
- `oficinaId` (path) - ID da oficina
- `data` (query, opcional) - Data da aula (formato: YYYY-MM-DD). Se não informado, usa a data atual.

**Resposta:** Lista de alunos confirmados na oficina com seus dados

```json
[
    {
        "alunoOficinaId": "abc-123",
        "nomeCompleto": "João da Silva",
        "idade": 12,
        "turno": "MANHA",
        "statusPresenca": "PRESENTE"  // Só aparece se já houver chamada na data
    }
]
```

**Exemplo de uso:**
```bash
# Listar alunos para chamada de hoje
GET http://localhost:3001/api/chamadas/oficinas/abc-123/alunos

# Listar alunos com status da chamada de uma data específica
GET http://localhost:3001/api/chamadas/oficinas/abc-123/alunos?data=2026-04-20
```

---

### 2. Registrar Chamada

**Endpoint:** `POST /api/chamadas/oficinas/{oficinaId}/registrar`

**Parâmetros:**
- `oficinaId` (path) - ID da oficina

**Body:**
```json
{
    "dataAula": "2026-04-26",
    "registradoPor": "Prof. Maria Santos",
    "presencas": [
        {
            "alunoOficinaId": "aluno-1",
            "status": "PRESENTE",
            "observacao": null
        },
        {
            "alunoOficinaId": "aluno-2",
            "status": "FALTA",
            "observacao": "Não avisou"
        },
        {
            "alunoOficinaId": "aluno-3",
            "status": "JUSTIFICADA",
            "observacao": "Atestado médico"
        }
    ]
}
```

**Resposta:** Lista das presenças registradas

**Observações:**
- Se já existir chamada registrada para um aluno naquela data, será atualizada
- Pode registrar um ou mais alunos de uma vez
- Status disponíveis: `PRESENTE`, `FALTA`, `JUSTIFICADA`

**Exemplo de uso:**
```bash
POST http://localhost:3001/api/chamadas/oficinas/abc-123/registrar
Content-Type: application/json

{
    "dataAula": "2026-04-26",
    "registradoPor": "Prof. João",
    "presencas": [
        {
            "alunoOficinaId": "aluno-1",
            "status": "PRESENTE"
        },
        {
            "alunoOficinaId": "aluno-2",
            "status": "FALTA"
        }
    ]
}
```

---

### 3. Histórico de Chamadas da Oficina

**Endpoint:** `GET /api/chamadas/oficinas/{oficinaId}/historico`

**Parâmetros:**
- `oficinaId` (path) - ID da oficina

**Resposta:** Lista completa de todas as chamadas da oficina, ordenadas por data (mais recente primeiro)

```json
[
    {
        "id": "presenca-123",
        "alunoOficinaId": "aluno-1",
        "nomeAluno": "João da Silva",
        "oficinaId": "oficina-abc",
        "nomeOficina": "Futebol",
        "dataAula": "2026-04-26",
        "status": "PRESENTE",
        "observacao": null,
        "dataRegistro": "2026-04-26T10:30:00",
        "registradoPor": "Prof. Maria"
    }
]
```

**Exemplo de uso:**
```bash
GET http://localhost:3001/api/chamadas/oficinas/abc-123/historico
```

---

### 4. Histórico de Presença do Aluno

**Endpoint:** `GET /api/chamadas/alunos/{alunoOficinaId}/historico`

**Parâmetros:**
- `alunoOficinaId` (path) - ID do aluno na oficina

**Resposta:** Lista de todas as presenças daquele aluno

```json
[
    {
        "id": "presenca-123",
        "alunoOficinaId": "aluno-1",
        "nomeAluno": "João da Silva",
        "oficinaId": "oficina-abc",
        "nomeOficina": "Futebol",
        "dataAula": "2026-04-26",
        "status": "PRESENTE",
        "observacao": null,
        "dataRegistro": "2026-04-26T10:30:00",
        "registradoPor": "Prof. Maria"
    }
]
```

**Exemplo de uso:**
```bash
GET http://localhost:3001/api/chamadas/alunos/aluno-123/historico
```

---

### 5. Estatísticas de Presença do Aluno

**Endpoint:** `GET /api/chamadas/alunos/{alunoOficinaId}/estatisticas`

**Parâmetros:**
- `alunoOficinaId` (path) - ID do aluno na oficina

**Resposta:** Contadores de presença e falta

```json
{
    "presencas": 18,
    "faltas": 2,
    "total": 20
}
```

**Exemplo de uso:**
```bash
GET http://localhost:3001/api/chamadas/alunos/aluno-123/estatisticas
```

---

## Fluxo de Uso Recomendado

### Fazer Chamada do Dia

1. **Listar alunos** da oficina para a data:
   ```
   GET /api/chamadas/oficinas/{oficinaId}/alunos?data=2026-04-26
   ```

2. **Registrar a chamada** com a lista de presença:
   ```
   POST /api/chamadas/oficinas/{oficinaId}/registrar
   ```

3. **Corrigir/Atualizar** chamada se necessário (mesmo endpoint de registro)

### Consultar Frequência

1. **Histórico da oficina:**
   ```
   GET /api/chamadas/oficinas/{oficinaId}/historico
   ```

2. **Frequência de um aluno:**
   ```
   GET /api/chamadas/alunos/{alunoOficinaId}/estatisticas
   ```

## Regras de Negócio

- ✅ Um aluno só pode ter um registro de presença por data
- ✅ Se registrar novamente na mesma data, o registro será atualizado
- ✅ Apenas alunos com status CONFIRMADO podem ter presença registrada
- ✅ A observação é opcional, mas recomendada para faltas justificadas
- ✅ O campo `registradoPor` identifica quem fez a chamada

## Exemplo Completo em Postman/Insomnia

```json
// 1. Criar uma oficina (se ainda não tiver)
POST /api/oficinas
{
    "nome": "Oficina de Futebol",
    "descricao": "Futebol para iniciantes",
    "categoria": "ESPORTE",
    "idadeMinima": 10,
    "idadeMaxima": 15,
    "vagasTotais": 20,
    "dataInicio": "2026-03-01",
    "dataFim": "2026-12-15",
    "status": "ABERTA"
}

// 2. Listar alunos para fazer chamada
GET /api/chamadas/oficinas/{oficinaId}/alunos?data=2026-04-26

// 3. Registrar presença
POST /api/chamadas/oficinas/{oficinaId}/registrar
{
    "dataAula": "2026-04-26",
    "registradoPor": "Prof. Carlos",
    "presencas": [
        {
            "alunoOficinaId": "aluno-1",
            "status": "PRESENTE"
        },
        {
            "alunoOficinaId": "aluno-2",
            "status": "FALTA",
            "observacao": "Faltou sem avisar"
        }
    ]
}

// 4. Ver histórico
GET /api/chamadas/oficinas/{oficinaId}/historico

// 5. Ver estatística de um aluno
GET /api/chamadas/alunos/{alunoOficinaId}/estatisticas
```

## Tabela no Banco de Dados

A tabela `presencas` será criada automaticamente pelo Hibernate com a seguinte estrutura:

```sql
CREATE TABLE presencas (
    id VARCHAR(36) PRIMARY KEY,
    aluno_oficina_id VARCHAR(36) NOT NULL,
    oficina_id VARCHAR(36) NOT NULL,
    data_aula DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    observacao VARCHAR(500),
    data_registro TIMESTAMP NOT NULL,
    registrado_por VARCHAR(255),
    UNIQUE (aluno_oficina_id, data_aula)
);
```

## Índices para Performance

Os seguintes índices são recomendados (já incluídos no `init.sql`):

```sql
CREATE INDEX idx_presencas_oficina_data ON presencas(oficina_id, data_aula);
CREATE INDEX idx_presencas_aluno ON presencas(aluno_oficina_id);
CREATE INDEX idx_presencas_data ON presencas(data_aula);
CREATE INDEX idx_presencas_status ON presencas(status);
```
