# Walid Demo - Country City Service

Application microservices avec authentification Keycloak, monitoring Prometheus/Grafana, et gateway API.

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    API Gateway (8085)                   │
│              Spring Cloud Gateway + OAuth2              │
└────────────────────────┬────────────────────────────────┘
                         │
        ┌────────────────┼────────────────┐
        │                │                │
┌───────▼────────┐ ┌─────▼──────────┐ ┌──▼──────────────┐
│ Country/City   │ │   Keycloak     │ │   PostgreSQL    │
│   Service      │ │    (8080)      │ │    (5432)       │
│    (8081)      │ │                │ │                 │
└────────────────┘ └────────────────┘ └─────────────────┘

        ┌──────────────────────────────┐
        │                              │
┌───────▼──────────┐          ┌────────▼─────────┐
│  Prometheus      │          │    Grafana       │
│    (9090)        │          │     (3000)       │
└──────────────────┘          └──────────────────┘
```

## 🚀 Démarrage rapide

### Prérequis
- Docker & Docker Compose v5.1+
- Ports disponibles: 8081, 8085, 8080, 5432, 9090, 3000

### Lancer les services
```bash
cd /Users/walidhamat/Desktop/delete/walid-demo
docker compose up -d
```

### Vérifier le statut
```bash
docker compose ps
```

## 🔐 Credentials

### Keycloak (http://localhost:8080)
- **Username:** admin
- **Password:** admin
- **Realm:** country-system

### Grafana (http://localhost:3000)
- **Username:** admin
- **Password:** admin (tu dois le changer au premier login)

### PostgreSQL (localhost:5432)
- **User:** admin
- **Password:** admin
- **Database:** countrydb

## 📊 Services

| Service | Port | URL | Description |
|---------|------|-----|-------------|
| **Country/City Service** | 8081 | http://localhost:8081 | Microservice métier (Spring Boot) |
| **API Gateway** | 8085 | http://localhost:8085 | Routage centralisé des requêtes |
| **Keycloak** | 8080 | http://localhost:8080 | Authentification OAuth2/OIDC |
| **PostgreSQL** | 5432 | localhost:5432 | Base de données |
| **Prometheus** | 9090 | http://localhost:9090 | Collecte des métriques |
| **Grafana** | 3000 | http://localhost:3000 | Visualisation des métriques |

---

## 📈 Monitoring avec Prometheus & Grafana

### 📊 Prometheus

**URL:** http://localhost:9090

Prometheus collecte automatiquement les métriques toutes les 15 secondes depuis:
- ✅ Country/City Service (`/actuator/prometheus`)
- ✅ API Gateway (`/actuator/prometheus`)
- ✅ Keycloak (port 8080)
- ✅ PostgreSQL (port 5432)

#### Configuration
Les cibles scrape sont définies dans `prometheus/prometheus.yml`:
```yaml
scrape_configs:
  - job_name: 'country-city-service'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['country-city-service:8081']

  - job_name: 'api-gateway'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['api-gateway:8085']
```

#### Vérifier les targets scrape
```bash
# Voir tous les targets
curl -s http://localhost:9090/api/v1/targets | jq '.data.activeTargets'

# Voir les targets down
curl -s http://localhost:9090/api/v1/targets | jq '.data.droppedTargets'
```

#### Métriques disponibles
- `up` - Statut des services (1=healthy, 0=down)
- `jvm_memory_used_bytes` - Mémoire JVM utilisée
- `jvm_memory_max_bytes` - Mémoire max disponible
- `process_cpu_usage` - Utilisation CPU
- `process_resident_memory_bytes` - Mémoire résidente
- `http_server_requests_seconds_count` - Nombre total de requêtes
- `http_server_requests_seconds_sum` - Temps total des requêtes
- `http_server_requests_seconds_bucket` - Distribution des latences

#### Tester une requête Prometheus
```bash
# Tous les services up?
curl -s 'http://localhost:9090/api/v1/query?query=up' | jq '.data.result'

# JVM Memory pour country-city-service
curl -s 'http://localhost:9090/api/v1/query?query=jvm_memory_used_bytes{job="country-city-service"}' | jq '.data.result'
```

---

### 📈 Grafana

**URL:** http://localhost:3000

**Credentials:**
- Username: `admin`
- Password: `admin` (tu dois le changer au premier login)

#### Dashboard prêt à l'emploi
Le dashboard **"Walid Demo - Application Metrics"** est chargé automatiquement avec 4 panels:

1. **Service Status** - Affiche `up` pour chaque service (courbe verte = 1 = healthy)
2. **JVM Memory Used** - Mémoire utilisée par les services Java
3. **HTTP Requests** - Taux de requêtes HTTP (5 min)
4. **CPU Usage** - Utilisation CPU en pourcentage

Le dashboard se rafraîchit **toutes les 10 secondes**.

#### Accéder au Dashboard
1. Va sur http://localhost:3000
2. Login avec `admin` / `admin`
3. Change ton mot de passe (optionnel)
4. Clique sur **Dashboards** → **Walid Demo - Application Metrics**

#### Créer un nouveau panel
1. Dans le dashboard, clique **+ Add panel**
2. Dans l'éditeur, tu verras **Prometheus** comme datasource
3. Écris une query PromQL (voir ci-dessous)
4. Clique **Run query** ou appuie sur **Shift+Enter**
5. Configure le titre, unités, et range
6. Clique **Save**

#### Queries PromQL utiles pour créer des panels:

**Statut des services**
```promql
up
```

**Mémoire JVM (en MB)**
```promql
jvm_memory_used_bytes / 1024 / 1024
```

**Taux de requêtes HTTP (5 min)**
```promql
rate(http_server_requests_seconds_count[5m])
```

**Utilisation CPU (%)**
```promql
process_cpu_usage * 100
```

**Latence moyenne des requêtes (ms)**
```promql
(rate(http_server_requests_seconds_sum[5m]) / rate(http_server_requests_seconds_count[5m])) * 1000
```

**Nombre de requêtes par statut HTTP**
```promql
rate(http_server_requests_seconds_count{status=~"2.."}[5m])
```

**Mémoire disponible vs utilisée**
```promql
jvm_memory_max_bytes - jvm_memory_used_bytes
```

#### Datasources Grafana
Prometheus est pré-configuré comme datasource:
- **Name:** Prometheus
- **URL:** http://prometheus:9090
- **Type:** Prometheus

Pour voir/modifier:
1. Grafana → **Configuration** (gear icon) → **Data sources**
2. Clique **Prometheus**

#### Auto-provisioning des dashboards
Les dashboards sont auto-chargés depuis `grafana/provisioning/dashboards/`:
- `default-dashboard.json` - Dashboard par défaut (Walid Demo)

Pour ajouter un nouveau dashboard:
1. Crée un fichier `.json` dans `grafana/provisioning/dashboards/`
2. Redémarre Grafana: `docker compose restart grafana`

---

### 🔗 Workflow complet pour tester

1. **Faire une requête API:**
   ```bash
   curl -s http://localhost:8085/countries | jq '.'
   ```

2. **Attendre 15 secondes** (intervalle de scrape Prometheus)

3. **Voir les métriques dans Prometheus:**
   - Va sur http://localhost:9090
   - Execute: `http_server_requests_seconds_count`
   - Tu verras les nouvelles requêtes

4. **Voir le graphique dans Grafana:**
   - Va sur http://localhost:3000
   - Dashboard **Walid Demo - Application Metrics**
   - Le panel **HTTP Requests** se met à jour

---

## 🔍 Tester l'API

### Via API Gateway
```bash
# Récupérer tous les pays
curl -s http://localhost:8085/countries | jq '.'

# Récupérer une pays par ID
curl -s http://localhost:8085/countries/1 | jq '.'

# Récupérer les villes d'un pays
curl -s http://localhost:8085/countries/1/cities | jq '.'

# Documentation Swagger
http://localhost:8085/swagger-ui.html
```

### Vérifier les métriques Prometheus
```bash
# Tous les services up?
curl -s 'http://localhost:9090/api/v1/query?query=up' | jq '.data.result'

# Métriques JVM
curl -s 'http://localhost:9090/api/v1/query?query=jvm_memory_used_bytes{job="country-city-service"}' | jq '.data.result'

# Métriques HTTP
curl -s 'http://localhost:9090/api/v1/query?query=http_server_requests_seconds_count' | jq '.data.result'
```

### Endpoints Actuator (non sécurisés pour monitoring)
```bash
# Health check
curl -s http://localhost:8081/actuator/health | jq '.'

# Métriques Prometheus
curl -s http://localhost:8081/actuator/prometheus | head -20

# Metrics disponibles
curl -s http://localhost:8081/actuator/metrics | jq '.names'
```

## 📝 Structure du Projet

```
walid-demo/
├── country-city-service/          # Microservice métier
│   ├── src/
│   │   └── main/java/.../
│   ├── pom.xml                    # Dépendances (Micrometer Prometheus)
│   └── Dockerfile
├── api-gateway/                   # Spring Cloud Gateway
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── keycloak/                      # Config Keycloak
│   └── realm.json                 # Realm exportée
├── prometheus/                    # Config Prometheus
│   └── prometheus.yml             # Scrape targets
├── grafana/                       # Config Grafana
│   └── provisioning/
│       ├── datasources/           # Datasource Prometheus
│       └── dashboards/            # Dashboards auto-chargés
├── docker-compose.yml             # Orchestration
└── README.md
```

## 🔧 Configuration

### Ajouter une nouvelle métrique (Spring Boot)
1. Injecter `MeterRegistry` dans ton contrôleur:
```java
@RestController
public class MyController {
    @Autowired
    MeterRegistry meterRegistry;
    
    @GetMapping("/endpoint")
    public void endpoint() {
        meterRegistry.counter("my_counter", "label", "value").increment();
    }
}
```

2. La métrique apparaît automatiquement dans Prometheus

### Ajouter un nouveau dashboard Grafana
1. Créer un fichier JSON dans `grafana/provisioning/dashboards/`
2. Redémarrer Grafana:
```bash
docker compose restart grafana
```

## 📋 Logs

```bash
# Logs d'un service
docker compose logs country-city-service -f

# Tous les logs
docker compose logs -f

# Dernières 50 lignes
docker compose logs --tail=50
```

## 🛑 Arrêter les services

```bash
# Arrêter sans supprimer les volumes
docker compose stop

# Arrêter et supprimer tout (y compris les données)
docker compose down -v
```

## 🐛 Dépannage

### Les services crashent (exit 137)
- Augmenter la mémoire Docker
- Réduire les mem_limit dans docker-compose.yml

### Prometheus ne scrape pas les métriques
```bash
# Vérifier les targets
curl -s http://localhost:9090/api/v1/targets | jq '.data.activeTargets'

# Vérifier la config Prometheus
cat prometheus/prometheus.yml
```

### Grafana ne charge pas les dashboards
```bash
# Redémarrer Grafana
docker compose restart grafana

# Vérifier les logs
docker compose logs grafana
```

### Keycloak: les services ne se connectent pas
- Vérifier que Keycloak est healthy: `docker compose logs keycloak`
- Vérifier les URIs OAuth2 dans `application-docker.properties`

### Endpoints /actuator retournent 401 Unauthorized
- Vérifier que les endpoints sont permis dans `SecurityConfig.java`:
```java
.requestMatchers(
    "/actuator/prometheus",
    "/actuator/health",
    "/actuator/metrics"
).permitAll()
```
- Rebuild: `docker compose build --no-cache country-city-service`

## 📚 Documentation utile

- [Spring Boot Actuator](https://spring.io/guides/gs/centralized-configuration/)
- [Prometheus Queries](https://prometheus.io/docs/prometheus/latest/querying/basics/)
- [Grafana Dashboards](https://grafana.com/docs/grafana/latest/dashboards/)
- [Keycloak OIDC](https://www.keycloak.org/docs/latest/server_admin/)
- [Micrometer Prometheus](https://micrometer.io/docs/registry/prometheus)

## 📞 Support

Pour des questions sur l'architecture ou la configuration, consulte les fichiers:
- `docker-compose.yml` - Configuration des services
- `prometheus/prometheus.yml` - Targets scrape
- `grafana/provisioning/datasources/prometheus.yml` - Datasource Grafana
- `grafana/provisioning/dashboards/default-dashboard.json` - Dashboard par défaut
