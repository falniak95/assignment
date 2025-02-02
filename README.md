# Banking Transaction System

A real-time banking transaction system with fraud detection capabilities, built using modern microservices architecture, Kafka event streaming, and gRPC communication.

## Project Overview

This system implements a sophisticated banking transaction platform with real-time fraud detection for Copenhagen-based branches. The architecture consists of four main components:

- **Transaction Producer**: Handles transaction creation and Kafka message publishing and GRPC communication
- **Fraud Detector**: Processes transactions from Kafka and performs fraud detection
- **Core Module**: Database operations, common utilities and shared models
- **Apache Kafka**: Event streaming platform for transaction processing
- **MongoDB**: Stores transaction and fraud detection results

## Technical Stack

### Core Technologies
- **Java 17**: Latest LTS version
- **Spring Boot 3.2.3**: Microservices framework
- **Apache Kafka**: Event streaming platform
- **gRPC**: High-performance RPC framework
- **MongoDB**: NoSQL database
- **Project Reactor**: Reactive programming
- **Docker**: Containerization
- **Maven**: Build tool and dependency management

### Key Dependencies
- `spring-boot-starter-web`: REST API support
- `spring-kafka`: Kafka integration
- `grpc-spring-boot-starter`: gRPC integration
- `spring-boot-starter-data-mongodb-reactive`: Reactive MongoDB operations
- `spring-boot-starter-validation`: Input validation
- `lombok`: Reduces boilerplate code
- `spring-boot-starter-actuator`: Application monitoring
- `protobuf`: Protocol buffers for gRPC
- `reactor-core`: Reactive programming support

## Service Architecture

### Ports
- Transaction Producer: 8080 (REST), 9091 (gRPC)
- Fraud Detector: 8081 (REST)
- MongoDB: 5415
- Kafka Broker: 9092 / 29092
- Zookeeper: 2181

### Data Flow
1. Client sends transaction request to Transaction Producer
2. Transaction Producer validates and processes request
3. Producer publishes transaction to Kafka topic
4. Fraud Detector consumes transaction from Kafka
5. Fraud Detector performs analysis
6. Results are stored in MongoDB

## Kafka Configuration

### Topics
- `transaction-events`: Main transaction topic

### Consumer Groups
- `fraud-detector-group`: Fraud detection service consumers

## Fraud Detection System

### Transaction Format
- Pattern: `YYYYMMDD_SENDER-BRANCH_SENDER-CUSTOMER_SEQUENCE`
- Example: `20240131_CPH001_10000001_0001`

### Validation Rules

1. **Transaction Validation**
   - Timestamp validation
   - Amount verification
   - Business hours check
   - Sequence validation

2. **Branch Validation**
   - Branch code format (CPHxxx)
   - Active status check
   - Operating hours verification

3. **Customer Validation**
   - Account status
   - Transaction limits
   - Relationship verification

## Setup and Deployment

### Prerequisites
- JDK 17
- Docker and Docker Compose
- Maven

### Local Development
1. Start the infrastructure using Docker Compose
2. Build all services using Maven
3. Run Transaction Producer service
4. Run Fraud Detector service

### Docker Deployment
Use Docker Compose to deploy all services and infrastructure components together.

### Initial Data Setup
Use the provided mongo-import script to import initial branch data.

## Monitoring

- Actuator endpoints: `/actuator/health`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Application metrics and logs

## Testing

- Kafka consumer/producer tests with JUnit
