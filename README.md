# 🌍 Microservices Country-City Project

This project is a robust **microservices architecture** built with **Spring Boot **. The entire infrastructure and services are containerized using **Docker**.

---

## 🚀 Key Features

* **API Gateway Routing:** Single entry point handling reactive request routing.
* **Centralized Security:** Resource protection via **Keycloak** (OAuth2 / *Client Credentials* flow).
* **API Documentation:** Swagger UI integrated and secured, accessible through the Gateway.
* **Observability:** Service health monitoring via Spring Boot Actuator.
* **Monitoring:** Metrics visualization with **Prometheus & Grafana**.
* **Persistent Database:** Containerized PostgreSQL database.

---

# 🏗️ System Architecture

The diagram below illustrates the flow of a typical request through the Gateway and the Docker-based ecosystem:

```text
                  [ Web Browser / Postman ]
                                │
         ┌──────────────────────┴──────────────────────┐
         ▼ (Port 8085)                                 ▼ (Port 8080)
┌─────────────────┐                             ┌──────────────┐
│   API Gateway   │                             │   Keycloak   │
└────────┬────────┘                             └──────┬───────┘
         │ (Internal Docker Network)                  │
         ▼ (Port 8081)                                │
┌──────────────────────┐                              │
│ Country-City Service ◄──────────────────────────────┘
└────────┬─────────────┘     JWT Token Verification
         │ (Port 5432)
         ▼
┌──────────────────┐
│ PostgreSQL (DB)  │
└──────────────────┘
```

---

# 🛠️ Prerequisites

Before running the project, make sure you have installed:

* **Docker & Docker Compose**
* **Postman**

---

# 🏁 Quick Start (All-in-One via Docker)

The entire architecture can be started with a single command using `docker-compose.yml`, configured with health checks to ensure proper startup order:

```bash
# 1. Clone the repository and navigate into the project
git clone <your-repository-url>
cd country-city-project

# 2. Start the full stack
docker compose up -d --build
```

---

### ⏱️ Automated Startup Order:

1. `postgres` starts and waits until it is ready.
2. `keycloak` starts and automatically imports the secured realm `country-system` from `./keycloak/realm.json`.
3. `country-city-service` builds its JAR and starts once DB and Keycloak are healthy.
4. `api-gateway` starts last to expose all routes.
5. `prometheus` starts and scrapes Spring Boot metrics.
6. `grafana` starts and loads dashboards automatically.

---

# 📦 Components & Ports

| Service                  | External Port | Description                             | Key URL                                      |
| ------------------------ | ------------- | --------------------------------------- | -------------------------------------------- |
| **API Gateway**          | `8085`        | Single entry point of the system        | `http://localhost:8085`                      |
| **Country-City Service** | `8081`        | Business logic for countries and cities | `http://localhost:8081`                      |
| **Keycloak**             | `8080`        | Identity & Access Management (OAuth2)   | `http://localhost:8080`                      |
| **PostgreSQL**           | `5432`        | Relational database                     | `jdbc:postgresql://localhost:5432/countrydb` |
| **Prometheus**           | `9090`        | Metrics collection system               | `http://localhost:9090`                      |
| **Grafana**              | `3000`        | Metrics visualization dashboard         | `http://localhost:3000`                      |

---

# 📖 Swagger UI

The interactive API documentation is exposed centrally via the Gateway:

👉 **URL:** `http://localhost:8085/swagger-ui/index.html`

---

### 🔐 How to authenticate in Swagger UI

This project uses the **Client Credentials (Machine-to-Machine)** flow.

1. Click the green **Authorize** button (top right).
2. Enter the Keycloak Realm credentials:

* **client_id:** `api-gateway`
* **client_secret:** `gateway-secret`

3. Click **Authorize**, then close the window. Locked endpoints will now be accessible.

---

# 📊 Observability (Prometheus & Grafana)

This project includes a full monitoring stack for metrics collection and visualization.

---

## 📈 Prometheus

Prometheus collects metrics exposed by Spring Boot Actuator.

📍 URL: [http://localhost:9090](http://localhost:9090)

### Scraped services:

* API Gateway (`/actuator/prometheus`)
* Country-City Service (`/actuator/prometheus`)

### Useful queries:

```promql
up
```

```promql
http_server_requests_seconds_count
```

---

## 📊 Grafana

Grafana is used to visualize metrics collected by Prometheus.

📍 URL: [http://localhost:3000](http://localhost:3000)
👤 Username: admin
🔑 Password: admin

### Included dashboards:

* JVM Memory Usage
* HTTP Request Rate
* Service Health (up)
* CPU Usage

### Data source:

* Prometheus: `http://prometheus:9090`

---

## 🔄 Monitoring Flow

```text
Spring Boot Services
        ↓
Prometheus (metrics scraping)
        ↓
Grafana (visual dashboards)
```

---

# 📡 Testing Guide & Postman Collection

A pre-configured Postman collection is available at the root of the project.

---

### 🔀 General Usage Flow

> **Important:** Execute requests in order. Request `1 - Get Keycloak Token` retrieves the access token and automatically stores it in `{{keycloak_access_token}}` for subsequent requests.

---

### 🛠️ Local Environment Configuration

| Service     | Default URL             |
| ----------- | ----------------------- |
| Keycloak    | `http://localhost:8080` |
| API Gateway | `http://localhost:8085` |

---

### 📋 Request List

#### 1 - Get Keycloak Token

Obtains an OAuth2 access token using the **Client Credentials** flow.

* **Method:** POST
* **URL:** [http://localhost:8080/realms/country-system/protocol/openid-connect/token](http://localhost:8080/realms/country-system/protocol/openid-connect/token)
* **Headers:** Content-Type: application/x-www-form-urlencoded

| Key           | Value              |
| ------------- | ------------------ |
| grant_type    | client_credentials |
| client_id     | api-gateway        |
| client_secret | gateway-secret     |

---

#### 2 - Get Countries (Gateway Secured)

GET [http://localhost:8085/countries](http://localhost:8085/countries)
Authorization: Bearer {{keycloak_access_token}}

---

#### 3 - Service Health

GET [http://localhost:8085/actuator/health](http://localhost:8085/actuator/health)
Authorization: Bearer {{keycloak_access_token}}

---

#### 4 - Get City Details

GET [http://localhost:8085/cities/{id}](http://localhost:8085/cities/{id})

---

#### 5 - Get Cities by Country

GET [http://localhost:8085/countries/{id}/cities?page=0&size=10](http://localhost:8085/countries/{id}/cities?page=0&size=10)

---

# 🧪 Automated Tests (Maven)

```bash
mvn test
```

---

# 🛑 Stopping the Project

```bash
docker compose down
```

> PostgreSQL data persists thanks to the Docker volume `postgres_data`.