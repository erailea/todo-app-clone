# Backend Documentation

## Overview
Spring Boot backend with JWT authentication, Couchbase database integration, and comprehensive test coverage.

## Setup

1. Copy environment file:
```bash
cp env.properties.sample env.properties
```

2. Update `env.properties` with your configuration:
- COUCHBASE_USERNAME (default: Administrator)
- COUCHBASE_PASSWORD (default: password)
- COUCHBASE_CONNECTION_STRING (default: couchbase://localhost)
- COUCHBASE_BUCKET_NAME (default: todo-app)
- JWT_SECRET (required)
- JWT_EXPIRATION (default: 86400000)
- SERVER_PORT (default: 8080)

3. Initialize Couchbase (Required First Time Setup):
   - Access Couchbase UI at http://localhost:8091
   - Login with default credentials:
     - Username: `Administrator`
     - Password: `password`
   - Create a new cluster:
     - Click "Setup New Cluster"
     - Cluster Name: `todo-cluster`
     - Accept the terms
     - Click "Finish With Defaults"
   - Create a new bucket:
     - Go to "Buckets" section
     - Click "Add Bucket"
     - Bucket Name: `todo-app`
     - Memory Quota: 100 MB
     - Click "Add Bucket"

## Development

### Build
```bash
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

### Test
```bash
mvn test
```

## API Documentation
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI Documentation: http://localhost:8080/api-docs

## Features
- RESTful API with Spring Boot 3.2
- JWT Authentication
- Couchbase Database Integration
- Swagger API Documentation
- Docker Support
- Comprehensive Test Coverage
- Global Exception Handling
- Input Validation
- Soft Delete Support 