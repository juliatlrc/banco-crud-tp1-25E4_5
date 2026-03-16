# Questão 1 — Dockerfile do Banco
# Imagem base exigida pelo enunciado
FROM postgres:17.8-alpine3.23

# Variáveis de ambiente do Postgres
# Serão usadas na criação do banco na primeira execução
ENV POSTGRES_DB=ricknmorty
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres

# Copia o script SQL para o diretório de inicialização oficial do Postgres.
# Qualquer arquivo .sql ou .sh colocado aqui é executado automaticamente
# APENAS na primeira vez que o container sobe com o diretório de dados vazio.
COPY data.sql /docker-entrypoint-initdb.d/data.sql

# Porta padrão do Postgres — declarada apenas para documentação.
# NÃO será exposta para o host (sem -p 5432:5432 no run).
EXPOSE 5432
