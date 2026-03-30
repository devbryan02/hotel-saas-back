## Stack / Tecnologías

- **Java 21**
- **Spring Boot 4.0.2**
- Spring Web MVC
- Spring Security (JWT)
- **PostgreSQL**
- **Flyway** (migraciones SQL)
- MapStruct + Lombok
- Docker + Docker Compose



Paquete base:
- `com.app.hotelsaas.catin`

















**Servidor**
- `PORT` (default `8080`)

- `PGHOST`
- `PGPORT`
- `PGDATABASE`
- `PGUSER`
- `PGPASSWORD`

**JWT**
- `JWT_SECRET` (obligatorio)

**Admin**
- `ADMIN_API_KEY`

**Frontend (CORS)**
- `app.front.url`
  - DEV: `http://localhost:3000`
  - PROD: `https://zowy.vercel.app/`









## Migraciones (Flyway)

- `baseline-on-migrate: true`

Migración inicial:
- `V1__init_schema.sql`

## Build / Empaquetado

```bash
./gradlew clean build
```