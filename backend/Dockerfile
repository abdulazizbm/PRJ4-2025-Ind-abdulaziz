# Use Node.js base image
FROM node:18-slim

# Set non-root user
USER node

# Create app directory
RUN mkdir -p /home/node/app
WORKDIR /home/node/app

# Copy package files and install dependencies
COPY --chown=node package*.json ./
RUN npm install

# Copy app source code
COPY --chown=node . .

# Build the LoopBack project
RUN npm run build

# Expose LoopBack REST API port
ENV HOST=0.0.0.0
ENV PORT=3000
EXPOSE 3000

# Start the LoopBack app
CMD ["node", "dist/index.js"]
