# Banco CRUD — Spring Boot Web

Sistema de gerenciamento de contas bancárias com operações CRUD e interface web, desenvolvido em Java com Spring Boot, Spring Data JPA, Lombok e Spring Validation. A interface é servida como conteúdo estático pelo próprio Spring Boot, sem dependências de frameworks frontend.

---

## Tecnologias

- Java 21
- Spring Boot 3.5.6
- Spring Data JPA
- Spring Web + Spring Validation
- Lombok
- H2 (banco em memória — padrão)
- MySQL (opcional, via profile)
- JUnit 5 + Mockito + MockMvc (testes)
- JaCoCo (cobertura de código)

---

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/org/example/banco/
│   │   ├── BancoCrudApplication.java       # Classe principal
│   │   ├── entity/
│   │   │   ├── Conta.java                  # Entidade JPA — imutável com comSaldo()
│   │   │   └── ContaForm.java              # DTO para receber dados da API
│   │   ├── repository/
│   │   │   └── ContaRepository.java        # Acesso ao banco via JPA
│   │   ├── service/
│   │   │   └── ContaService.java           # Regras de negócio (CQS)
│   │   ├── controller/
│   │   │   └── ContaController.java        # Endpoints REST
│   │   └── exception/
│   │       ├── ContaNaoEncontradaException.java
│   │       ├── SaldoInvalidoException.java
│   │       └── GlobalExceptionHandler.java
│   └── resources/
│       ├── application.properties          # Configuração do banco H2
│       └── static/
│           ├── index.html                  # Interface web
│           ├── css/style.css
│           └── js/app.js
└── test/
    └── java/org/example/banco/
        ├── ContaServiceTest.java           # Testes unitários (Mockito)
        ├── ContaControllerTest.java        # Testes de integração web (MockMvc)
        └── ContaServiceIntegracaoTest.java # Testes de integração com H2
```

---

## Como Rodar

### Pré-requisitos

- JDK 21 instalado
- Maven (ou usar o wrapper `mvnw` já incluído no projeto)

### Executar a aplicação

```bash
# Linux / Mac
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

Após iniciar, acesse:

- **Interface web:** `http://localhost:8080`
- **Console H2:** `http://localhost:8080/h2-console`
- **API REST:** `http://localhost:8080/api/contas`

### Executar os testes

```bash
./mvnw test
```

### Gerar relatório de cobertura (JaCoCo)

```bash
./mvnw clean test
# Relatório disponível em: target/site/jacoco/index.html
```

---

## Endpoints da API

| Método | Endpoint              | Descrição                            |
|--------|-----------------------|--------------------------------------|
| GET    | `/api/contas`         | Lista todas as contas                |
| GET    | `/api/contas?nome=X`  | Filtra contas por nome               |
| GET    | `/api/contas/{id}`    | Busca conta por ID                   |
| POST   | `/api/contas`         | Cria nova conta                      |
| PUT    | `/api/contas/{id}`    | Atualiza conta existente             |
| DELETE | `/api/contas/{id}`    | Remove conta                         |

### Exemplo de body (POST / PUT)

```json
{
  "nome": "Maria Silva",
  "saldo": 1500.00
}
```

---

## Validações e Erros

O sistema aplica validações em múltiplas camadas:

- **Interface web (JavaScript):** valida antes de enviar a requisição
- **Controller:** Bean Validation via `@Valid` no `ContaForm`
- **Service:** validações explícitas com fail early antes de acessar o banco
- **GlobalExceptionHandler:** centraliza respostas de erro sem expor detalhes internos

Exceções tratadas:

- `ContaNaoEncontradaException` — ID não existe no banco (404)
- `SaldoInvalidoException` — saldo nulo, negativo ou acima do limite (400)
- `MethodArgumentNotValidException` — campos obrigatórios ausentes (400)
- `Exception` (genérica) — falhas inesperadas sem exposição de stack trace (500)

---

## Banco de Dados

Por padrão o projeto usa **H2 em memória**, sem necessidade de configuração adicional. Os dados são resetados a cada execução.

Para usar **MySQL**, configure o `application-mysql.properties` e ative o profile:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
```

---

## Boas Práticas Aplicadas

- **Clean Code** — nomes descritivos, métodos curtos com responsabilidade única
- **Imutabilidade** — a entidade `Conta` não possui setters; alterações usam `comSaldo()`
- **CQS (Command Query Separation)** — commands e queries separados no service
- **Fail early** — validações antes de qualquer operação no banco ou na UI
- **Fail gracefully** — erros inesperados retornam mensagem genérica sem expor internos
- **Exceções tipadas** — cada tipo de erro tem sua própria exceção com mensagem contextualizada
- **Segurança básica** — escapeHtml no JavaScript para prevenção de XSS
