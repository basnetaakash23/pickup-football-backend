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

```docker-compose up --build ```

This will:

Build the Spring Boot app image using your Dockerfile

Spin up the pickup-football-db PostgreSQL container

Spin up the pickup-football-app Spring Boot container

Wire both together in a shared Docker network

```http://localhost:8084```

```GET http://localhost:8084/games/get-active-games```

### 📘 Documented Process: Docker Network, PostgreSQL, and Spring Boot
✅ 1. Create a User-Defined Docker Network
This network allows containers to communicate via container names (e.g., postgres).

bash
Copy
Edit
docker network create spring-net
✅ 2. Run PostgreSQL Container on This Network
bash
Copy
Edit
docker run -d \
--name postgres \
--network spring-net \
-e POSTGRES_DB=football \
-e POSTGRES_USER=username \
-e POSTGRES_PASSWORD=password \
-p 5432:5432 \
postgres
🔍 Explanation:
--network spring-net: Adds the container to the shared network.

--name postgres: Allows other containers to reach it via postgres hostname.

-p 5432:5432: Exposes the DB to the host (optional if only accessed from Spring Boot).

docker run -d \
--name springboot-app \
--network spring-net \
-p 8084:8088 \
-e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/football \
-e SPRING_DATASOURCE_USERNAME=username \
-e SPRING_DATASOURCE_PASSWORD=password \
*********.dkr.ecr.us-east-1.amazonaws.com/pickup-football-app:latest

