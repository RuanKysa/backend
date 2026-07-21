# API de Matrícula - Backend

Sistema de gerenciamento de matrículas desenvolvido com Spring Boot 3.x e Java 17.

## 🚀 Tecnologias

- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- PostgreSQL
- Swagger/OpenAPI 3.0 (SpringDoc)
- Lombok
- Maven

## 📋 Pré-requisitos

- JDK 17 ou superior
- Maven 3.6+
- PostgreSQL 12+

## ⚙️ Configuração

### Banco de Dados

1. Criar banco de dados PostgreSQL:
```sql
CREATE DATABASE matricula_db;
```

2. Configurar credenciais em `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/matricula_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### Executar o projeto

```bash
# Compilar
mvn clean install

# Executar
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

## � Documentação Swagger/OpenAPI

Acesse a documentação interativa da API:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

O Swagger UI permite:
- ✅ Visualizar todos os endpoints disponíveis
- ✅ Testar as requisições diretamente no navegador
- ✅ Ver exemplos de request/response
- ✅ Validar schemas dos DTOs
- ✅ Documentação detalhada de cada endpoint

## �📚 Endpoints da API

### Matrículas

#### Criar Matrícula
```http
POST /api/matriculas
Content-Type: application/json

{
  "nomeCompleto": "João da Silva",
  "dataNascimento": "2010-05-15",
  "turnoSCFV": "manha",
  "cpf": "12345678901",
  "possuiDeficiencia": false,
  "utilizaTransporte": true,
  "almoco": true,
  ...
}
```

#### Listar Todas as Matrículas
```http
GET /api/matriculas
```

#### Buscar Matrícula por ID
```http
GET /api/matriculas/{id}
```

#### Atualizar Matrícula
```http
PUT /api/matriculas/{id}
Content-Type: application/json

{
  "nomeCompleto": "João da Silva Santos",
  ...
}
```

#### Deletar Matrícula
```http
DELETE /api/matriculas/{id}
```

#### Buscar com Filtros
```http
GET /api/matriculas?busca=João&status=pendente&dataInicio=2024-01-01&dataFim=2024-12-31
```

Parâmetros de query:
- `busca`: Busca por nome, CPF ou RG
- `status`: Filtrar por status (pendente, aprovada, rejeitada, aguardando_documentos)
- `dataInicio`: Data inicial (formato: yyyy-MM-dd)
- `dataFim`: Data final (formato: yyyy-MM-dd)

#### Buscar por Nome
```http
GET /api/matriculas/buscar/nome?nome=João
```

#### Buscar por CPF
```http
GET /api/matriculas/buscar/cpf?cpf=12345678901
```

#### Buscar por Status
```http
GET /api/matriculas/buscar/status?status=pendente
```

#### Atualizar Status
```http
PATCH /api/matriculas/{id}/status
Content-Type: application/json

{
  "status": "aprovada"
}
```

#### Assinar Matrícula
```http
PATCH /api/matriculas/{id}/assinatura
Content-Type: application/json

{
  "assinatura": "data:image/png;base64,..."
}
```

#### Estatísticas
```http
GET /api/matriculas/stats
```

## 📊 Estrutura de Dados

### Status da Matrícula
- `pendente`: Matrícula aguardando análise
- `aprovada`: Matrícula aprovada
- `rejeitada`: Matrícula rejeitada
- `aguardando_documentos`: Aguardando documentação

### Turnos SCFV
- `manha`: Manhã
- `tarde`: Tarde
- `integral`: Integral

### Etnia
- `branca`: Branca
- `negra`: Negra
- `parda`: Parda
- `amarela`: Amarela
- `indigena`: Indígena
- `nao_informar`: Não informar

### Programas Sociais
- `nao_possui`: Não possui
- `bolsa_familia`: Bolsa Família
- `bpc`: BPC
- `tarifa_social`: Tarifa Social

### Tipo de Deficiência
- `fisica`: Física
- `auditiva`: Auditiva
- `visual`: Visual
- `mental`: Mental
- `nao_possui`: Não possui

### Tipos de Documento
- `rg`: RG
- `cpf`: CPF
- `comprovante_residencia`: Comprovante de Residência
- `foto`: Foto
- `certidao_nascimento`: Certidão de Nascimento
- `declaracao_assinada`: Declaração Assinada
- `renovacao_assinada`: Renovação Assinada
- `ficha_natacao_assinada`: Ficha de Natação Assinada
- `desistencia_assinada`: Desistência Assinada
- `outro`: Outro

## 🔒 Validações

### Campos Obrigatórios
- Nome Completo (mínimo 3 caracteres)
- Data de Nascimento
- Possui Deficiência (boolean)
- Utiliza Transporte (boolean)
- Almoço (boolean)

### Formatos
- CPF: `11 dígitos` ou `formato XXX.XXX.XXX-XX`
- Telefone: `(XX) XXXXX-XXXX` ou `(XX) XXXX-XXXX`
- Datas: `yyyy-MM-dd`

## 🗂️ Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/example/matricula/
│   │       ├── application/
│   │       │   └── controller/
│   │       │       └── MatriculaController.java
│   │       ├── domain/
│   │       │   ├── dto/
│   │       │   │   ├── DocumentoAnexoDTO.java
│   │       │   │   ├── FiltrosMatriculaDTO.java
│   │       │   │   ├── MatriculaRequestDTO.java
│   │       │   │   └── MatriculaResponseDTO.java
│   │       │   ├── entity/
│   │       │   │   ├── DocumentoAnexo.java
│   │       │   │   └── Matricula.java
│   │       │   ├── mapper/
│   │       │   │   └── MatriculaMapper.java
│   │       │   ├── repository/
│   │       │   │   ├── DocumentoAnexoRepository.java
│   │       │   │   └── MatriculaRepository.java
│   │       │   └── service/
│   │       │       └── MatriculaService.java
│   │       ├── infrastructure/
│   │       │   ├── config/
│   │       │   │   └── CorsConfig.java
│   │       │   └── exception/
│   │       │       └── GlobalExceptionHandler.java
│   │       └── MatriculaApplication.java
│   └── resources/
│       └── application.properties
```

## 🧪 Testes

```bash
# Executar testes
mvn test
```

## 📝 Notas

- A API utiliza UUID para IDs
- Todas as datas são armazenadas em UTC
- Timestamps são gerados automaticamente
- O projeto usa Lombok para reduzir boilerplate
- CORS configurado para localhost:3000 e localhost:4200

## 🤝 Contribuindo

1. Fork o projeto
2. Crie sua feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT.
