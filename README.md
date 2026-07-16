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
### Windows
1. Otwórz terminal w katalogu projektu
2. Uruchom: `run.bat`

### PowerShell
1. Otwórz terminal w katalogu projektu
2. Uruchom: `powershell -ExecutionPolicy Bypass -File .\run.ps1`

Jeśli `mvnw.cmd` w PowerShell nie działa, użyj `run.bat`.

Aplikacja będzie dostępna pod `http://localhost:8080` (oraz na adresie IP serwera, jeśli jest wystawiona z zewnątrz)

Jeśli chcesz, aby backend był dostępny z zewnątrz, upewnij się, że port 8080 jest otwarty w firewallu i serwer nasłuchuje na `0.0.0.0`.

> Wymagany jest Java JDK, nie tylko JRE.

## Docker i PostgreSQL
1. `docker compose up --build`
2. Backend dostępny pod `http://localhost:8080` (lub inny port jeśli zmienisz zmienną `SERVER_PORT`)
3. Dla dostępu z zewnątrz użyj adresu serwera: `http://<IP_SERWERA>:8080/api/health`
4. Baza PostgreSQL dostępna pod `postgresql://postgres:postgres@localhost:5432/lumogo`

## Ngrok
1. Uruchom backend lokalnie (port 8081) lub w Dockerze (port 8080)
2. Otwórz terminal i wpisz: `ngrok http 8081` (dla development) lub `ngrok http 8080` (dla Docker)
3. Skopiuj wygenerowany adres publiczny, np. `https://xxxxxx.ngrok.io`
4. Frontend może korzystać z tego adresu dla połączeń do API

## Swagger
- UI Swagger: `http://localhost:8081/swagger-ui/index.html`
- API docs: `http://localhost:8081/v3/api-docs`

## Endpoints
- `GET /api/health` - health check
- `GET /api/test` - example test endpoint for frontend integration
- `GET /api/conditions/today?lat={latitude}&lon={longitude}` - today’s weather + air quality summary
- `POST /api/auth/register` - rejestracja nowego użytkownika
  - payload: `{ "firstName": "Anna", "lastName": "Kowalska", "email": "anna@example.com", "password": "Abc12", "passwordConfirm": "Abc12" }`
  - hasło musi mieć co najmniej 5 znaków, zawierać co najmniej jedną cyfrę, jedną dużą literę i jedną małą literę
- `POST /api/auth/login` - logowanie użytkownika
- `GET /api/interests/options` - lista dostępnych zainteresowań do wyboru
- `GET /api/interests/user?email=anna@example.com` - pobierz zapisane zainteresowania użytkownika
- `POST /api/interests` - zapisanie wybranych zainteresowań po rejestracji
  - payload: `{ "email": "anna@example.com", "interests": ["Bieganie", "Spacer", "Trekking"] }`
  - payload: `{ "email": "anna@example.com", "password": "abc12" }`
