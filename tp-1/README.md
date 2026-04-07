# Banco CRUD — Spring Boot

Sistema de gerenciamento de contas bancárias com operações CRUD, desenvolvido em Java com Spring Boot, Spring Data JPA e Lombok. A interface é via linha de comando (terminal), sem necessidade de frontend ou servidor web.

---

## Tecnologias

- Java 21
- Spring Boot 3.5.6
- Spring Data JPA
- Lombok
- H2 (banco em memória — padrão)
- MySQL (opcional, via profile)
- JUnit 5 + Mockito (testes)

---

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/org/example/banco/
│   │   ├── BancoCrudApplication.java       # Classe principal (CommandLineRunner)
│   │   ├── entity/
│   │   │   └── Conta.java                  # Entidade JPA — tabela conta
│   │   ├── repository/
│   │   │   └── ContaRepository.java        # Acesso ao banco via JPA
│   │   ├── service/
│   │   │   └── ContaService.java           # Regras de negócio
│   │   └── exception/
│   │       ├── ContaNaoEncontradaException.java
│   │       ├── SaldoInvalidoException.java
│   │       └── NomeInvalidoException.java
│   └── resources/
│       └── application.properties          # Configuração do banco H2
└── test/
    └── java/org/example/banco/
        ├── ContaServiceTest.java            # Testes unitários (Mockito)
        └── ContaServiceIntegracaoTest.java  # Testes de integração (H2)
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

Ao iniciar, a aplicação executa automaticamente uma demonstração das operações CRUD no terminal.

### Executar os testes

```bash
./mvnw test
```

---

## Operações disponíveis

| Operação | Método              | Descrição                          |
|----------|---------------------|------------------------------------|
| Create   | `incluirConta`      | Cria uma nova conta bancária       |
| Read     | `consultarConta`    | Busca uma conta pelo ID            |
| Read     | `consultarTodasContas` | Lista todas as contas           |
| Update   | `alterarSaldo`      | Atualiza o saldo de uma conta      |
| Delete   | `excluirConta`      | Remove uma conta pelo ID           |

---

## Validações e Erros

O sistema lança exceções tipadas para entradas inválidas, evitando estados inconsistentes:

- `NomeInvalidoException` — nome nulo, vazio ou só espaços
- `SaldoInvalidoException` — saldo nulo ou negativo
- `ContaNaoEncontradaException` — ID não existe no banco

---

## Banco de Dados

Por padrão o projeto usa **H2 em memória**, sem necessidade de configuração adicional. Os dados são resetados a cada execução.

Para usar **MySQL**, ative o profile correspondente e configure as credenciais no `application-mysql.properties`:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
```

---

## Boas Práticas Aplicadas

- **Clean Code** — nomes descritivos, métodos curtos e com responsabilidade única
- **Imutabilidade** — a entidade `Conta` não possui setters; alterações criam nova instância via `comSaldo()`
- **CQS (Command Query Separation)** — métodos que alteram estado são separados dos que apenas consultam
- **Exceções tipadas** — cada tipo de erro tem sua própria exceção com mensagem contextualizada
- **Testes** — cobertura de cenários válidos, inválidos e limites com JUnit 5 e Mockito
