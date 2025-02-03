FROM maven:3.8.4-openjdk-17 AS builder

# Copy parent POM first
WORKDIR /app
COPY pom.xml .

# Copy module POMs
COPY core/pom.xml core/
COPY transaction_producer/pom.xml transaction_producer/
COPY fraud_detector/pom.xml fraud_detector/

# Copy source code
COPY core/src core/src
COPY transaction_producer/src transaction_producer/src
COPY fraud_detector/src fraud_detector/src

# Build all modules
RUN mvn clean package -DskipTests

# Final stage
FROM openjdk:17-slim
WORKDIR /app

# Copy built JARs
COPY --from=builder /app/core/target/*.jar core.jar
COPY --from=builder /app/transaction_producer/target/*.jar producer.jar
COPY --from=builder /app/fraud_detector/target/*.jar detector.jar

COPY mongo-seed/ /docker-entrypoint-initdb.d/
COPY branches.json /docker-entrypoint-initdb.d/
COPY customers.json /docker-entrypoint-initdb.d/

EXPOSE 8083 8080 8081 9091

CMD ["sh", "-c", "java -jar core.jar & java -jar producer.jar & java -jar detector.jar"]