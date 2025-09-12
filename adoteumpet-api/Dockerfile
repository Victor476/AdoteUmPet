# Use OpenJDK 21 as base image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy Maven wrapper and pom.xml first for better layer caching
COPY mvnw mvnw.cmd pom.xml ./
COPY .mvn .mvn

# Make Maven wrapper executable
RUN chmod +x mvnw

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline -B

# Copy the source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests -B

# Expose the port that the Spring Boot app runs on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/adoteumpet-api-0.0.1-SNAPSHOT.jar"]
