FROM gradle:8.13.0-jdk21 AS build

WORKDIR /app

COPY --chown=gradle:gradle . .

RUN gradle build --no-daemon -x test

FROM openjdk:25-jdk-slim

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
