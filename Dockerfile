FROM gradle:8-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build app/build/libs/*.jar learning_service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "learning_service.jar"]