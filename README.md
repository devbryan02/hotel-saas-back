# hotel-saas-back (catIN)

Sistema de Gestión hotelera **Multi-Tenant** para Ayacucho, Perú.

## Stack / Tecnologías

- **Java 21**
- **Spring Boot 4.0.2**
- Spring Web MVC
- Spring Data JPA
- Spring Security (JWT)
- **PostgreSQL**
- **Flyway** (migraciones SQL)
- MapStruct + Lombok
- Docker + Docker Compose

## Estructura del proyecto

- `src/main/java/com/app/hotelsaas/catin/CatInApplication.java`: punto de entrada de la app (`@SpringBootApplication`) y `@EnableScheduling`.
- `src/main/resources/application.yaml`: perfil activo por defecto.
- `src/main/resources/application-dev.yaml`: configuración de desarrollo.
- `src/main/resources/application-prod.yaml`: configuración de producción.
- `src/main/resources/database/migration/`: scripts Flyway (por ejemplo `V1__init_schema.sql`).

Paquete base:
- `com.app.hotelsaas.catin`

## Ejecución local (DEV)

### Requisitos
- Java 21
- PostgreSQL (o Docker)
- Variables de entorno (ver sección Configuración)

### Opción A: correr con Gradle

```bash
./gradlew bootRun
```

La API se expone bajo el prefijo:
- `http://localhost:8080/api/v1`

> El puerto puede cambiar por `PORT` (por defecto 8080).

### Opción B: Docker

El repo incluye `Dockerfile` y `docker-compose.yml`.

```bash
docker compose up --build
```

> Ajusta las variables necesarias en tu entorno/compose según tu DB y secretos JWT.

## Configuración (application.yml / profiles)

### Perfil activo

En `src/main/resources/application.yaml`:
- `spring.profiles.active: dev`

Puedes cambiarlo con:
- `SPRING_PROFILES_ACTIVE=prod` (o editando el YAML)

### Variables de entorno (DEV/PROD)

En `application-dev.yaml` / `application-prod.yaml` se usan:

**Servidor**
- `PORT` (default `8080`)

**Base de datos (PostgreSQL)**
- `PGHOST`
- `PGPORT`
- `PGDATABASE`
- `PGUSER`
- `PGPASSWORD`

En PROD el JDBC incluye `?sslmode=require`.

**JWT**
- `JWT_SECRET` (obligatorio)
- `jwt.expiration` (configurado a `86400000` ms = 24h)

**Admin**
- `ADMIN_API_KEY`

**Frontend (CORS)**
- `app.front.url`
  - DEV: `http://localhost:3000`
  - PROD: `https://zowy.vercel.app/`

## Base URL / Versionado de API

En ambos perfiles:
- `spring.mvc.servlet.path: `/api/v1``

Por lo tanto:
- Base URL: `http://<host>:<port>/api/v1`

## Autenticación y Seguridad (JWT)

### Flujo general

- El backend usa **Bearer Token** en el header:
  - `Authorization: Bearer <jwt>`

### Claims del token

El token incluye claims:
- `userId`
- `tenantId`
- `role`

y el `subject` es el email.

### Filtro JWT

Se aplica un filtro `JwtAuthFilter` que:
- Lee el header `Authorization`
- Extrae y valida el JWT
- Carga el usuario por email
- Setea el contexto de Spring Security si es válido

### Reglas de autorización (por rutas)

En `SecurityConfig`:
- Rutas públicas:
  - `OPTIONS /**`
  - `/auth/**`
- Rutas protegidas por roles:
  - `/tenants/*/rooms/**` → `ADMIN` o `RECEPTIONIST`
  - `/tenants/*/clients/**` → `ADMIN` o `RECEPTIONIST`
  - `/tenants/*/occupations/**` → `ADMIN` o `RECEPTIONIST`
- Cualquier otra ruta requiere autenticación.

### CORS

- `allowedOrigins` se configura con `app.front.url`
- Métodos permitidos: `GET, POST, PUT, DELETE, PATCH, OPTIONS`
- Headers: `*`
- Expuestos: `Authorization`

## Multi-Tenant (modelo y persistencia)

A nivel de datos (según migración `V1__init_schema.sql`) existe la tabla:
- `tenant` (y el resto de tablas referencian `tenant_id`)

Entidades principales en DB:
- `tenant`
- `client` (con `tenant_id`)
- `room` (con `tenant_id`)
- `occupation` (con `tenant_id`, `client_id`, `room_id`)
- `app_user` (con `tenant_id`, email único)

Esto permite que cada recurso quede aislado por `tenant_id`.

## Migraciones (Flyway)

Flyway está habilitado:
- `spring.flyway.enabled: true`
- `locations: classpath:database/migration`
- `baseline-on-migrate: true`

Migración inicial:
- `V1__init_schema.sql`

## Build / Empaquetado

El `bootJar` genera:
- `app.jar`

Para compilar:

```bash
./gradlew clean build
```