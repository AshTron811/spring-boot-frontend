# Use a Maven image to build the JAR file
FROM maven:3.8.6-openjdk-23 AS build
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests

# Use a lightweight OpenJDK image to run the app
FROM openjdk:23-jdk-slim
WORKDIR /app

# Copy the built JAR file from the previous stage
COPY --from=build /app/target/attendance-frontend-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port (adjust if needed)
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
