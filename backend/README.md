# Todo App Clone Backend

A Spring Boot backend application for a Todo application with Couchbase database integration.

## Prerequisites

### Local Development
- Java 17 or higher
- Maven 3.6 or higher
- Couchbase Server 7.x or higher
- Docker and Docker Compose (optional, for running Couchbase)

### Docker Deployment
- Docker
- Docker Compose

## Environment Setup

1. Copy `env.properties.sample` to `env.properties`:
```bash
cp env.properties.sample env.properties
```

2. Update the values in `env.properties` according to your environment.

## Local Development

### 1. Start Couchbase Server

#### Option 1: Using Docker
```bash
docker run -d --name couchbase \
    -p 8091-8096:8091-8096 \
    -p 11210:11210 \
    couchbase:latest
```

Then access Couchbase Web UI at http://localhost:8091 and:
1. Create a new cluster
2. Create a bucket named `todo-app`
3. Create a user with username `Administrator` and password `password`

#### Option 2: Using Couchbase Server
1. Download and install Couchbase Server from [Couchbase Downloads](https://www.couchbase.com/downloads/)
2. Start Couchbase Server
3. Create a bucket named `todo-app`
4. Create a user with username `Administrator` and password `password`

### 2. Build the Application
```bash
mvn clean install
```

### 3. Run Tests
```bash
mvn test
```

### 4. Run the Application
```bash
mvn spring-boot:run
```

The application will be available at http://localhost:8080

## Docker Deployment

### 1. Build Docker Image
```bash
docker build -t todo-app-backend .
```

### 2. Run with Docker Compose
```bash
docker-compose up -d
```

This will start both the application and Couchbase Server.

## API Documentation

Once the application is running, you can access:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI Documentation: http://localhost:8080/api-docs

## API Endpoints

### Authentication
- POST `/auth/register` - Register a new user
- POST `/auth/authenticate` - Authenticate a user

### Todo Lists
- GET `/lists` - Get all todo lists
- POST `/lists` - Create a new todo list
- PATCH `/lists/{id}` - Update a todo list
- DELETE `/lists/{id}` - Delete a todo list

### Notes
- GET `/lists/{listId}/notes` - Get all notes in a list
- POST `/lists/{listId}/notes` - Create a new note
- GET `/lists/{listId}/notes/{noteId}` - Get a specific note
- PATCH `/lists/{listId}/notes/{noteId}` - Update a note
- DELETE `/lists/{listId}/notes/{noteId}` - Delete a note

## Environment Variables

The following environment variables can be configured:

| Variable | Description | Default |
|----------|-------------|---------|
| COUCHBASE_USERNAME | Couchbase username | Administrator |
| COUCHBASE_PASSWORD | Couchbase password | password |
| COUCHBASE_CONNECTION_STRING | Couchbase connection string | couchbase://localhost |
| COUCHBASE_BUCKET_NAME | Couchbase bucket name | todo-app |
| JWT_SECRET | JWT signing key | - |
| JWT_EXPIRATION | JWT token expiration in milliseconds | 86400000 |
| SERVER_PORT | Application port | 8080 |

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

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request 