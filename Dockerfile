# Use an official OpenJDK 23 image
FROM openjdk:23-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar file from the target directory into the container.
# Replace 'your-frontend-app.jar' with the actual name of your jar file.
COPY target/attendance-frontend-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app runs on (typically 8080)
EXPOSE 8080

# Run your Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]