# Banking Transaction System Assignment

A real-time banking transaction system with fraud detection capabilities, built using modern microservices architecture.

## Project Overview

This assignment implements a banking transaction system focusing on real-time fraud detection in Copenhagen-based branches. The system consists of two microservices:
- **Transaction Producer**: Handles transaction creation and Kafka message publishing
- **Fraud Detector**: Processes transactions and performs sophisticated fraud detection

## Technical Stack

### Core Technologies
- **Java 17**: Latest LTS version for enhanced performance and features
- **Spring Boot 3.2.3**: Modern framework for building microservices
- **Apache Kafka**: Event streaming for real-time transaction processing
- **MongoDB**: NoSQL database for flexible data storage
- **Project Reactor**: Reactive programming support
- **Docker**: Containerization and deployment

### Key Dependencies
- `spring-boot-starter-web`: RESTful API support
- `spring-kafka`: Kafka integration for event streaming
- `spring-boot-starter-data-mongodb-reactive`: Reactive MongoDB support
- `spring-boot-starter-validation`: Input validation
- `lombok`: Boilerplate code reduction
- `spring-boot-starter-actuator`: Application monitoring
- `reactor-core`: Reactive programming support

## Architecture

### Data Flow
1. Transaction creation in Producer service
2. Event publishing to Kafka
3. Real-time consumption by Fraud Detector
4. Fraud analysis and status update
5. Result persistence in MongoDB

## Fraud Detection System

### Transaction Identifier Format
- Pattern: `YYYYMMDD_SENDER-BRANCH_SENDER-CUSTOMER_SEQUENCE`
- Example: `20240131_CPH001_10000001_0001`

### Fraud Detection Rules

1. **Time-based Validation**
   - No future-dated transactions
   - 24-hour transaction window
   - Business hours monitoring
   - Timestamp consistency checks

2. **Branch Validation**
   - Active branch verification
   - Valid branch code format (CPHxxx)
   - Branch-customer relationship validation

3. **Customer Validation**
   - Active customer status
   - Valid customer number format
   - Customer-branch relationship check

4. **Amount Pattern Analysis**
   - Transaction limit monitoring
   - Unusual pattern detection
   - High-value transaction scrutiny

5. **Location Analysis**
   - Cross-branch transaction patterns
   - Geographic consistency
   - Multiple location access detection

## Setup and Running

### Prerequisites
- JDK 17
- Docker and Docker Compose
- MongoDB
- Apache Kafka

### Local Development
To start the infrastructure and services:

1. Start Docker containers using docker-compose
2. Run transaction_producer service using Maven
3. Run fraud_detector service using Maven

### Docker Deployment
Use docker-compose to deploy all services and infrastructure components together.

## Monitoring

- Actuator endpoints for health checks
- Kafka monitoring through UI
- MongoDB performance monitoring
- Application logs and metrics

## Future Improvements

1. Enhanced fraud detection algorithms
2. Machine learning integration
3. Real-time notification system
4. Performance optimization
5. Additional security measures

## License

This project is part of a banking system assignment and is for educational purposes only. 