# Usar imagen base de Java 17
FROM openjdk:17-jdk-slim

# Crear directorio de trabajo
WORKDIR /app

# Copiar el JAR
COPY target/oriola-denim-0.0.1-SNAPSHOT.jar app.jar

# Exponer puerto
EXPOSE 8080

# Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=railway
ENV PORT=8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "app.jar", "--server.port=${PORT}"]
