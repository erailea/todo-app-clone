#!/bin/sh

# Set default values
export PORT=${PORT:-80}
export VITE_API_BASE_URL=${VITE_API_BASE_URL:-https://api.example.com}

echo "Starting application with:"
echo "PORT: $PORT"
echo "VITE_API_BASE_URL: $VITE_API_BASE_URL"

# Create temporary directories for non-root user
mkdir -p /tmp/client_temp
mkdir -p /tmp/proxy_temp_path
mkdir -p /tmp/fastcgi_temp
mkdir -p /tmp/uwsgi_temp
mkdir -p /tmp/scgi_temp

# Replace API URL placeholders in built files
echo "Injecting environment variables into built application..."
find /usr/share/nginx/html -name "*.js" -exec sed -i "s|https://api.example.com|$VITE_API_BASE_URL|g" {} \;
find /usr/share/nginx/html -name "*.html" -exec sed -i "s|https://api.example.com|$VITE_API_BASE_URL|g" {} \;

# Replace PORT placeholder in nginx config
echo "Generating nginx configuration..."
sed "s/\$PORT/$PORT/g" /etc/nginx/nginx.conf.template > /etc/nginx/nginx.conf

# Test nginx configuration
echo "Testing nginx configuration..."
nginx -t

if [ $? -eq 0 ]; then
    echo "Nginx configuration is valid. Starting nginx..."
    exec nginx -g "daemon off;"
else
    echo "Nginx configuration is invalid!"
    exit 1
fi 