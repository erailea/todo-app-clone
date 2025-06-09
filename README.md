# Todo App Clone

A full-stack Todo application built with Spring Boot and Vue.js.

## Project Structure

```
todo-app-clone/
├── backend/         # Spring Boot application
└── frontend/        # Vue.js application
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
- Frontend: http://localhost:3000
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

#### Frontend

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Copy environment file:
```bash
cp .env.example .env
```

3. Update the values in `.env` according to your environment.

4. Install dependencies and run:
```bash
npm install
npm run dev
```

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

### Frontend
- Vue.js 3 with Composition API
- Vuex State Management
- Vue Router for Navigation
- Axios for HTTP Requests
- JWT Token Authentication
- Responsive Design
- Cookie-based Authentication Storage

## Deployment

### Heroku Deployment

#### Frontend Deployment (Docker)

The frontend is deployed using Docker containers for better flexibility and consistency.

**Key Features:**
- **Runtime Environment Configuration**: Same Docker image can be used across different environments
- **Multi-stage Build**: Optimized production build with Nginx
- **Dynamic Configuration**: API URL can be set at runtime via environment variables

**Environment Variables:**
```bash
# Set API URL at runtime
heroku config:set API_BASE_URL=https://your-backend-url.herokuapp.com

# Optional: Set custom port (Heroku sets this automatically)
heroku config:set PORT=8080
```

**Local Docker Development:**
```bash
# Build the Docker image
docker build -t todo-frontend ./frontend

# Run with custom API URL
docker run -p 3000:80 -e API_BASE_URL=http://localhost:8080 todo-frontend

# Run with different environment
docker run -p 3000:80 -e API_BASE_URL=https://staging-api.example.com todo-frontend
```

#### Backend Deployment
The backend is already deployed at: https://to-do-app-clone-be-ff03bad65820.herokuapp.com/

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
- Node.js 18 or higher
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

#### Frontend Environment
1. Copy `frontend/.env.example` to `frontend/.env`
2. Update the following variables:
   - VITE_API_BASE_URL (default: http://localhost:8080)
   - VITE_DEV_SERVER_PORT (default: 3000)
   - VITE_DEV_SERVER_HOST (default: true)
   - PORT (for Heroku deployment)
   - NODE_ENV (for production builds)

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

## Code Coverage Report

View the detailed code coverage report at: https://erailea.github.io/todo-app-clone/index.html

## Live Demo

- Backend API: https://to-do-app-clone-be-ff03bad65820.herokuapp.com/swagger-ui/index.html
- Frontend (coming soon): TBD

## Troubleshooting

### Common Issues

1. **Couchbase Connection Issues**
   - Ensure Couchbase Server is running
   - Check if the bucket exists
   - Verify username and password

2. **Port Conflicts**
   - Change SERVER_PORT in env.properties if 8080 is in use
   - Change VITE_DEV_SERVER_PORT in .env if 3000 is in use

3. **Docker Issues**
   - Ensure Docker daemon is running
   - Check if ports are not in use
   - Verify Docker Compose installation

4. **Frontend API Connection Issues**
   - Verify VITE_API_BASE_URL is correct
   - Check if backend is running
   - Ensure CORS is properly configured
