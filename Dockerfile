# Use an official Java runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built Spring Boot JAR file into the container
COPY target/*.jar app.jar

# Expose port 8080 to the outside
EXPOSE 8084

ENV SPRING_PROFILES_ACTIVE=local

# Run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
