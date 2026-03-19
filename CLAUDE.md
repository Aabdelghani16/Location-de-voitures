# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

- **Build**: `./mvnw clean install` (or `mvnw.cmd clean install` on Windows)
- **Run**: `./mvnw spring-boot:run`
- **Run tests**: `./mvnw test`
- **Run single test**: `./mvnw test -Dtest=ClassName#methodName`
- **Skip tests**: `./mvnw install -DskipTests`

## Prerequisites

- Java 17
- PostgreSQL running on localhost:5433 with database `car_rental` (user: postgres, password: chk)

## Architecture

Spring Boot 4.0.3 MVC application with server-side rendering (Thymeleaf) and Spring Security. Uses Lombok throughout for boilerplate reduction.

**Layered architecture**: Model → Repository → Service → Controller → Thymeleaf views

### Domain Model

Four JPA entities in `org.example.carrental.model`:
- **User** — has `role` field (ADMIN/CLIENT), unique email, used as Spring Security principal
- **Car** — has `type` field (SALE/RENT), tracks availability, has both `salePrice` and `rentalPrice`
- **Rental** — links User↔Car with date range, totalPrice, status
- **Purchase** — links User↔Car with purchaseDate, price, status

### Key Packages

- `model/` — JPA entities with Lombok annotations (@Data, @NoArgsConstructor, @AllArgsConstructor)
- `repository/` — Spring Data JPA repositories (UserRepository has custom `findByEmail`, RentalRepository/PurchaseRepository have `findByUser`)
- `service/` — Business logic (currently only CarService with CRUD operations)
- `controller/` — MVC controllers: CarController (`/cars`), AdminController (`/admin`), LoginController (`/login`)
- `dto/` — Data transfer objects (empty — not yet implemented)
- `config/` — SecurityConfig (role-based access, custom login page) and DataInitializer (seeds admin/client users)

### Security

- SecurityConfig uses BCrypt password encoding with email as the username field
- `/admin/**` requires ADMIN role; `/cars/**` requires ADMIN or CLIENT role
- Custom login page at `/login`, default redirect to `/cars` on success
- DataInitializer seeds two users: `ad@min.com`/admin (ADMIN) and `cli@ent.com`/client (CLIENT)

### Application Flow

1. Unauthenticated users are redirected to `/login`
2. After login, users land on `/cars` (car listing)
3. CLIENT users can buy (SALE cars) or rent (RENT cars with date picker), and view orders at `/cars/my-orders`
4. ADMIN users can access `/admin` to add/delete cars

### Current Gaps

- No UserService, RentalService, or PurchaseService — CarController accesses repositories directly for rentals/purchases
- No DTOs — domain entities passed directly to views
- No input validation or global error handling
- Buying/renting does not update `car.available` flag
- Only a context-load test exists; no unit or integration tests

## Database

PostgreSQL with Hibernate `ddl-auto=update` (auto-creates/updates tables). SQL logging is enabled (`show-sql=true`). Thymeleaf cache is disabled for development.
