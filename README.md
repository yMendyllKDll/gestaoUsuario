# API de Gerenciamento de Usuários e Endereços

API REST desenvolvida com Spring Boot para gerenciamento de usuários e endereços, incluindo integração com o serviço ViaCEP.

## Tecnologias Utilizadas

- Java 17
- Spring Boot 2.7.0
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT
- JUnit 5
- Mockito
- Swagger/OpenAPI

## Requisitos

- JDK 17
- Maven
- PostgreSQL

## Configuração do Banco de Dados

1. Crie um banco de dados PostgreSQL:
```sql
CREATE DATABASE usuario_db;
```

2. Configure as credenciais do banco no arquivo `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/usuario_db
spring.datasource.username=admin
spring.datasource.password=admin
```

## Instalação e Execução

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/gestaoUsuario.git
cd gestaoUsuario
```

2. Compile o projeto:
```bash
mvn clean install
```

3. Execute a aplicação:
```bash
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080/gestaoUsuarios`

## Documentação da API

A documentação da API está disponível através do Swagger UI:
- Swagger UI: `http://localhost:8080/gestaoUsuarios/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/gestaoUsuarios/api-docs`

## Endpoints Principais

### Usuários
- POST `/api/usuarios` - Criar usuário
- GET `/api/usuarios` - Listar usuários
- GET `/api/usuarios/{id}` - Buscar usuário por ID
- PUT `/api/usuarios/{id}` - Atualizar usuário
- DELETE `/api/usuarios/{id}` - Deletar usuário

### Endereços
- POST `/api/enderecos` - Criar endereço
- GET `/api/enderecos/usuario/{usuarioId}` - Listar endereços por usuário
- GET `/api/enderecos/{id}` - Buscar endereço por ID
- PUT `/api/enderecos/{id}` - Atualizar endereço
- DELETE `/api/enderecos/{id}` - Deletar endereço

### Autenticação
- POST `/api/auth/login` - Login (retorna token JWT com tipo do usuário)
- POST `/api/auth/registro` - Registro de novo usuário

## Testes

Execute os testes unitários e de integração:
```bash
mvn test
```

## Segurança

A API utiliza autenticação JWT com dois níveis de acesso:
- Admin: Pode gerenciar todos os usuários
- Usuário comum: Só pode acessar e modificar seus próprios dados

O token JWT inclui o tipo do usuário (ADMIN ou USUARIO) como uma claim adicional, permitindo controle de acesso baseado em roles.


