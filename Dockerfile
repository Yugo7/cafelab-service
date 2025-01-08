# Use an official OpenJDK runtime as a parent image
FROM gradle:jdk18 AS build
RUN echo "MAIL_HOST=$MAIL_HOST"
RUN echo "MAIL_PORT=$MAIL_PORT"
RUN echo "MAIL_USERNAME=$MAIL_USERNAME"
RUN echo "MAIL_PASSWORD=$MAIL_PASSWORD"
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean build
RUN ls -l build/libs

FROM openjdk:24-slim-bullseye
RUN echo "MAIL_HOST=$MAIL_HOST"
RUN echo "MAIL_PORT=$MAIL_PORT"
RUN echo "MAIL_USERNAME=$MAIL_USERNAME"
RUN echo "MAIL_PASSWORD=$MAIL_PASSWORD"
WORKDIR /app
# Copy the JAR file to the container
COPY --from=build /app/build/libs/CafeLabService-0.0.4-SNAPSHOT.jar app.jar
# Expose the port that the application will run

# Display the values of the environment variables

EXPOSE 8080
# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]