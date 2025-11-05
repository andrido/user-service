# Estágio de Build
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

# Estágio Final
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8081 # A porta do User Service. Se ele usar 8080 por padrão, mude para 8081 para não conflitar com o Book Service.
ENTRYPOINT ["java", "-jar", "app.jar"]