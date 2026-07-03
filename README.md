# MoneyWise API

Sistema de gerenciamento financeiro pessoal via API REST, com autenticação JWT, cadastro de categorias e controle de transações (receitas e despesas).

## 📖 Descrição

O **MoneyWise** é uma API para controle de finanças pessoais. Ela permite que o usuário:

- Se autentique via JWT (login com usuário e senha);
- Gerencie **categorias** financeiras (ex: Alimentação, Transporte, Saúde);
- Registre **transações** (receitas e despesas), vinculando cada uma a uma categoria;
- Consulte suas transações com **paginação**, **ordenação** e **filtros** (por categoria ou por tipo — receita/despesa).

A aplicação é totalmente containerizada com Docker, incluindo o banco de dados PostgreSQL, e conta com documentação interativa via Swagger/OpenAPI.

## 🛠️ Tecnologias utilizadas

- **Java 21**
- **Spring Boot 4**
    - Spring Web (REST)
    - Spring Data JPA
    - Spring Security
    - Bean Validation (Jakarta Validation)
- **PostgreSQL 16**
- **JWT (JJWT)** — autenticação stateless baseada em token
- **Lombok**
- **Springdoc OpenAPI (Swagger UI)**
- **Docker & Docker Compose**
- **Maven**

## 🚀 Como executar com Docker

### Pré-requisitos

- Docker e Docker Compose instalados.

### Passo a passo

1. Clone o repositório:
   ```bash
   git clone <url-do-repositorio>
   cd moneywise
   ```

2. Suba os containers (aplicação + banco de dados):
   ```bash
   docker compose up --build
   ```

3. Aguarde os logs indicarem que a aplicação subiu com sucesso. A API estará disponível em:
   ```
   http://localhost:8080
   ```

O `compose.yaml` já sobe dois serviços:

| Serviço | Descrição | Porta |
|---|---|---|
| `db` | PostgreSQL 16 | `5432` |
| `app` | API MoneyWise | `8080` |

> O banco de dados é criado automaticamente (`MoneyWiseDB`) e as tabelas são geradas via `spring.jpa.hibernate.ddl-auto=update`. Um **seeder** (`DataSeeder`) popula o banco com um usuário administrador e dados de exemplo (categorias e transações) na primeira execução.

### Parar os containers

```bash
docker compose down
```

Para remover também o volume do banco de dados (apagando os dados persistidos):

```bash
docker compose down -v
```

## 📑 Como acessar o Swagger

Com a aplicação em execução, acesse a documentação interativa em:

```
http://localhost:8080/swagger-ui.html
```

A especificação OpenAPI (JSON) fica disponível em:

```
http://localhost:8080/v3/api-docs
```

> A maioria dos endpoints protegidos exige autenticação. No Swagger, use o botão **Authorize** e informe o token JWT no formato `Bearer <token>` (obtido através do endpoint de login).

## 🔑 Usuário e senha padrão

O `DataSeeder` cria automaticamente um usuário administrador na primeira inicialização da aplicação:

| Usuário | Senha | Papel |
|---|---|---|
| `admin` | `admin123` | `ADMIN` |

Use essas credenciais no endpoint `POST /auth/login` para obter o token JWT.

## 🔌 Exemplos de endpoints

### Autenticação

| Método | Endpoint | Descrição | Autenticação |
|---|---|---|---|
| `POST` | `/auth/login` | Autentica o usuário e retorna um token JWT | Não |

### Categorias

| Método | Endpoint | Descrição | Autenticação |
|---|---|---|---|
| `GET` | `/categories` | Lista todas as categorias | Não |
| `GET` | `/categories/{id}` | Busca uma categoria por ID | Não |
| `POST` | `/categories` | Cria uma nova categoria | Sim |
| `PUT` | `/categories/{id}` | Atualiza uma categoria existente | Sim |
| `DELETE` | `/categories/{id}` | Remove uma categoria | Sim |

### Transações

| Método | Endpoint | Descrição | Autenticação |
|---|---|---|---|
| `GET` | `/transactions` | Lista todas as transações (paginado) | Não |
| `GET` | `/transactions/{id}` | Busca uma transação por ID | Não |
| `POST` | `/transactions` | Cria uma nova transação | Sim |
| `PUT` | `/transactions/{id}` | Atualiza uma transação existente | Sim |
| `DELETE` | `/transactions/{id}` | Remove uma transação | Sim |
| `GET` | `/transactions/search?category={nome}` | Filtra transações por nome da categoria (paginado) | Não |
| `GET` | `/transactions/search?type={INCOME\|EXPENSE}` | Filtra transações por tipo (paginado) | Não |

> Todos os endpoints de listagem/busca de transações aceitam os parâmetros de paginação do Spring: `page`, `size` e `sort` (ex: `?page=0&size=10&sort=transactionDate,desc`).

## 📦 Exemplos de payloads

### Login — `POST /auth/login`

**Request**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Response** `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNzUxNTU1NTU1LCJleHAiOjE3NTE2NDE5NTV9.assinatura"
}
```

### Criar categoria — `POST /categories`

> Requer header `Authorization: Bearer <token>`

**Request**
```json
{
  "categoryName": "Educação"
}
```

**Response** `201 Created`
```json
{
  "id": 6,
  "name": "Educação"
}
```

### Criar transação — `POST /transactions`

> Requer header `Authorization: Bearer <token>`

**Request**
```json
{
  "description": "Compra de livros",
  "amount": 149.90,
  "transactionType": "EXPENSE",
  "transactionDate": "2026-07-03",
  "categoryId": 6
}
```

**Response** `201 Created`
```json
{
  "id": 13,
  "description": "Compra de livros",
  "amount": 149.90,
  "transactionType": "EXPENSE",
  "transactionDate": "2026-07-03",
  "categoryName": "Educação"
}
```

### Listar transações (paginado) — `GET /transactions?page=0&size=5`

**Response** `200 OK`
```json
{
  "content": [
    {
      "id": 1,
      "description": "Grocery shopping",
      "amount": 89.90,
      "transactionType": "EXPENSE",
      "transactionDate": "2026-07-02",
      "categoryName": "Food"
    }
  ],
  "totalElements": 12,
  "totalPages": 3,
  "number": 0,
  "size": 5
}
```

### Erro de autenticação — `401 Unauthorized`

```json
{
  "timestamp": "2026-07-03T10:15:30",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid or missing authentication token",
  "errors": null
}
```

## 🖼️ Prints da API funcionando

### Swagger UI
![Swagger UI](imgreadme/swagger.png)

### Login retornando o token JWT
![Login](imgreadme/login.png)

### Criação de uma transação
![Criar transação](imgreadme/transacao.png)

### Listagem paginada de transações
![Listagem paginada](imgreadme/listagem.png)

## ⚙️ Principais funcionalidades

- **Autenticação via JWT**: login stateless, sem sessão no servidor. O token é validado em cada requisição por um filtro (`JwtAuthenticationFilter`), e falhas de autenticação retornam um JSON padronizado de erro (`JwtAuthenticationEntryPoint`).
- **Controle de acesso por rota**: leituras (`GET`) em `/categories` e `/transactions` são públicas; criação, atualização e remoção exigem token válido (`SecurityConfig`).
- **CRUD de Categorias**: criação, listagem, busca por ID, atualização e remoção de categorias financeiras.
- **CRUD de Transações**: criação, listagem, busca por ID, atualização e remoção de transações, sempre vinculadas a uma categoria existente.
- **Busca e paginação de transações**: endpoint dedicado (`/transactions/search`) permite filtrar por categoria (busca parcial, case-insensitive) ou por tipo (`INCOME`/`EXPENSE`), com suporte a paginação e ordenação padrão por `id`.
- **Validações de entrada**: uso de Bean Validation nos DTOs (ex: descrição obrigatória entre 3 e 150 caracteres, valor mínimo positivo, campos obrigatórios), com mensagens de erro em português.
- **Tratamento de exceções**: recursos não encontrados (ex: transação ou categoria inexistente) retornam `404` com mensagem descritiva, via `ResourceNotFoundException`.
- **Seed automático de dados**: na primeira inicialização, a aplicação cria um usuário administrador padrão e popula o banco com categorias e transações de exemplo, facilitando os testes.
- **Documentação interativa**: toda a API é documentada via Swagger/OpenAPI, com suporte a autenticação Bearer diretamente na interface.
- **Ambiente containerizado**: aplicação e banco de dados sobem juntos via Docker Compose, com variáveis de ambiente configuráveis e healthcheck do banco antes de subir a aplicação.