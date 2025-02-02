FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/fraud_detector-0.0.1-SNAPSHOT.jar app.jar

ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=docker
ENV LOGGING_LEVEL_ROOT=INFO
ENV LOGGING_LEVEL_COM_FURKANALNIAK=DEBUG

EXPOSE 8081

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} \
    -Dlogging.level.root=INFO \
    -Dlogging.level.com.furkanalniak=DEBUG \
    -Dlogging.pattern.console='%d{yyyy-MM-dd HH:mm:ss} - %msg%n' \
    -jar app.jar"] 