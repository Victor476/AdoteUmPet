# Use Node.js 18 as base image
FROM node:18-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json (if available)
COPY package*.json ./

# Install dependencies (development for hot-reload)
RUN npm install

# Copy the rest of the application code
COPY . .

# Expose the port that Next.js runs on
EXPOSE 3000

# Default to development server for hot reloading
CMD ["npm", "run", "dev"]
