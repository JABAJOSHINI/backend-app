# Use lightweight Java image
FROM eclipse-temurin:17-jdk-jammy


# Set working directory
WORKDIR /app

# Copy the built jar
COPY target/*.jar app.jar

# Expose gateway port (change if needed)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
