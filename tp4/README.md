# Banco CRUD — TP4 (Integrado)

Sistema bancário com CRUD completo, integrando os módulos desenvolvidos nos TPs anteriores.
Expõe uma API REST e uma interface web estática. Também pode rodar como CLI via profile `demo`.

---

## Tecnologias

- Java 21
- Spring Boot 3.5.6
- Spring Data JPA + Spring Validation
- Lombok
- H2 (padrão) / MySQL (opcional)
- JUnit 5 + Mockito + MockMvc
- JaCoCo (cobertura ≥ 85%)
- GitHub Actions (CI/CD)

---

## Estrutura

```
src/
├── main/java/org/example/banco/
│   ├── BancoCrudApplication.java
│   ├── entity/         Conta.java, ContaForm.java
│   ├── repository/     ContaRepository.java
│   ├── service/        ContaServicePort.java, ContaServiceImpl.java
│   ├── validation/     ValidadorConta.java
│   ├── controller/     ContaController.java
│   └── exception/      ContaNaoEncontradaException, NomeInvalidoException,
│                       SaldoInvalidoException, GlobalExceptionHandler
├── main/resources/
│   ├── application.properties
│   ├── application-mysql.properties
│   └── static/  index.html, css/style.css, js/app.js
└── test/java/org/example/banco/
    ├── ContaServiceTest.java
    ├── ValidadorContaTest.java
    ├── ContaControllerTest.java
    └── ContaServiceIntegracaoTest.java
```

---

## Como rodar

```bash
# API REST + interface web (padrão H2)
./mvnw spring-boot:run

# Demo CLI
./mvnw spring-boot:run -Dspring-boot.run.profiles=demo

# MySQL
./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
```

Após iniciar:
- Interface web: http://localhost:8080
- API REST: http://localhost:8080/api/contas
- Console H2: http://localhost:8080/h2-console

---

## Testes e cobertura

```bash
# Rodar todos os testes
./mvnw test

# Gerar relatório JaCoCo
./mvnw clean test jacoco:report
# Relatório: target/site/jacoco/index.html

# Verificar cobertura mínima (85%)
./mvnw jacoco:check
```

---

## Endpoints da API

| Método | Endpoint              | Descrição                   |
|--------|-----------------------|-----------------------------|
| GET    | `/api/contas`         | Lista todas as contas       |
| GET    | `/api/contas?nome=X`  | Filtra por nome             |
| GET    | `/api/contas/{id}`    | Busca por ID                |
| POST   | `/api/contas`         | Cria nova conta             |
| PUT    | `/api/contas/{id}`    | Atualiza nome e saldo       |
| DELETE | `/api/contas/{id}`    | Remove conta                |

Body esperado (POST / PUT):
```json
{ "nome": "Maria Silva", "saldo": 1500.00 }
```

---

## O que mudou em relação aos TPs anteriores

- Criada interface `ContaServicePort` — desacopla controller da implementação
- Criada classe `ValidadorConta` — centraliza validações que estavam duplicadas
- Método `comDados()` adicionado em `Conta` — suporta atualização de nome + saldo
- `GlobalExceptionHandler` trata `NomeInvalidoException` junto com as demais
- Workflows de CI/CD configurados em `.github/workflows/`
