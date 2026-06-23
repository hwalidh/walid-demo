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

# 🧩 Architecture

Client ↓ API Gateway (8085) ↓ Country-City Service (8081) ↓ PostgreSQL (there is an docker-compose file to create)
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
