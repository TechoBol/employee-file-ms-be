FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copiar archivos de Maven
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Dar permisos de ejecución al mvnw
RUN chmod +x ./mvnw

# Descargar dependencias
RUN ./mvnw dependency:go-offline

# Copiar código fuente
COPY src ./src

# Compilar
RUN ./mvnw clean package -DskipTests

# Imagen final
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]