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

# Create non-root user for security
RUN addgroup -g 1001 -S nodejs && \
    adduser -S nextjs -u 1001

# Static build output
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