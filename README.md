# Zoomatech API

API desenvolvida com Spring Boot para gerenciamento de serviços, membros e configurações de sistema.

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 4**
- **Spring Data JPA**
- **Spring Security (JWT)**
- **PostgreSQL**
- **Flyway** (Migrações de banco de dados)
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
3. Preencha as variáveis de ambiente no arquivo `.env` com as suas credenciais de banco de dados e segredos JWT.

## Como Executar

### Via Docker Compose

O projeto está configurado para rodar em containers. Para iniciar a aplicação e o banco de dados:

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

As requisições da API estão documentadas através de coleções do Postman localizadas na pasta `/postman/collections`.

### Principais Recursos

- **Auth**: Registro e login de usuários.
- **Users**: Gerenciamento de usuários.
- **Members**: CRUD de membros da equipe (com suporte a upload de fotos).
- **Roles**: CRUD de cargos/funções.
- **Services**: Gerenciamento de serviços oferecidos.
- **Configurations**: Configurações dinâmicas do sistema.

## Segurança

A aplicação utiliza JWT (JSON Web Token) para autenticação. Certifique-se de definir uma chave `JWT_SECRET` segura no seu arquivo `.env`.
