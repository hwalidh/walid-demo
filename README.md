# Microservices Country-City Project

This project is a simple microservices architecture built with Spring Boot.

It demonstrates:
- API Gateway routing
- Microservice communication
- REST APIs for countries and cities
- Pagination support
- Global exception handling
- Swagger/OpenAPI documentation
- Actuator health monitoring

---

# 🛠️ Prerequisites

Before running the project, ensure you have the following tools installed:
* **Java 21**
* **Maven 3.9+**
* **Git**
* **Docker & Docker Compose** (required for the PostgreSQL database)
* **Postman** (API testing tools)

---

## 💾 Database Setup

This project uses **PostgreSQL**. A Docker Compose file is provided to spin up the database locally.

* **Start the database:**
    ```bash
    docker compose up -d
    ```
* **Stop the database:**
    ```bash
    docker compose down
    ```

---

# 🧩 Architecture

Client -> API Gateway (8085) -> Country-City Service (8081) -> PostgreSQL
Or
Client -> Country-City Service (8081) -> PostgreSQL

---

# 🚀 Services

## 1. API Gateway
- Port: 8085
- Entry point for all requests
- Routes requests to backend services
- Exposes aggregated health endpoint

## 2. Country-City Service
- Port: 8081
- Business logic service
- Manages countries and cities
- Provides REST APIs

---

# 📡 Main APIs (via Gateway)

- GET /countries
- GET /countries/{id}/cities?page=&size=
- GET /cities/{id}

---

# API Testing & Collections

Two Postman/Bruno collections are available in the project to test the endpoints:

Direct Service Collection: Calls Country City Service directly via port 8081.

Gateway Collection: Routes requests through the API Gateway via port 8085.

Main Endpoints

GET /countries

GET /countries/{countryId}/cities?page=0&size=10

GET /cities/{cityId}

# 🩺 Health Monitoring

- Gateway health: `/actuator/health`
- Service health: `/actuator/health`
- Aggregated health: `/health/all`

---

# 📖 Swagger

Swagger UI available via Gateway:

- http://localhost:8085/swagger-ui/index.html

---

# 🧪 Testing

Run tests:

bash
mvn test
Covers:
* Controller tests
* Service tests
* Exception handling
* Pagination

🚀 How to run
1. Start Country-City Service (8081)
2. Start API Gateway (8085)
3	Access APIs via Gateway
---
