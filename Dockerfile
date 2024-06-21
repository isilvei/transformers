# Use the official OpenJDK 8 image as the base image
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

COPY /build/libs/transformers-*.jar /app/app.jar

# Set the entry point for the application
ENTRYPOINT ["java", "-jar", "app.jar"]

# Expose the port your application will run on
EXPOSE 8080