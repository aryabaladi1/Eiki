# Eiki

Eiki is a full-stack habit tracking application built with Kotlin Spring Boot, React, and PostgreSQL.

## Features

- JWT authentication
- Create and manage habits
- Track daily progress
- Weekly habit statistics

## Tech Stack

**Backend**
- Kotlin
- Spring Boot
- PostgreSQL

**Frontend**
- React
- TypeScript
- Axios

**DevOps**
- Docker
- Docker Compose
- Nginx

## Running the Project

### Requirements

- Docker Desktop

### Setup

Clone the repository:

```bash
git clone <repository-url>
cd habit-tracker-2
```

Create a `.env` file based on `.env.example`.

Run:

```bash
docker compose up --build
```

The application will be available at:

Frontend:
```
http://localhost:3000
```

Backend API:
```
http://localhost:8080
```

## Project Structure

```
habit-tracker-2
├── backend
├── client
├── docker-compose.yml
└── README.md
```

## Future Improvements

- Dashboard improvements
- More analytics
- Better UI/UX
- Automated testing
- CI/CD pipeline