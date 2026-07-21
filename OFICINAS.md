# Módulo de Oficinas - Documentação

## Visão Geral

O módulo de oficinas foi implementado seguindo o padrão arquitetural do projeto, com estrutura completa de entidades JPA, DTOs, repositories, services, mappers e controllers REST.

## Estrutura Criada

### Entidades (JPA)

1. **Oficina** - Entidade principal que representa uma oficina
   - Relacionamento com Responsável (ManyToOne)
   - Relacionamento com Horários (OneToMany)
   - Suporta categorias: ESPORTE, ARTE, MUSICA, DANCA, ARTESANATO, INFORMATICA, IDIOMAS, OUTRAS
   - Status: PLANEJADA, ATIVA, SUSPENSA, ENCERRADA

2. **Responsavel** - Professores ou estagiários responsáveis
   - Tipos: PROFESSOR, ESTAGIARIO

3. **AgentesCidadania** - Agentes de cidadania vinculados
   - Status: ATIVO, INATIVO

4. **Horario** - Horários de funcionamento das oficinas
   - Dias da semana: SEGUNDA, TERCA, QUARTA, QUINTA, SEXTA, SABADO
   - Relacionamento ManyToMany com AlunoOficina

5. **AlunoOficina** - Representa alunos inscritos
   - Referência à matrícula existente
   - **oficina_id** (VARCHAR 255, NOT NULL) - ID da oficina
   - **horario_id** (VARCHAR 255, NOT NULL) - ID do horário específico
   - **status** (VARCHAR 50, DEFAULT 'confirmado') - Status: CONFIRMADO, PENDENTE, CANCELADO

6. **~~DistribuicaoAluno~~** - ❌ REMOVIDA (duplicação com AlunoOficina)

## Endpoints REST

Base URL: `/api/oficinas`

### Oficinas

- `POST /api/oficinas` - Criar nova oficina
- `GET /api/oficinas` - Listar todas (com filtros opcionais)
  - Parâmetros: categoria, status, responsavel, diaSemana, dataInicio, dataFim, pesquisa
- `GET /api/oficinas/{id}` - Buscar por ID
- `PUT /api/oficinas/{id}` - Atualizar oficina
- `DELETE /api/oficinas/{id}` - Deletar oficina
- `GET /api/oficinas/resumo` - Obter resumo estatístico

### Responsáveis

- `POST /api/oficinas/responsaveis` - Criar responsável
- `GET /api/oficinas/responsaveis` - Listar todos
- `GET /api/oficinas/responsaveis/{id}` - Buscar por ID
- `PUT /api/oficinas/responsaveis/{id}` - Atualizar responsável
- `DELETE /api/oficinas/responsaveis/{id}` - Deletar responsável

### Agentes de Cidadania

- `POST /api/oficinas/agentes-cidadania` - Criar agente
- `GET /api/oficinas/agentes-cidadania` - Listar todos
- `GET /api/oficinas/agentes-cidadania/{id}` - Buscar por ID
- `GET /api/oficinas/agentes-cidadania/{id}/oficinas` - Listar oficinas vinculadas ao agente
- `PUT /api/oficinas/agentes-cidadania/{id}` - Atualizar agente
- `DELETE /api/oficinas/agentes-cidadania/{id}` - Deletar agente

### Alunos de Oficina

- `POST /api/oficinas/alunos` - Inscrever aluno em oficina
- `GET /api/oficinas/alunos` - Listar todos os alunos inscritos
- `GET /api/oficinas/alunos/{id}` - Buscar inscrição por ID
- `GET /api/oficinas/alunos/matricula/{matriculaId}` - Listar oficinas de um aluno
- `GET /api/oficinas/alunos/oficina/{oficinaId}` - Listar alunos de uma oficina
- `PUT /api/oficinas/alunos/{id}` - Atualizar inscrição
- `DELETE /api/oficinas/alunos/{id}` - Cancelar inscrição

## Exemplos de Requisição

### Criar Oficina

```json
POST /api/oficinas
{
  "nome": "Futsal Infantil",
  "descricao": "Oficina de futsal para crianças",
  "categoria": "esporte",
  "responsavelId": "uuid-do-responsavel",
  "horarios": [
    {
      "diaSemana": "segunda",
      "horaInicio": "14:00",
      "horaFim": "16:00",
      "vagas": 20
    },
    {
      "diaSemana": "quarta",
      "horaInicio": "14:00",
      "horaFim": "16:00",
      "vagas": 20
    }
  ],
  "idadeMinima": 6,
  "idadeMaxima": 12,
  "vagasTotais": 40,
  "dataInicio": "2026-04-01",
  "dataFim": "2026-12-20",
  "local": "Quadra Poliesportiva",
  "sala": "Quadra 1",
  "materiais": ["Bola de futsal", "Coletes", "Cones"],
  "status": "ativa",
  "criadoPor": "admin"
}
```

### Criar Responsável

```json
POST /api/oficinas/responsaveis
{
  "nome": "João Silva",
  "tipo": "professor",
  "email": "joao.silva@email.com",
  "telefone": "(11) 98765-4321",
  "especialidade": "Educação Física",
  "dataAdmissao": "2025-01-15"
}
```

### Inscrever Aluno em Oficina

```json
POST /api/oficinas/alunos
{
  "matriculaId": "uuid-da-matricula",
  "nomeCompleto": "Maria Silva Santos",
  "idade": 10,
  "turno": "manha",
  "observacoes": "Aluna tem experiência prévia",
  "dataInscricao": "2026-03-16T10:30:00",
  "oficinaId": "uuid-da-oficina",
  "horarioId": "uuid-do-horario",
  "status": "confirmado"
}
```

## Funcionalidades Implementadas

✅ CRUD completo de oficinas
✅ CRUD completo de responsáveis
✅ CRUD completo de agentes de cidadania
✅ Sistema de inscrição de alunos (via AlunoOficina)
✅ Controle automático de vagas disponíveis
✅ Filtros avançados para busca de oficinas
✅ Resumo estatístico (dashboard)
✅ Validações de dados com Bean Validation
✅ Documentação Swagger/OpenAPI
✅ Tratamento de exceções
✅ Transações gerenciadas
✅ **Tabela única para alunos-oficina (sem duplicação)**

## Regras de Negócio

1. **Vagas**: 
   - Ao criar uma oficina, vagasDisponiveis = vagasTotais
   - Ao inscrever um aluno, vagasDisponiveis é decrementada
   - Ao cancelar inscrição, vagasDisponiveis é incrementada
   - Não permite inscrição se vagasDisponiveis = 0

2. **Horários**:
   - Uma oficina pode ter múltiplos horários
   - Horários suportam dias da semana específicos
   - Cada horário tem seu próprio limite de vagas

3. **Status de Oficina**:
   - PLANEJADA: Oficina em fase de planejamento
   - ATIVA: Oficina em funcionamento
   - SUSPENSA: Temporariamente suspensa
   - ENCERRADA: Finalizada

4. **Status de Inscrição (AlunoOficina)**:
   - CONFIRMADO: Inscrição confirmada
   - PENDENTE: Aguardando confirmação
   - CANCELADO: Inscrição cancelada

## ⚠️ Correções Importantes

### Problema Resolvido: Inconsistência de Tabelas

**Antes (duplicação):**
- ❌ `distribuicao_alunos` - registrava distribuição
- ❌ `horario_alunos` - tabela ManyToMany redundante  
- ✅ `alunos_oficina` - tinha campos mas não era usada

**Depois (solução):**
- ✅ `alunos_oficina` - **Única fonte da verdade**
  - Contém: matriculaId, oficinaId, horarioId, status
  - Controla inscrições e vagas automaticamente

**Mudanças técnicas:**
- Removida entidade `DistribuicaoAluno`
- Removido relacionamento ManyToMany em `Horario`
- Todos os endpoints agora usam `/api/oficinas/alunos`

## Próximos Passos

Para usar o módulo:

1. Compile o projeto: `mvn clean compile`
2. Execute os testes: `mvn test`
3. Inicie a aplicação: `mvn spring-boot:run`
4. Acesse a documentação Swagger: `http://localhost:8080/swagger-ui.html`

## Dependências

O módulo utiliza as mesmas dependências do projeto:
- Spring Boot 3.x
- Spring Data JPA
- Lombok
- Bean Validation
- Swagger/OpenAPI
- PostgreSQL (conforme configuração existente)
