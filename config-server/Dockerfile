FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app
COPY target/*.jar app.jar

# config files folder (optional)
COPY app-config /app/app-config

EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
