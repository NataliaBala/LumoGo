# LumoGo Backend

Backend aplikacji mobilnej LumoGo dla biegaczy i osób aktywnych.

## Wdrożenie na Render

1. Utwórz bazę PostgreSQL w Render. Skopiuj jej wewnętrzne parametry połączenia. Nie używaj darmowej bazy do danych produkcyjnych: ma ograniczenia i może wygasnąć.
2. W Render wybierz **New → Blueprint**, połącz repozytorium `NataliaBala/LumoGo` i wskaż ten plik `render.yaml`.
3. Uzupełnij wymagane wartości `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER` i `DB_PASSWORD` danymi z bazy PostgreSQL. Render wygeneruje `JWT_SECRET`.
4. Zatwierdź Blueprint i poczekaj, aż wdrożenie przejdzie kontrolę zdrowia `/api/health`.
5. Skopiuj publiczny adres usługi HTTPS. Dokumentacja API będzie pod `/v3/api-docs`, a Swagger UI pod `/swagger-ui.html`.
6. Gdy frontend będzie miał docelową domenę, zmień `CORS_ALLOWED_ORIGINS` w ustawieniach usługi na tę domenę (np. `https://app.example.com`) i wykonaj redeploy.

Frontend używa publicznego adresu usługi jako bazowego URL API. Chronione żądania wysyłają `Authorization: Bearer <accessToken>`. Plan usługi `free` może usypiać backend po okresie bezczynności. Baza PostgreSQL jest osobnym zasobem Render i jej plan może wiązać się z opłatą; sprawdź koszt i trwałość planu przed utworzeniem bazy.

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

> Backend jest skonfigurowany do nasłuchiwania na `0.0.0.0`, więc frontend na innym urządzeniu może korzystać z `http://<IP_SERWERA>:8080`.

## Docker i PostgreSQL
1. `docker compose up --build`
2. Backend dostępny pod `http://localhost:8080` (lub inny port jeśli zmienisz zmienną `SERVER_PORT`)
3. Dla dostępu z zewnątrz użyj adresu serwera: `http://<IP_SERWERA>:8080/api/health`
4. Baza PostgreSQL dostępna pod `postgresql://postgres:postgres@localhost:5432/lumogo`

## Ngrok
1. Uruchom backend lokalnie (port 8080) lub w Dockerze (port 8080)
2. Otwórz terminal i wpisz: `ngrok http 8080`
3. Skopiuj wygenerowany adres publiczny, np. `https://xxxxxx.ngrok.io`
4. Frontend może korzystać z tego adresu dla połączeń do API

## Swagger
- UI Swagger: `http://localhost:8080/swagger-ui/index.html`
- API docs: `http://localhost:8080/v3/api-docs`

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
