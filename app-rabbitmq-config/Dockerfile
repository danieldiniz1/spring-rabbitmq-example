# Use an official OpenJDK runtime as a parent image
FROM openjdk:17
#RUN apk add --no-cache bash

# Set the working directory in the container
WORKDIR /app

# Copy the build files from the host to the container
COPY build/libs/*.jar app.jar

# Create and set the volume directory
VOLUME /app/volume

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]