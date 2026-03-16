# Questão 2 — Dockerfile da API Rick & Morty
# Build multi-stage: separa compilação de execução
# Resultado: imagem final menor, sem JDK nem Maven

# ---- Estágio 1: build ----
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copia o wrapper e o pom primeiro — aproveita cache do Docker
# Se só o código-fonte mudar, o Maven não baixa dependências de novo
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Copia o código-fonte e compila
COPY src ./src
RUN ./mvnw package -DskipTests -B

# ---- Estágio 2: execução ----
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia apenas o JAR gerado — sem JDK, sem Maven, sem código-fonte
COPY --from=build /app/target/*.jar app.jar

# Porta da API Spring Boot
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
