# 🏗️ Build stage
FROM node:20-alpine AS build

WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY . .
ENV VITE_API_BASE_URL=https://api.example.com
RUN npm run build

# 🚀 Production stage (serve with nginx)
FROM nginx:alpine

# Static build output
COPY --from=build /app/dist /usr/share/nginx/html

# Optional: custom nginx config
# COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]