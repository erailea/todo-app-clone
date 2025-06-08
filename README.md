# Todo App Clone

A full-stack Todo application built with Spring Boot and modern frontend technologies.

## Project Structure

```
todo-app-clone/
├── backend/         # Spring Boot application
└── frontend/        # Frontend application (coming soon)
```

## Quick Start

### Using Docker Compose (Recommended)

1. Start all services:
```bash
docker-compose up -d
```

2. Initialize Couchbase (Required First Time Setup):
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

3. Access the applications:
- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- Couchbase UI: http://localhost:8091

### Manual Setup

#### Backend

1. Navigate to the backend directory:
```bash
cd backend
```

2. Copy environment file:
```bash
cp env.properties.sample env.properties
```

3. Update the values in `env.properties` according to your environment.

4. Build and run:
```bash
mvn clean install
mvn spring-boot:run
```

#### Frontend (Coming Soon)

Frontend setup instructions will be added when the application is ready.

## Features

### Backend
- RESTful API with Spring Boot 3.2
- JWT Authentication
- Couchbase Database Integration
- Swagger API Documentation
- Docker Support
- Comprehensive Test Coverage
- Global Exception Handling
- Input Validation
- Soft Delete Support

### Frontend (Coming Soon)
- Modern UI Framework
- Responsive Design
- State Management
- API Integration
- User Authentication

## Backend API Documentation

Once the backend is running, you can access:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI Documentation: http://localhost:8080/api-docs

### API Endpoints

#### Authentication
- POST `/auth/register` - Register a new user
  - Required fields: email, password (min 6 chars), fullName (2-100 chars)
- POST `/auth/authenticate` - Authenticate a user
  - Required fields: email, password

#### Todo Lists
- GET `/lists` - Get all todo lists for authenticated user
- POST `/lists` - Create a new todo list
  - Required fields: title
- PATCH `/lists/{id}` - Update a todo list title
  - Required fields: title
- DELETE `/lists/{id}` - Delete a todo list (soft delete)

- GET `/lists/{id}/notes` - Get all notes in a list
- POST `/lists/{id}/notes` - Create a new note
  - Required fields: content
  - Optional fields: dueDate

#### Notes
- GET `/notes/{id}` - Get a specific note
- PATCH `/notes/{id}` - Update a note
  - Optional fields: content, done, dueDate, targetListId
- DELETE `/notes/{id}` - Delete a note (soft delete)

## Development

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Node.js 18 or higher (for frontend)
- Docker and Docker Compose
- Couchbase Server 7.x or higher

### Environment Setup

#### Backend Environment
1. Copy `backend/env.properties.sample` to `backend/env.properties`
2. Update the following variables:
   - COUCHBASE_USERNAME (default: Administrator)
   - COUCHBASE_PASSWORD (default: password)
   - COUCHBASE_CONNECTION_STRING (default: couchbase://localhost)
   - COUCHBASE_BUCKET_NAME (default: todo-app)
   - JWT_SECRET (required)
   - JWT_EXPIRATION (default: 86400000)
   - SERVER_PORT (default: 8080)

#### Frontend Environment (Coming Soon)
Frontend environment setup instructions will be added when the application is ready.

### Running Tests

#### Backend Tests
```bash
cd backend
mvn test
```

The backend includes comprehensive test coverage for:
- Authentication Service
- JWT Service
- Todo List Service
- Note Service
- Global Exception Handling

#### Frontend Tests (Coming Soon)
Frontend test instructions will be added when the application is ready.

## Code Coverage Report

View the detailed code coverage report at: https://erailea.github.io/todo-app-clone/index.html

## Live Demo

The backend API is deployed and available at: https://to-do-app-clone-d8b4f8b698af.herokuapp.com/swagger-ui/index.html

## Troubleshooting

### Common Issues

1. **Couchbase Connection Issues**
   - Ensure Couchbase Server is running
   - Check if the bucket exists
   - Verify username and password

2. **Port Conflicts**
   - Change SERVER_PORT in env.properties if 8080 is in use

3. **Docker Issues**
   - Ensure Docker daemon is running
   - Check if ports are not in use
   - Verify Docker Compose installation
