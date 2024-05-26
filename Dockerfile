# Используем базовый образ с установленной Java для сборки
FROM openjdk:17-jdk-slim AS builder

COPY . /app

WORKDIR /app

RUN ./gradlew build

FROM openjdk:17-jdk-slim

COPY --from=builder /app/build/libs/*.jar /app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app.jar"]
