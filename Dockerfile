FROM maven:3.8.4-openjdk-17 AS builder-core
WORKDIR /app/core
COPY core/pom.xml .
COPY core/src ./src
RUN mvn clean package -DskipTests

FROM maven:3.8.4-openjdk-17 AS builder-producer
WORKDIR /app/producer
COPY transaction_producer/pom.xml .
COPY transaction_producer/src ./src
RUN mvn clean package -DskipTests

FROM maven:3.8.4-openjdk-17 AS builder-detector
WORKDIR /app/detector
COPY fraud_detector/pom.xml .
COPY fraud_detector/src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-slim
WORKDIR /app

COPY --from=builder-core /app/core/target/*.jar core.jar
COPY --from=builder-producer /app/producer/target/*.jar producer.jar
COPY --from=builder-detector /app/detector/target/*.jar detector.jar

COPY mongo-seed/ /docker-entrypoint-initdb.d/
COPY branches.json /docker-entrypoint-initdb.d/
COPY customers.json /docker-entrypoint-initdb.d/

EXPOSE 8083 8080 8081 9091

CMD ["sh", "-c", "java -jar core.jar & java -jar producer.jar & java -jar detector.jar"]