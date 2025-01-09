# syntax = docker/dockerfile:1.2
FROM gradle:jdk18 AS build
RUN --mount=type=secret,id=_env,dst=/etc/secrets/.env cat /etc/secrets/.env
WORKDIR /app
COPY . .
RUN chmod +x gradlew

# Source the .env file and export the variables
RUN set -o allexport; source /etc/secrets/.env; set +o allexport

# Use the environment variables during the build
RUN ./gradlew clean build
RUN ls -l build/libs

FROM openjdk:24-slim-bullseye
WORKDIR /app

# Copy the .env file to the container
COPY --from=build /etc/secrets/.env /app/.env

# Source the .env file and export the variables
RUN set -o allexport; source /app/.env; set +o allexport

# Display the values of the environment variables
RUN echo "BD_URL=$BD_URL"
RUN echo "MAIL_HOST=$MAIL_HOST"
RUN echo "MAIL_PORT=$MAIL_PORT"
RUN echo "MAIL_USERNAME=$MAIL_USERNAME"
RUN echo "MAIL_PASSWORD=$MAIL_PASSWORD"

# Copy the JAR file to the container
COPY --from=build /app/build/libs/CafeLabService-0.0.4-SNAPSHOT.jar app.jar

# Expose the port that the application will run on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]