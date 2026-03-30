# hotel-saas-back (catIN) — API SaaS Hotelera Multi‑Tenant

Backend para un **sistema de gestión hotelera multi‑tenant** (Ayacucho, Perú).  
Construido con **Spring Boot + Spring Security (JWT) + JPA + Flyway + PostgreSQL**, con una estructura orientada a separar **dominio** y **adaptadores** (persistencia/seguridad).

---

## Qué demuestra este proyecto (para recruiters)

- **Diseño Multi‑Tenant**: el modelo de datos y las entidades están pensadas para aislar información por `tenant_id`.
- **Autenticación Stateless con JWT**:
  - filtro custom `JwtAuthFilter`
  - servicio `JwtService`
  - token con *claims* útiles para autorización multi‑tenant
- **Autorización por roles (RBAC)** con reglas explícitas en `SecurityConfig`.
- **Persistencia con Spring Data JPA** + entidades JPA.
- **Versionado de DB con Flyway** (migraciones SQL).
- **Configuración por perfiles** (`dev` / `prod`) usando variables de entorno (12-factor style).
- **Dockerización** (Dockerfile + docker-compose) y build reproducible (`app.jar`).

---

## Stack / Tecnologías

- **Java 21**
- **Spring Boot 4.0.2**
- Spring Web MVC
- Spring Data JPA (Hibernate)
- Spring Security (JWT)
- **PostgreSQL**
- **Flyway** (migraciones SQL)
- MapStruct + Lombok
- Docker + Docker Compose

---

## Estructura técnica (visión rápida)

Paquete base:
- `com.app.hotelsaas.catin`

Punto de entrada:
- `src/main/java/com/app/hotelsaas/catin/CatInApplication.java` (`@SpringBootApplication`, `@EnableScheduling`)

Separación por capas (alto nivel):
- **Domain**
  - `domain/model`: modelos de negocio (ej. `Tenant`)
  - `domain/port`: contratos/interfaces (ej. `TenantRepository`)
- **Infrastructure**
  - `infrastructure/persistence`: entidades JPA, repositorios, mappers (MapStruct)
  - `infrastructure/security`: configuración de seguridad + JWT

> Esta separación busca evitar acoplar el dominio al framework y hace más mantenible el crecimiento del proyecto.

---

## Multi‑Tenant (cómo está resuelto)

A nivel de base de datos (ver `V1__init_schema.sql`), las tablas principales están relacionadas a `tenant` mediante `tenant_id`:

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

### JWT (token)
El token se firma y contiene:
- `subject`: email del usuario
- *claims*:
  - `userId`
  - `tenantId`
  - `role`

Header esperado:
- `Authorization: Bearer <jwt>`

### Flujo
- `JwtAuthFilter` intercepta requests, valida el token y setea el `SecurityContext`.
- La app opera **stateless** (`SessionCreationPolicy.STATELESS`).

### Reglas por rutas (RBAC)
En `SecurityConfig`:
- Públicas:
  - `/auth/**`
- Protegidas por rol:
  - `/tenants/*/rooms/**` → `ADMIN` o `RECEPTIONIST`
  - `/tenants/*/clients/**` → `ADMIN` o `RECEPTIONIST`
  - `/tenants/*/occupations/**` → `ADMIN` o `RECEPTIONIST`
- Cualquier otra ruta requiere autenticación.

### CORS
- Origen permitido: `app.front.url`
- Métodos: `GET, POST, PUT, DELETE, PATCH, OPTIONS`
- Expuestos: `Authorization`

---

## Base URL / Versionado de API

Se usa prefijo de API:
- `spring.mvc.servlet.path: "/api/v1"`

Base URL:
- `http://<host>:<port>/api/v1`

---

## Configuración (profiles + env vars)

Perfil activo por defecto:
- `src/main/resources/application.yaml` → `spring.profiles.active: dev`

### Variables de entorno

**Servidor**
- `PORT` (default `8080`)

**PostgreSQL**
- `PGHOST`
- `PGPORT`
- `PGDATABASE`
- `PGUSER`
- `PGPASSWORD`

**JWT**
- `JWT_SECRET` (obligatorio)
- `jwt.expiration` = `86400000` ms (24h)

**Admin**
- `ADMIN_API_KEY`

**Frontend (CORS)**
- `app.front.url`
  - DEV: `http://localhost:3000`
  - PROD: `https://zowy.vercel.app/`

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

- habilitado: `spring.flyway.enabled: true`
- ubicación: `classpath:database/migration`
- `baseline-on-migrate: true`

Migración inicial:
- `V1__init_schema.sql`

---

## Build / Empaquetado

Genera `app.jar`:
```bash
./gradlew clean build
```

---

## Próximos pasos (mejoras técnicas planificadas)

- Documentación de endpoints con ejemplos `curl` (Auth, Rooms, Clients, Occupations)
- OpenAPI/Swagger (`springdoc-openapi`)
- Tests de integración con Postgres (Testcontainers)
- Manejo global de errores + respuestas estándar
- Auditoría/logs de acciones administrativas