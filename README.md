# LumoGo Backend

Backend aplikacji mobilnej LumoGo dla biegaczy i osób aktywnych.

## Stack
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- Spring Validation
- Lombok
- H2 Database (runtime)

## Uruchomienie
1. `./mvnw spring-boot:run` (Linux/macOS) lub `mvnw.cmd spring-boot:run` (Windows)
2. Aplikacja dostępna pod `http://localhost:8080`

> Wymagany jest Java JDK, nie tylko JRE.

## Endpoints
- `GET /api/health` - health check
