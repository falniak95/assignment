# Banking Transaction System

A real-time banking transaction system with capabilities for fraud detection, suspicious transaction monitoring, and reporting to financial authorities (such as MASAK in Turkey, which tracks high-value transfers to prevent financial crimes). The system ensures seamless transaction flow by leveraging a queue-based architecture, allowing real-time detection and reporting without interrupting the transaction process. Built using a modern microservices architecture, Kafka event streaming, and gRPC communication, it provides both efficiency and compliance.

## Project Overview

This system implements a sophisticated banking transaction platform with real-time fraud detection for Copenhagen-based branches. The architecture consists of three main microservices:

- **Transaction Producer Service**: 
  - Handles transaction creation and management
  - Provides gRPC endpoints for banking operations
  - Publishes transactions to Kafka
  - Exposes REST endpoints for transaction generation

- **Fraud Detector Service**: 
  - Consumes transactions from Kafka
  - Performs real-time fraud detection
  - Implements complex validation rules
  - Stores detection results in MongoDB

- **Core Module**: 
  - Contains shared domain models
  - Provides common database operations
  - Implements shared utilities
  - Manages MongoDB entity definitions

## Technical Stack

### Core Technologies
- **Java 17**: Latest LTS version with modern language features
- **Spring Boot 3.2.3**: Modern microservices framework
- **Apache Kafka**: Event streaming for transaction processing
- **MongoDB**: NoSQL database for data persistence
- **gRPC**: High-performance RPC framework for inter-service communication
- **Project Reactor**: Reactive programming support
- **Docker & Docker Compose**: Containerization and orchestration
- **Maven**: Build tool and dependency management

## Service Architecture

### Service Ports
- Transaction Producer: 
  - REST API: 8080
  - gRPC Server: 9091
- Fraud Detector: 8081
- MongoDB: 5415
- Kafka: 9092 (External), 29092 (Internal)
- Zookeeper: 2181

### Communication Flow
1. Client initiates transaction via REST API or gRPC
2. Transaction Producer validates and processes request
3. Valid transactions are published to Kafka topic
4. Fraud Detector consumes transactions from Kafka
5. Fraud detection rules are applied
6. Results are persisted in MongoDB

## Fraud Detection System

### Transaction Format
- Pattern: `YYYYMMDD_SENDER-BRANCH_SENDER-CUSTOMER_SEQUENCE`
- Example: `20240131_CPH001_10000001_0001`

### Validation Rules

1. **Transaction Validation**
   - Timestamp validation (not future dated, within 24h)
   - Amount verification (daily limits)
   - Business hours check for high-value transactions
   - Transaction identifier format validation

2. **Branch Validation**
   - Copenhagen branch code format (CPHxxx)
   - Branch active status verification
   - Operating hours compliance

3. **Customer Validation**
   - Account status check
   - Transaction limits monitoring
   - Multi-branch activity pattern detection

## Setup and Deployment

### Prerequisites
- JDK 17
- Docker and Docker Compose
- Maven 3.x

### Local Development Setup
1. Clone the repository
2. Build all services:
    ```bash
   mvn clean install
   ```
3. Build the docker compose:
   ```bash
   docker compose build
   ```
4. Run services:
   ```bash
   docker compose up -d
   ```

### API Documentation
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- gRPC Services: Defined in `banking_service.proto`

### Monitoring
- Health Check: `/actuator/health`
- Metrics: `/actuator/metrics`
- Kafka Topics: `transaction-events`

## Testing
- Unit Tests with JUnit 5
- Integration Tests with TestContainers
- gRPC Tests with gRPC testing framework
- Kafka Consumer/Producer Tests
