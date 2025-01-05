# Use an official OpenJDK runtime as a parent image
FROM gradle:jdk18 AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean build

FROM openjdk:24-slim-bullseye
WORKDIR /app
# Copy the JAR file to the container
COPY --from=build app/target/CafeLabService-0.0.4-SNAPSHOT.jar app/app.jar
# Expose the port that the application will run on
EXPOSE 8080
# Run the JAR file
ENTRYPOINT ["java", "-jar", "app/app.jar"]