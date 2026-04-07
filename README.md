# Banco CRUD — Projeto de Bloco

![CI](https://github.com/juliatlrc/banco-crud-tp1-25E4_5/actions/workflows/ci.yml/badge.svg)
![Deploy](https://github.com/juliatlrc/banco-crud-tp1-25E4_5/actions/workflows/deploy.yml/badge.svg)
![Security](https://github.com/juliatlrc/banco-crud-tp1-25E4_5/actions/workflows/security.yml/badge.svg)

Projeto desenvolvido ao longo da disciplina de Programação Backend. Cada TP evoluiu em cima do anterior, partindo de um CRUD simples em console até um sistema com API REST, testes automatizados e pipeline de CI/CD completo.

---

## Estrutura do repositório

```
banco-crud-tp1-25E4_5/
├── tp-1/       CRUD bancário em console (Spring Boot + JPA)
├── tp-2/       Interface web com Thymeleaf + testes Selenium
├── tp3/        API REST + frontend em HTML/JS
├── tp4/        Refatoração com arquitetura em camadas e validações
├── tp5/        Finalização, CI/CD, CodeQL e deploy automatizado
└── .github/
    └── workflows/
        ├── ci.yml
        ├── security.yml
        └── deploy.yml
```

---

## TP1 — CRUD Console

Primeiro trabalho da disciplina. O objetivo era criar um sistema bancário simples rodando no terminal usando Spring Boot e Spring Data JPA.

**Tecnologias:** Spring Boot, Spring Data JPA, H2, MySQL, Lombok

**O que foi feito:**
- Entidade `Conta` com id, nome e saldo
- Repositório JPA com os métodos básicos
- Service com os cinco métodos do CRUD: incluir, listar, buscar por id, alterar saldo e excluir
- Aplicação principal rodando via `CommandLineRunner`
- Testes unitários e de integração com H2 em memória

**Como rodar:**
```bash
cd tp-1
mvn spring-boot:run
```

---

## TP2 — Interface Web + Selenium

Adicionou uma interface web ao sistema bancário e os primeiros testes com Selenium.

**Tecnologias:** Spring Boot, Thymeleaf, Selenium WebDriver, Page Object Model

**O que foi feito:**
- Controller REST com endpoints para as operações CRUD
- Templates HTML com Thymeleaf para listar e cadastrar contas
- Testes Selenium usando o padrão Page Object (PaginaBase, PaginaListaContas, PaginaFormulario)
- Testes parametrizados com Selenium

**Como rodar:**
```bash
cd tp-2/banco-web
mvn spring-boot:run
# acessa http://localhost:8080
```

---

## TP3 — API REST + Frontend desacoplado

Separou o frontend do backend. A API passou a ser consumida por um frontend em HTML/CSS/JS puro.

**Tecnologias:** Spring Boot, REST API, HTML, CSS, JavaScript (fetch API)

**O que foi feito:**
- API REST completa com `@RestController`
- Frontend em HTML/JS consumindo a API via fetch
- Tratamento de exceções com `@ControllerAdvice`
- Testes unitários do controller com MockMvc
- Testes de integração do service

**Como rodar:**
```bash
cd tp3
mvn spring-boot:run
# acessa http://localhost:8080
```

---

## TP4 — Refatoração e arquitetura em camadas

Refatorou o projeto aplicando princípios de Clean Code e separando melhor as responsabilidades.

**Tecnologias:** Spring Boot, REST API, interfaces de serviço, validação customizada

**O que foi feito:**
- Interface `ContaServicePort` separando contrato da implementação
- `ContaServiceImpl` como implementação concreta
- `ValidadorConta` centralizado com as regras de negócio
- Exceções específicas: `ContaNaoEncontradaException`, `NomeInvalidoException`, `SaldoInvalidoException`
- `GlobalExceptionHandler` para tratar erros na API
- Cobertura de testes ampliada (unitários, integração, validação)

**Como rodar:**
```bash
cd tp4
mvn spring-boot:run
# acessa http://localhost:8080
```

---

## TP5 — CI/CD, CodeQL e Deploy automatizado

Etapa final do projeto. Foco em qualidade, automação e entrega contínua.

**Tecnologias:** GitHub Actions, JaCoCo, CodeQL, OIDC (AWS)

**O que foi feito:**

### Refatoração
- Entidade `Conta` sem setters — imutável, valores definidos só no construtor
- Cláusulas de guarda substituindo condicionais aninhadas no service
- Remoção de código morto e métodos não utilizados

### Workflows (`.github/workflows/`)

**ci.yml** — roda em todo push para main e develop:
- Build com Maven
- Execução dos testes (7 testes unitários + teste pós-deploy)
- Upload do relatório de testes como artefato
- Resumo em Markdown no GitHub

**security.yml** — roda em push na main e toda segunda-feira:
- Análise SAST com CodeQL
- Detecta vulnerabilidades no código Java

**deploy.yml** — deploy progressivo por ambiente:
- `dev` → `test` → `prod` (cada etapa depende da anterior)
- Ambiente `prod` com aprovação manual obrigatória
- Autenticação OIDC com AWS (sem chaves estáticas)

### Cobertura de testes
Medida com JaCoCo. Resultado: **96%** (meta mínima era 90%).

| Classe | Cobertura |
|---|---|
| Conta.java | 100% |
| ContaRepository.java | 100% |
| ContaService.java | 94% |
| CrudT1Application.java | 91% |
| **Total** | **96%** |

### Testes pós-deploy
`SeleniumPostDeployTest` verifica que o sistema inicializou corretamente e consegue executar operações básicas após o deploy.

**Como rodar:**
```bash
cd tp5
mvn spring-boot:run           # com H2
mvn spring-boot:run -Dspring-boot.run.profiles=mysql   # com MySQL
```

**Rodar testes com relatório de cobertura:**
```bash
cd tp5
mvn test
# relatório em target/site/jacoco/index.html
```

---

## Secrets necessários (para o deploy.yml)

| Secret | Descrição |
|---|---|
| `AWS_ROLE_ARN` | ARN da role IAM para autenticação OIDC |
| `PROD_DB_URL` | URL do banco em produção |
| `PROD_DB_USER` | Usuário do banco em produção |
| `PROD_DB_PASS` | Senha do banco em produção |

---

## Perfis de banco de dados

| Perfil | Banco | Quando usar |
|---|---|---|
| `h2` (padrão) | H2 em memória | Desenvolvimento local e testes |
| `test` | H2 em memória | Testes automatizados |
| `mysql` | MySQL | Produção |

---

**Aluna:** Júlia Caroline Tallarico  
**Disciplina:** Programação Backend
