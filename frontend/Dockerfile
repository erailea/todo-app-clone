# 🏗️ Build stage
FROM node:23-alpine AS build

WORKDIR /app

# Copy package files and install dependencies
COPY package*.json ./
RUN npm ci 

# Copy source code
COPY . .

# Set build argument and environment variable for API URL
ARG VITE_API_BASE_URL=
ENV VITE_API_BASE_URL=${VITE_API_BASE_URL}

# Set environment variable for production build
ENV NODE_ENV=production

# Build the application
RUN npm run build

# 🚀 Production stage (serve with nginx)
FROM nginx:alpine

# Create non-root user for security
RUN addgroup -g 1001 -S nodejs && \
    adduser -S nextjs -u 1001

# Copy built app from build stage
COPY --from=build /app/dist /usr/share/nginx/html

# Copy nginx config template and startup script
COPY nginx.conf.template /etc/nginx/nginx.conf.template
COPY start.sh /start.sh

# Create required directories and set permissions
RUN mkdir -p /var/cache/nginx /var/log/nginx /tmp && \
    chown -R 1001:1001 /var/cache/nginx /var/log/nginx /tmp /usr/share/nginx/html /etc/nginx && \
    chmod +x /start.sh

# Switch to non-root user
USER 1001

# Use dynamic port (set by Heroku)
EXPOSE $PORT

CMD ["/start.sh"]