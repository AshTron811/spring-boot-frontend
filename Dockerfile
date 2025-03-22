# Stage 1: Build the JAR file using Maven
FROM maven:3.8.6-openjdk-23 AS build
WORKDIR /app
# Copy all project files to the container
COPY . .
# Build the project and skip tests for faster build times
RUN mvn clean install -DskipTests

# Stage 2: Run the Spring Boot application using a lightweight JDK 23 image
FROM openjdk:23-jdk-slim
WORKDIR /app
# Copy the built jar file from the previous stage.
# Make sure the jar name matches what Maven produces.
COPY --from=build /app/target/attendance-frontend-0.0.1-SNAPSHOT.jar app.jar
# Expose the port that your Spring Boot application listens on (typically 8080)
EXPOSE 8080
# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
