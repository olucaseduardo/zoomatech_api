# Zoomatech API

API desenvolvida com Spring Boot para gerenciamento de serviços, membros, eventos e configurações de sistema da ZoomaTech Jr.

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 4**
- **Spring Data JPA**
- **Spring Security (JWT)**
- **PostgreSQL**
- **Flyway** (Migrações de banco de dados)
- **AWS S3 / Cloudflare R2** (Upload de arquivos)
- **Docker & Docker Compose**
- **Lombok**

## Requisitos

- Java 21 ou superior
- Docker e Docker Compose
- Gradle (opcional, incluído via `gradlew`)

## Configuração

1. Clone o repositório.
2. Crie um arquivo `.env` na raiz do projeto baseado no arquivo `.env-example`:
   ```bash
   cp .env-example .env
   ```
3. Preencha as variáveis de ambiente no arquivo `.env` com as suas credenciais.

## Como Executar

### Via Docker Compose

```bash
docker-compose up --build
```

A API estará disponível em `http://localhost:8080`.

### Localmente (Desenvolvimento)

Certifique-se de que o banco de dados PostgreSQL esteja rodando e as variáveis de ambiente estejam configuradas, então execute:

```bash
./gradlew bootRun
```

## Documentação da API

As requisições estão documentadas através de coleções do Postman localizadas na pasta `/postman/collections`.

### Recursos

| Recurso | Descrição |
|---|---|
| **Auth** | Registro e login de usuários |
| **Users** | Gerenciamento de usuários |
| **Members** | CRUD de membros da equipe com upload de foto |
| **Roles** | CRUD de cargos e funções |
| **Services** | Gerenciamento de serviços oferecidos |
| **Work Performed** | CRUD de trabalhos realizados com upload de foto |
| **Events** | CRUD de eventos com categorias (Realizados / Participação) |
| **Configurations** | Configurações dinâmicas do sistema |
| **Public** | Endpoint agregado para o website |

## Segurança

A aplicação utiliza JWT (JSON Web Token) para autenticação. Certifique-se de definir uma chave `JWT_SECRET` segura no seu arquivo `.env`.
