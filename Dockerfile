# syntax = docker/dockerfile:1.2

# Check if the TEST environment variable is set
RUN if [ -z "${TEST}" ]; then echo "TEST is not set"; else echo "TEST is set to ${TEST}"; fi

FROM gradle:jdk18 AS build
RUN if [ -z "${TEST}" ]; then echo "2TEST is not set"; else echo "2TEST is set to ${TEST}"; fi

# Mount the .env file as a secret
RUN --mount=type=secret,id=_env,dst=/etc/secrets/.env cat /etc/secrets/.env || true

WORKDIR /app
COPY . .
RUN chmod +x gradlew

# Create a shell script to source the .env file and run the Gradle build
RUN --mount=type=secret,id=_env,dst=/etc/secrets/.env \
    echo 'export $(grep -v '^#' /etc/secrets/.env | xargs) && ./gradlew clean build' > build.sh && \
    chmod +x build.sh

# Run the shell script with the secret mounted
RUN --mount=type=secret,id=_env,dst=/etc/secrets/.env ./build.sh
RUN ls -l build/libs

FROM openjdk:24-slim-bullseye
WORKDIR /app
RUN if [ -z "${TEST}" ]; then echo "3TEST is not set"; else echo "3TEST is set to ${TEST}"; fi

# Copy the JAR file to the container
COPY --from=build /app/build/libs/CafeLabService-0.0.4-SNAPSHOT.jar app.jar

# Set environment variables
ENV SPRING_DATASOURCE_URL=${BD_URL}
ENV SPRING_DATASOURCE_USERNAME=${BD_USER}
ENV SPRING_DATASOURCE_PASSWORD=${BD_PASSWORD}
ENV FRONTEND_URL=${FRONTEND_URL}
ENV STRIPE_KEY=${STRIPE_KEY}
ENV SPRING_MAIL_HOST=${MAIL_HOST}
ENV SPRING_MAIL_PORT=587
ENV SPRING_MAIL_USERNAME=${MAIL_USERNAME}
ENV SPRING_MAIL_PASSWORD=${MAIL_PASSWORD}
ENV SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH=true
ENV SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE=true

# Expose the port that the application will run on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]