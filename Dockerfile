# syntax = docker/dockerfile:1.2

FROM gradle:jdk18 AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean build

FROM openjdk:24-slim-bullseye
WORKDIR /app

# Expose the port that the application will run on
EXPOSE 8080

# Copy the JAR file to the container
COPY --from=build /app/build/libs/CafeLabService-0.0.4-SNAPSHOT.jar app.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]