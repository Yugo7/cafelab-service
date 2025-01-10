# syntax = docker/dockerfile:1.2
FROM gradle:jdk18 AS build

# Mount the .env file as a secret
RUN --mount=type=secret,id=_env,dst=/etc/secrets/.env cat /etc/secrets/.env || true

WORKDIR /app
COPY . .

RUN chmod +x gradlew

# Create a script to load environment variables and run gradlew
RUN echo '#!/bin/sh\n. /etc/secrets/.env\n./gradlew clean build -x test' > run_gradlew.sh && chmod +x run_gradlew.sh

# Run the script
RUN --mount=type=secret,id=_env,dst=/etc/secrets/.env ./run_gradlew.sh

RUN ls -l build/libs

FROM openjdk:24-slim-bullseye
WORKDIR /app

# Copy the JAR file to the container
COPY --from=build /app/build/libs/CafeLabService-0.0.4-SNAPSHOT.jar app.jar

# Expose the port that the application will run on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]