# syntax = docker/dockerfile:1.2
FROM gradle:jdk18 AS build

# Mount the .env file as a secret
RUN --mount=type=secret,id=_env,dst=/etc/secrets/.env cat /etc/secrets/.env
RUN --env-file /etc/secrets/.env build
WORKDIR /app
COPY . .
RUN chmod +x gradlew

# Source the .env file and export the variables
RUN --mount=type=secret,id=_env,dst=/etc/secrets/.env \
    set -o allexport; . /etc/secrets/.env; set +o allexport

# Print environment variables to verify they are set correctly
RUN env

# Use the environment variables during the build
RUN ./gradlew clean build
RUN ls -l build/libs

FROM openjdk:24-slim-bullseye
WORKDIR /app

# Copy the JAR file to the container
COPY --from=build /app/build/libs/CafeLabService-0.0.4-SNAPSHOT.jar app.jar

# Expose the port that the application will run on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]