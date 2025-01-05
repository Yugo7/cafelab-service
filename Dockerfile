# Use an official OpenJDK runtime as a parent image
FROM openjdk:18-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the host machine to the container
COPY build/libs/CafeLabService-0.0.4-SNAPSHOT.jar app/app.jar

# Expose the port that the application will run on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app/app.jar"]