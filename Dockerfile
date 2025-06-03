# Etapa 1: Construcción
FROM maven:3.9-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Copiar el archivo POM y descargar dependencias
COPY pom.xml .
COPY .mvn .mvn
# Capa de caché para dependencias Maven
RUN mvn dependency:go-offline

# Copiar el código fuente
COPY src ./src
COPY mvnw mvnw.cmd ./

# Construir el proyecto omitiendo tests
RUN mvn package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Crear usuario no root para seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Variables de entorno configurables
ENV MONGO_HOST=localhost \
    MONGO_PORT=27017 \
    MONGO_DATABASE=hotel_auth_db \
    SERVER_PORT=8080 \
    JWT_SECRET=H8DSF7sdf87DSF87ds87f6SD87f6ds78F6SD7f6d7F6D87f6d78F6ds78F6DS78f6d78F6d78FS6d87f6D78S \
    JWT_EXPIRATION=86400000 \
    REFRESH_TOKEN_EXPIRATION=604800000

# Copiar el archivo JAR desde la etapa de construcción
COPY --from=builder /app/target/*.jar app.jar

# Puerto expuesto por la aplicación
EXPOSE 8080

# Comando de entrada
ENTRYPOINT ["java", "-jar", "/app/app.jar"]