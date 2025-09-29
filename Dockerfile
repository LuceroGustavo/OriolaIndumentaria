# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application and create JAR
RUN mvn clean package -DskipTests

# The JAR is already in the target directory after the build
# No need to copy it separately

# Expose port 8080
EXPOSE 8080

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=prod
ENV PORT=8080

# Run the application using the JAR from target directory
CMD ["java", "-jar", "target/oriola-denim-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]