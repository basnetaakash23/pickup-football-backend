PickupFootball Application

Functions:
- Display lists of available football games around the area
- Allows registering for football games

APIs
- games/register-games
- games/create-games
- venues/get-all-venues

Features
- RESTful Web services

Technologies
- Spring Boot
- Spring Data JPA
- Hibernate
- Spring Security
- PostgreSQL
- Java 17


# 🏈 Pickup Football App

A Spring Boot backend for managing pickup football games, packaged with PostgreSQL using Docker Compose.

---

## 🐳 Run the App with Docker Compose

> Requires: [Docker & Docker Compose](https://docs.docker.com/compose/install/)

### 🔧 Step 1: Build the Project

If you're starting from source:

```bash
./mvnw clean package -DskipTests
```

docker-compose up --build
This will:

Build the Spring Boot app image using your Dockerfile

Spin up the pickup-football-db PostgreSQL container

Spin up the pickup-football-app Spring Boot container

Wire both together in a shared Docker network

```http://localhost:8084```

```GET http://localhost:8084/games/get-active-games```
 
