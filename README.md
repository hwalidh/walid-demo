# 🌍 Microservices Country-City Project

This project is a robust **microservices architecture** built with **Spring Boot 3.x** and **Spring Cloud**. The entire infrastructure and services are containerized using **Docker**.

---

## 🚀 Key Features

* **API Gateway Routing:** Single entry point handling reactive request routing.
* **Centralized Security:** Resource protection via **Keycloak** (OAuth2 / *Client Credentials* flow).
* **API Documentation:** Swagger UI integrated and secured, accessible through the Gateway.
* **Observability:** Service health monitoring via Spring Boot Actuator.
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

---

# 📦 Components & Ports

| Service                  | External Port | Description                             | Key URL                                      |
| ------------------------ | ------------- | --------------------------------------- | -------------------------------------------- |
| **API Gateway**          | `8085`        | Single entry point of the system        | `http://localhost:8085`                      |
| **Country-City Service** | `8081`        | Business logic for countries and cities | `http://localhost:8081`                      |
| **Keycloak**             | `8080`        | Identity & Access Management (OAuth2)   | `http://localhost:8080`                      |
| **PostgreSQL**           | `5432`        | Relational database                     | `jdbc:postgresql://localhost:5432/countrydb` |

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

* **Method:** `POST`
* **URL:** `http://localhost:8080/realms/country-system/protocol/openid-connect/token`
* **Headers:** `Content-Type: application/x-www-form-urlencoded`

**Body (form-urlencoded):**

| Key           | Value              |
| ------------- | ------------------ |
| grant_type    | client_credentials |
| client_id     | api-gateway        |
| client_secret | gateway-secret     |

**Post-script:** Automatically extracts `access_token` and stores it in `{{keycloak_access_token}}`.

---

#### 2 - Get Countries (Gateway Secured)

* **Method:** `GET`
* **URL:** `http://localhost:8085/countries`
* **Headers:** `Authorization: Bearer {{keycloak_access_token}}`

---

#### 3 - Service Health

* **Method:** `GET`
* **URL:** `http://localhost:8085/actuator/health`
* **Headers:** `Authorization: Bearer {{keycloak_access_token}}`

---

#### 4 - Get City Details

* **Method:** `GET`
* **URL:** `http://localhost:8085/cities/{id}`
  Example: `http://localhost:8085/cities/1`
* **Headers:** `Authorization: Bearer {{keycloak_access_token}}`

---

#### 5 - Get Cities by Country

* **Method:** `GET`
* **URL:** `http://localhost:8085/countries/{id}/cities`
  Example: `http://localhost:8085/countries/1/cities?page=0&size=10`

**Query params:**

| Parameter | Default | Description              |
| --------- | ------- | ------------------------ |
| page      | 0       | Page number              |
| size      | 10      | Number of items per page |

* **Headers:** `Authorization: Bearer {{keycloak_access_token}}`

---

### 📌 Postman Collection Variables

| Variable                | Description                    | Auto-filled        |
| ----------------------- | ------------------------------ | ------------------ |
| `keycloak_access_token` | JWT access token from Keycloak | ✅ Yes (Request #1) |

---

# 🧪 Automated Tests (Maven)

To run the full test suite (controllers, services, exception handling, pagination validation):

```bash
mvn test
```

---

# 🛑 Stopping the Project

To stop all containers and free system resources:

```bash
docker compose down
```

> **Note:** PostgreSQL data persists between restarts thanks to the Docker volume `postgres_data`.