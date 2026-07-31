# ==========================================
# Etapa 1: compilación
# ==========================================

FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copiamos primero el POM para aprovechar la caché de Docker
COPY pom.xml .

# Descarga anticipadamente las dependencias
RUN mvn dependency:go-offline -B

# Copiamos el código fuente
COPY src ./src

# Compilamos y generamos target/app.jar
RUN mvn clean package -DskipTests -B


# ==========================================
# Etapa 2: ejecución
# ==========================================

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/target/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]