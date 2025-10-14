# Jenkins Demo - Node.js Application

A basic Node.js application built with Express for Jenkins CI/CD demonstration.

## Prerequisites

- Node.js (v14 or higher)
- npm (comes with Node.js)

## Installation

1. Install dependencies:
```bash
npm install
```

## Running the Application

Start the server:
```bash
npm start
```

The application will run on `http://localhost:3000`

## Available Endpoints

- `GET /` - Welcome message
- `GET /health` - Health check endpoint
- `GET /api/info` - Application information

## Testing the Application

Once the server is running, you can test it using:

```bash
# Test the root endpoint
curl http://localhost:3000

# Test the health endpoint
curl http://localhost:3000/health

# Test the info endpoint
curl http://localhost:3000/api/info
```

## Project Structure

```
jenkins-demo/
├── index.js          # Main application file
├── package.json      # Project dependencies and scripts
├── .gitignore       # Git ignore rules
└── README.md        # This file
```

## Environment Variables

- `PORT` - Server port (default: 3000)
- `NODE_ENV` - Environment (development/production)

