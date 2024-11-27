# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the pre-built JAR file into the image
COPY build/libs/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the Spring Boot application with 'prod' profile
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]