# Todo App Clone

A full-stack Todo application built with Spring Boot and Vue.js.

## Live Demo
- [Frontend Application][frontend-url]
- [API Documentation (Swagger UI)][swagger-url]
- [API Base URL][api-url]

## Quick Start

### Prerequisites
- Java 17 or higher
- Node.js 18 or higher
- Docker and Docker Compose

### Build and Run

1. Clone the repository:
```bash
git clone https://github.com/erailea/todo-app-clone.git
cd todo-app-clone
```

2. Start the application with Docker Compose:
```bash
docker-compose up -d
```

The application will be available at:
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html

### Run Tests

#### Backend Tests
```bash
cd backend
mvn test
```

## Documentation

For more detailed information about each component, please refer to:
- [Backend Documentation](backend/README.md)
- [Frontend Documentation](frontend/README.md)

[frontend-url]: https://to-do-app-clone-fe-9426774d4821.herokuapp.com/dashboard
[swagger-url]: https://to-do-app-clone-be-ff03bad65820.herokuapp.com/swagger-ui/index.html
[api-url]: https://to-do-app-clone-be-ff03bad65820.herokuapp.com