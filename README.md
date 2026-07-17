# E-Commerce Platform

A full-stack e-commerce platform built with **Spring Boot 3 / Java 17** (backend) and
**Angular 17 (standalone components)** (frontend), using **JWT** authentication and
**PostgreSQL** (or in-memory **H2** for zero-setup local runs).

This project is based on the architecture described in the accompanying
*Comprehensive Technical Development Guide* (layered architecture, JWT auth,
repository pattern, DTOs) and is filled out into a complete, runnable codebase:
entities, DTOs, repositories, services, controllers, security config, exception
handling, database seeding, and a working Angular UI (login/register, product
catalog, cart, checkout, order history).

```
ecommerce-platform/
├── backend/     Spring Boot REST API (Java 17, Maven)
├── frontend/    Angular 17 SPA (standalone components)
└── docker-compose.yml
```

## Features

- JWT access + refresh token authentication (register/login/refresh)
- Role-based authorization (`CUSTOMER`, `ADMIN`) via Spring Security
- Product catalog with pagination, categories
- Admin-only product & category management endpoints
- Shopping cart (add/update/remove items) tied to the authenticated user
- Checkout → order creation with pessimistic-lock stock reduction
- Order history per user, admin order listing & status updates
- Global exception handling with consistent JSON error responses
- Auto-seeded demo data (admin user, customer user, categories, products)
- Angular: reactive forms, HTTP interceptor for JWT + silent refresh, route guards

## Quick start — Docker (recommended, zero local dependencies)

Requires Docker and Docker Compose.

```bash
cd ecommerce-platform
docker compose up --build
```

- Frontend: http://localhost:4200
- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- Postgres: localhost:5432 (db `ecommerce_db`, user `ecommerce_user`, pass `ecommerce_pass`)

Demo accounts (auto-seeded on first backend startup):

| Role     | Email                     | Password        |
|----------|----------------------------|------------------|
| Admin    | admin@ecommerce.local      | Admin@12345      |
| Customer | customer@ecommerce.local   | Customer@12345   |

## Run without Docker

### Backend

Requires JDK 17+ and Maven 3.9+.

```bash
cd backend
mvn spring-boot:run
```

By default the backend runs with the `h2` profile — an in-memory database that
needs no setup, seeded automatically on startup. API available at
`http://localhost:8080`, H2 console at `http://localhost:8080/h2-console`
(JDBC URL: `jdbc:h2:mem:ecommerce`).

To use PostgreSQL instead, start a local Postgres instance and run:

```bash
SPRING_PROFILES_ACTIVE=postgres \
DB_HOST=localhost DB_NAME=ecommerce_db DB_USERNAME=ecommerce_user DB_PASSWORD=ecommerce_pass \
mvn spring-boot:run
```

Build a runnable jar:

```bash
mvn clean package
java -jar target/ecommerce-platform.jar
```

### Frontend

Requires Node.js 18+ and npm.

```bash
cd frontend
npm install
npm start
```

Runs at `http://localhost:4200` and talks to the backend at
`http://localhost:8080` (see `src/environments/environment.ts`).

## API overview

| Method | Endpoint                          | Auth        | Description                  |
|--------|------------------------------------|-------------|-------------------------------|
| POST   | /api/v1/auth/register              | Public      | Create a customer account     |
| POST   | /api/v1/auth/login                 | Public      | Login, returns JWT pair       |
| POST   | /api/v1/auth/refresh                | Public      | Refresh access token          |
| GET    | /api/v1/products                   | Public      | Paginated product list        |
| GET    | /api/v1/products/{id}              | Public      | Product detail                |
| POST   | /api/v1/products                   | Admin       | Create product                |
| PUT    | /api/v1/products/{id}              | Admin       | Update product                |
| DELETE | /api/v1/products/{id}              | Admin       | Delete product                |
| GET    | /api/v1/categories                 | Public      | List categories               |
| POST   | /api/v1/categories                 | Admin       | Create category                |
| GET    | /api/v1/cart                       | Authenticated | Get current user's cart     |
| POST   | /api/v1/cart/items                 | Authenticated | Add item to cart            |
| PUT    | /api/v1/cart/items/{id}            | Authenticated | Update item quantity        |
| DELETE | /api/v1/cart/items/{id}            | Authenticated | Remove item                 |
| POST   | /api/v1/orders                     | Authenticated | Place order from cart       |
| GET    | /api/v1/orders                     | Authenticated | Current user's orders       |
| GET    | /api/v1/orders/admin/all           | Admin       | All orders                    |
| PATCH  | /api/v1/orders/{id}/status         | Admin       | Update order status           |

Full interactive documentation is available at `/swagger-ui.html` once the
backend is running.

## Architecture notes

- **Layered architecture**: Controller → Service → Repository → Database, with
  DTOs isolating the API contract from JPA entities.
- **Security**: stateless JWT auth via `JwtAuthenticationFilter`, BCrypt password
  hashing, method-level `@PreAuthorize` for admin routes.
- **Concurrency**: stock decrements use a pessimistic write lock
  (`findByIdWithLock`) to prevent overselling under concurrent checkouts.
- **Frontend**: standalone Angular components with lazy-loaded routes, a
  functional HTTP interceptor that attaches the JWT and transparently refreshes
  expired tokens, and functional route guards for authenticated/admin-only pages.

## Known limitations / next steps

- No payment gateway integration (order status is managed manually by admins).
- No automated test suite is included yet — `spring-boot-starter-test` and
  `spring-security-test` are wired up and ready for you to add unit/integration
  tests.
- The Angular admin UI (product/category CRUD forms) is not built out — the
  backend endpoints are ready and can be exercised via Swagger or curl/Postman
  while an admin UI is added.
