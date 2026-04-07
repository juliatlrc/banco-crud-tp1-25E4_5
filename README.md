# Banco CRUD - TP5

![CI](https://github.com/juliatlrc/banco-crud-tp1-25E4_5/actions/workflows/ci.yml/badge.svg)
![Deploy](https://github.com/juliatlrc/banco-crud-tp1-25E4_5/actions/workflows/deploy.yml/badge.svg)
![Security](https://github.com/juliatlrc/banco-crud-tp1-25E4_5/actions/workflows/security.yml/badge.svg)

Projeto de CRUD bancário desenvolvido ao longo dos TPs da disciplina de Programação Backend.

## O que tem no projeto

- Spring Boot + Spring Data JPA
- Banco H2 pra desenvolvimento e testes, MySQL pra produção
- Lombok pra não precisar escrever getters/setters na mão
- Jacoco medindo cobertura de testes (mínimo 90%)
- Selenium pra validar o sistema depois do deploy
- CI/CD no GitHub Actions

## Como rodar

```bash
# com H2 (sem precisar instalar nada)
mvn spring-boot:run

# com MySQL (precisa ter o banco rodando)
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

## Estrutura

```
src/
├── main/java/org/example/bancocrud/
│   ├── entity/         -> Conta.java
│   ├── repository/     -> ContaRepository.java
│   ├── service/        -> ContaService.java
│   └── CrudT1Application.java
├── test/
│   ├── ContaServiceTest.java
│   └── SeleniumPostDeployTest.java
└── resources/
    ├── application.properties
    ├── application-h2.properties
    ├── application-mysql.properties
    └── application-test.properties

.github/workflows/
├── ci.yml        -> build e testes
├── security.yml  -> analise SAST com CodeQL
└── deploy.yml    -> deploy dev -> test -> prod
```

## Secrets necessários no GitHub

| Secret | Descrição |
|---|---|
| AWS_ROLE_ARN | Role IAM pra autenticação OIDC |
| PROD_DB_URL | URL do banco em produção |
| PROD_DB_USER | Usuário do banco em produção |
| PROD_DB_PASS | Senha do banco em produção |

---
Júlia Caroline Tallarico
