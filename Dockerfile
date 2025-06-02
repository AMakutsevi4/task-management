FROM gradle:8.5.0-jdk21 AS build
WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle ./gradle
RUN gradle clean build --no-daemon || true

COPY . .
RUN gradle bootJar --no-daemon

FROM openjdk:21-jdk-slim AS run
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

CMD ["java", "-jar", "app.jar"]