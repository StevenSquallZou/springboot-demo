# Use an official OpenJDK runtime as the base image
FROM eclipse-temurin:19-jre-jammy

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/springboot-demo-1.0-SNAPSHOT.jar app.jar

# Expose the port your app runs on (e.g., 8080)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
