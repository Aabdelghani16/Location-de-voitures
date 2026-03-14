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
- PostgreSQL running on localhost:5432 with database `car_rental` (user: postgres)

## Architecture

Spring Boot 4.0.3 MVC application with server-side rendering (Thymeleaf) and Spring Security. Uses Lombok throughout for boilerplate reduction.

**Layered architecture**: Model → Repository → Service → Controller → Thymeleaf views

### Domain Model

Four JPA entities in `org.example.carrental.model`:
- **User** — has `role` field (ADMIN/CLIENT), unique email
- **Car** — has `type` field (SALE/RENT), tracks availability, has both `salePrice` and `rentalPrice`
- **Rental** — links User↔Car with date range, totalPrice, status
- **Purchase** — links User↔Car with purchaseDate, price, status

### Key Packages

- `model/` — JPA entities with Lombok annotations (@Data, @NoArgsConstructor, @AllArgsConstructor)
- `repository/` — Spring Data JPA repositories (UserRepository has custom `findByEmail`)
- `service/` — Business logic (currently only CarService is implemented)
- `controller/` — MVC controllers (stub — not yet implemented)
- `dto/` — Data transfer objects (stub — not yet implemented)
- `config/` — Configuration classes (stub — not yet implemented)

### Current State

The data layer (models + repositories) is complete. Service, controller, DTO, and security configuration layers are scaffolded but mostly unimplemented. Spring Security is on the classpath but has no custom configuration, so default security applies.

## Database

PostgreSQL with Hibernate `ddl-auto=update` (auto-creates/updates tables). SQL logging is enabled (`show-sql=true`).
