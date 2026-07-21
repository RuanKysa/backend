# 📖 Guia de Uso do Swagger

## 🌐 Acessando o Swagger UI

Após iniciar a aplicação, acesse:

**http://localhost:8080/swagger-ui.html**

## 📋 Visão Geral

O Swagger UI oferece uma interface visual interativa para explorar e testar todos os endpoints da API.

### URLs Disponíveis

- **Swagger UI (Interface Visual)**: http://localhost:8080/swagger-ui.html
- **OpenAPI Spec (JSON)**: http://localhost:8080/api-docs
- **OpenAPI Spec (YAML)**: http://localhost:8080/api-docs.yaml

## 🔍 Como Usar

### 1. Visualizar Endpoints

Todos os endpoints estão organizados por tags:
- **Matrículas**: Operações CRUD completo de matrículas

### 2. Testar um Endpoint

#### Criar uma Matrícula (POST /api/matriculas)

1. Clique em **POST /api/matriculas**
2. Clique no botão **Try it out**
3. Edite o JSON de exemplo no campo Request body:

```json
{
  "nomeCompleto": "João da Silva",
  "dataNascimento": "2010-05-15",
  "turnoSCFV": "manha",
  "cpf": "12345678901",
  "possuiDeficiencia": false,
  "utilizaTransporte": true,
  "almoco": true,
  "naturalidade": "São Paulo",
  "municipio": "São Paulo",
  "uf": "SP",
  "pais": "Brasil"
}
```

4. Clique em **Execute**
5. Veja a resposta em **Response body**

#### Listar Todas as Matrículas (GET /api/matriculas)

1. Clique em **GET /api/matriculas**
2. Clique em **Try it out**
3. Opcionalmente preencha os filtros (busca, status, datas)
4. Clique em **Execute**
5. Veja a lista de matrículas retornada

#### Buscar por ID (GET /api/matriculas/{id})

1. Clique em **GET /api/matriculas/{id}**
2. Clique em **Try it out**
3. Cole o ID da matrícula no campo **id**
4. Clique em **Execute**
5. Veja os detalhes da matrícula

#### Atualizar Status (PATCH /api/matriculas/{id}/status)

1. Clique em **PATCH /api/matriculas/{id}/status**
2. Clique em **Try it out**
3. Cole o ID no campo **id**
4. Edite o body:

```json
{
  "status": "aprovada"
}
```

5. Clique em **Execute**

Status válidos:
- `pendente`
- `aprovada`
- `rejeitada`
- `aguardando_documentos`

#### Assinar Matrícula (PATCH /api/matriculas/{id}/assinatura)

1. Clique em **PATCH /api/matriculas/{id}/assinatura**
2. Clique em **Try it out**
3. Cole o ID no campo **id**
4. Adicione a assinatura em base64:

```json
{
  "assinatura": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg=="
}
```

5. Clique em **Execute**

## 📊 Recursos do Swagger

### Schemas

Na parte inferior da página, você encontra a seção **Schemas** com todos os modelos de dados:

- **MatriculaRequestDTO**: Estrutura para criar/atualizar matrícula
- **MatriculaResponseDTO**: Estrutura de resposta da API
- **DocumentoAnexoDTO**: Estrutura de documentos anexados
- **FiltrosMatriculaDTO**: Filtros de busca

### Códigos de Resposta

Cada endpoint documenta seus possíveis códigos de resposta:

- **200**: Sucesso
- **201**: Criado com sucesso
- **204**: Deletado com sucesso
- **400**: Requisição inválida
- **404**: Recurso não encontrado
- **500**: Erro interno do servidor

### Validações

O Swagger mostra quais campos são obrigatórios e suas validações:
- ✅ **required**: Campo obrigatório
- 📏 **minLength/maxLength**: Tamanho mínimo/máximo
- 🔢 **pattern**: Padrão regex (CPF, telefone, etc.)

## 🎯 Exemplos Práticos

### Fluxo Completo de Teste

1. **Criar uma matrícula** (POST)
2. **Copiar o ID** retornado na resposta
3. **Buscar por ID** (GET) para verificar
4. **Atualizar dados** (PUT) se necessário
5. **Atualizar status** para "aprovada" (PATCH)
6. **Assinar matrícula** (PATCH)
7. **Ver estatísticas** (GET /stats)

### Buscar com Filtros

Teste diferentes combinações de filtros em **GET /api/matriculas**:

```
busca: João
status: pendente
dataInicio: 2024-01-01
dataFim: 2024-12-31
```

## 🔧 Personalização

### Ordernação

Os endpoints estão ordenados alfabeticamente por método HTTP (GET, POST, PUT, PATCH, DELETE).

### Servidores

O Swagger está configurado com dois ambientes:
- **Desenvolvimento**: http://localhost:8080
- **Produção**: https://api.exemplo.com (configurar conforme necessário)

## 💡 Dicas

1. **Sempre use "Try it out"** para habilitar o teste de um endpoint
2. **Copie os IDs** das respostas para usar em outros endpoints
3. **Verifique os schemas** para entender a estrutura dos dados
4. **Use os exemplos** fornecidos como base
5. **Atenção aos campos obrigatórios** marcados com asterisco (*)

## 📱 Alternativas ao Swagger

Se preferir usar outras ferramentas:

### Postman
Importe o arquivo `postman-collection.json` na raiz do projeto

### cURL
Use os comandos do README.md

### Insomnia
Use as mesmas URLs e estruturas JSON documentadas no Swagger

---

**Dúvidas?** Consulte a [documentação completa](README.md) do projeto.
