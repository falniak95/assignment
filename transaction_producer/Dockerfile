FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/transaction_producer-0.0.1-SNAPSHOT.jar app.jar

RUN mkdir -p /var/log

EXPOSE 8080
EXPOSE 9091

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"] 