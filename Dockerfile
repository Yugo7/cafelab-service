# syntax = docker/dockerfile:1.2
FROM gradle:jdk18 AS build
WORKDIR /app
COPY . .

RUN chmod +x gradlew
RUN echo '#!/bin/sh\n. /etc/secrets/.env\n./gradlew clean build -x test' > run_gradlew.sh && chmod +x run_gradlew.sh
RUN --mount=type=secret,id=_env,dst=/etc/secrets/.env ./run_gradlew.sh

RUN ls -l build/libs

FROM openjdk:24-slim-bullseye
WORKDIR /app
COPY --from=build /app/build/libs/CafeLabService-0.0.4-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]