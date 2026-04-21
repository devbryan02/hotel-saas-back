# API SaaS Hotelera Multi‑Tenant

Backend para un **sistema de gestión hotelera multi‑tenant** orientado a hoteles/negocios en Ayacucho, Perú.  
Construido con **Java 21** y **Spring Boot 4.0.2**, con autenticación **stateless (JWT)**, persistencia **PostgreSQL + JPA**, migraciones **Flyway**, y un stack de **observabilidad (Prometheus + Grafana)** listo vía Docker Compose.
---

## Qué demuestra este proyecto (para recruiters)

- **Multi‑Tenant**: diseño de datos aislado por `tenant_id` para soportar múltiples hoteles/empresas en un solo backend.
- **Arquitectura por capas (estilo Clean/Hexagonal)**: separación clara entre *domain* y *adapters* (web/persistencia/seguridad).
- **Spring Security + JWT (Stateless)**:
   - filtro custom `JwtAuthFilter`
   - servicio `JwtService`
   - claims incluyendo `tenantId` para autorización multi‑tenant
- **RBAC (Role-Based Access Control)**: reglas explícitas por rutas en `SecurityConfig`.
- **Persistencia y calidad de datos**: Spring Data JPA + Validation.
- **Migraciones SQL versionadas**: Flyway.
- **Observabilidad real**: Actuator + métricas Prometheus + dashboards con Grafana.
- **DevOps readiness**: Dockerfile multi-stage y Docker Compose con DB + App + Observabilidad.

---

## Stack / Tecnologías

- **Java 21**
- **Spring Boot 4.0.2**
   - Spring Web MVC
   - Spring Data JPA (Hibernate)
   - Spring Security
   - Validation
   - Actuator
- **PostgreSQL**
- **Flyway** (migraciones SQL)
- **JWT** (`io.jsonwebtoken`)
- **MapStruct** + **Lombok**
- **OpenAPI/Swagger UI**: `springdoc-openapi-starter-webmvc-ui`
- **Rate limiting**: Bucket4j
- **Observabilidad**: Micrometer + Prometheus + Grafana
- **Docker / Docker Compose**

---
## Estructura del proyecto

Resumen de carpetas principales:

```text
hotel-saas-back/
├─ src/
│  ├─ main/
│  │  ├─ java/
│  │  │  └─ (código fuente)
│  │  │     ├─ application/        # casos de uso / lógica de aplicación
│  │  │     ├─ domain/             # lógica de negocio (modelos + contratos)
│  │  │     ├─ infrastructure/     # adaptadores (persistencia, seguridad, integraciones)
│  │  │     └─ web/                # API REST (controllers, DTOs, etc.)
│  │  └─ resources/
│  │     ├─ application.yaml
│  │     ├─ application-dev.yaml
│  │     ├─ application-prod.yaml
│  │     └─ database/
│  │        └─ migration/          # scripts Flyway (V1__, V2__, V3__...)
│  └─ test/
│     └─ (tests)
├─ observability/
│  └─ prometheus.yml               # configuración de Prometheus
├─ Dockerfile
├─ docker-compose.yml
├─ docker-compose-prod.yml
├─ build.gradle.kts
└─ README.md
```

Tabla rápida

| Área            | Carpeta           | Qué contiene                      |
|-----------------|-------------------|-----------------------------------|
| Negocio         | `domain/`         | modelos + contratos (core)        |
| Casos de uso    | `application/`    | servicios / orquestación          |
| Infraestructura | `infrastructure/` | persistencia, seguridad, etc.     |
| API             | `web/`            | controllers y DTOs                |
| Config          | `resources/`      | profiles + migraciones Flyway     |
| Observabilidad  | `observability/`  | config de Prometheus              |
| DevOps          | raíz del repo     | Dockerfile/compose + build Gradle |
---

## Multi‑Tenant (cómo está resuelto)

A nivel de base de datos, las entidades principales se relacionan con `tenant` a través de `tenant_id` (ver migración inicial `V1__init_schema.sql`). Tablas relevantes:

- `tenant`
- `client` → `tenant_id`
- `room` → `tenant_id`
- `occupation` → `tenant_id`, `client_id`, `room_id`
- `app_user` → `tenant_id`

Esto permite:
- aislamiento de datos por tenant
- escalabilidad para múltiples hoteles/empresas dentro del mismo backend

---

## Seguridad (JWT + RBAC)

### JWT
El token contiene:
- `subject`: email del usuario
- claims:
   - `userId`
   - `tenantId`
   - `role`

Header:
- `Authorization: Bearer <jwt>`

### Flujo
- `JwtAuthFilter` intercepta requests, valida el token y setea el `SecurityContext`.
- La app opera **stateless** (`SessionCreationPolicy.STATELESS`).

### RBAC por rutas (SecurityConfig)

| Tipo de acceso | Rutas | Roles permitidos |
|---|---|---|
| Público | `/auth/**` | — |
| Protegido | `/tenants/*/rooms/**` | `ADMIN`, `RECEPTIONIST` |
| Protegido | `/tenants/*/clients/**` | `ADMIN`, `RECEPTIONIST` |
| Protegido | `/tenants/*/occupations/**` | `ADMIN`, `RECEPTIONIST` |
| Por defecto | Cualquier otra ruta | Requiere autenticación |

### CORS

| Configuración | Valor |
|---|---|
| Origen permitido | `app.front.url` |
| Métodos permitidos | `GET, POST, PUT, DELETE, PATCH, OPTIONS` |
| Headers expuestos | `Authorization` |

---

## Base URL / Versionado de API

Prefijo global:
- `spring.mvc.servlet.path: "/api/v1"`

Base URL:
- `http://<host>:<port>/api/v1`

---

## Configuración (profiles + env vars)

Profile activo por defecto:
- `spring.profiles.active: dev`

### Variables de entorno

| Variable                     | Descripción                                              |
|------------------------------|----------------------------------------------------------|
| `PORT`                       | Puerto donde corre la API (si no se define, usa `8080`). |
| `PGHOST`                     | Host de PostgreSQL.                                      |
| `PGPORT`                     | Puerto de PostgreSQL.                                    |
| `PGDATABASE`                 | Nombre de la base de datos.                              |
| `PGUSER`                     | Usuario de PostgreSQL.                                   |
| `PGPASSWORD`                 | Contraseña de PostgreSQL.                                |
| `JWT_SECRET`                 | Secreto usado para firmar y validar tokens JWT.          |
| `ADMIN_API_KEY`              | API key para operaciones administrativas.                |
| `GF_SECURITY_ADMIN_PASSWORD` | Contraseña del usuario admin de Grafana.                 |
| `app.front.url`              | Origen permitido para CORS (URL del frontend).           |

---

## Docker Compose (Stack completo: App + DB + Observabilidad)

El proyecto incluye un entorno *local/dev* levantable con **Docker Compose**, que orquesta:

- **PostgreSQL 16** (base de datos)
- **hotel-saas-back** (Spring Boot API)
- **Prometheus** (scraping de métricas vía Actuator/Micrometer)
- **Grafana** (dashboards para visualizar métricas)

### Servicios

- **db** (`postgres:16`)
   - Persiste datos con volumen `postgres_data`.
   - Expone `5432:5432`.

- **app** (build desde el repo)
   - Expone `8080:8080`.
   - Usa variables de entorno para DB, JWT y API key admin.
   - Depende de `db`.

- **prometheus** (`prom/prometheus:latest`)
   - Expone `9090:9090`.
   - Monta configuración desde `./observability/prometheus.yml`.
   - Depende de `app`.

- **grafana** (`grafana/grafana:latest`)
   - Expone `3000:3000`.
   - Persiste configuración/dashboards en `grafana_data`.
   - Depende de `prometheus`.
   - Password admin por env: `GF_SECURITY_ADMIN_PASSWORD`.

### Levantar el stack

```bash
docker compose up --build
```

### Puertos (por defecto)

- API: `http://localhost:8080/api/v1`
- PostgreSQL: `localhost:5432`
- Prometheus: `http://localhost:9090`
- Grafana: `http://localhost:3000`

### Variables de entorno requeridas (compose)

- **PostgreSQL**
   - `PGUSER`
   - `PGPASSWORD`
   - `PGDATABASE`
   - `PGHOST`
   - `PGPORT`

- **JWT / Seguridad**
   - `JWT_SECRET`
   - `ADMIN_API_KEY`

- **Grafana**
   - `GF_SECURITY_ADMIN_PASSWORD`

---

## Ejecutar en local (DEV)

### Requisitos
- Java 21
- PostgreSQL (o Docker)
- Variables de entorno configuradas

### Opción A: Gradle
```bash
./gradlew bootRun
```

API:
- `http://localhost:8080/api/v1`

> El puerto puede cambiar con `PORT`.

### Opción B: Docker
```bash
docker compose up --build
```

---

## Migraciones (Flyway)

- ubicación: `classpath:database/migration`
- `baseline-on-migrate: true`

Migraciones incluidas:
- `V1__init_schema.sql`
- `V2__add_client_last_stay.sql`
- `V3__add_column_finished_date.sql`

---

## Build / Empaquetado

Genera `app.jar`:
```bash
./gradlew clean build
```

Dockerfile (multi-stage) compila y ejecuta:
- build: `./gradlew clean bootJar -x test`
- run: `java -jar app.jar`

---