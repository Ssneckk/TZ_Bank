FROM ubuntu:latest

LABEL authors="Ssneckk (Adil Soltanov)"

WORKDIR /app

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]