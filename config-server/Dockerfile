FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar

# config files folder (optional)
COPY app-config /app/app-config

EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
