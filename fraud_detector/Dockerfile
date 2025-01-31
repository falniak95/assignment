FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/fraud_detector-0.0.1-SNAPSHOT.jar app.jar

RUN mkdir -p /var/log

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"] 