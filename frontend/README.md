# Frontend Documentation

## Overview
Vue.js frontend with Vuex state management, Vue Router, and responsive design.

## Setup

1. Copy environment file:
```bash
cp .env.example .env
```

2. Update `.env` with your configuration:
- VITE_API_BASE_URL (default: http://localhost:8080)
- VITE_DEV_SERVER_PORT (default: 3000)
- VITE_DEV_SERVER_HOST (default: true)

## Development

### Install Dependencies
```bash
npm install
```

### Run Development Server
```bash
npm run dev
```

### Build for Production
```bash
npm run build
```

### Run Tests
```bash
npm test
```

## Features
- Vue.js 3 with Composition API
- Vuex State Management
- Vue Router for Navigation
- Axios for HTTP Requests
- JWT Token Authentication
- Responsive Design
- Cookie-based Authentication Storage 