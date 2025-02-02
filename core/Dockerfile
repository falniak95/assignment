FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/core-0.0.1-SNAPSHOT.jar app.jar

RUN mkdir -p /var/log

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"] 