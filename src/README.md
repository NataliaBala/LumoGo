# LumoGo Backend

Backend aplikacji LumoGo oparty na Spring Boot. Udostępnia REST API do rejestracji i logowania, profilu użytkownika, zainteresowań, warunków pogodowych oraz stanu widgetu.

## Wymagania

- Java zgodna z wersją skonfigurowaną w projekcie
- PostgreSQL
- Konfiguracja połączenia z bazą przez zmienne środowiskowe

> W aktualnym katalogu projektu nie ma pliku `pom.xml`, `build.gradle` ani `build.gradle.kts`. Przed uruchomieniem lokalnym lub skonfigurowaniem nowego buildu trzeba dodać/odzyskać plik budowania projektu. Render może korzystać z konfiguracji Docker zapisanej poza tym katalogiem/repozytorium.

## Konfiguracja

Aplikacja czyta ustawienia z pliku `main/resources/application.yml`. Ustaw poniższe zmienne w środowisku uruchomieniowym, np. w Render w ustawieniach Web Service → Environment:

| Zmienna | Wymagana | Opis |
| --- | --- | --- |
| `SERVER_PORT` | Nie | Port serwera; domyślnie `8080`. |
| `SERVER_ADDRESS` | Nie | Adres nasłuchiwania; domyślnie `0.0.0.0`. |
| `SPRING_DATASOURCE_URL` | Tak | JDBC URL PostgreSQL, np. `jdbc:postgresql://HOST:5432/NAZWA_BAZY?sslmode=require`. |
| `SPRING_DATASOURCE_DRIVER` | Tak | `org.postgresql.Driver`. |
| `SPRING_DATASOURCE_USERNAME` | Tak | Użytkownik bazy PostgreSQL. |
| `SPRING_DATASOURCE_PASSWORD` | Tak | Hasło bazy PostgreSQL. Trzymaj je wyłącznie w ustawieniach środowiska. |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Nie | Tryb zarządzania schematem; domyślnie `update`. |

Nie zapisuj haseł ani pełnych connection stringów zawierających hasło w repozytorium, README, aplikacji mobilnej ani publicznych logach. Jeśli dane dostępowe zostały ujawnione, zmień hasło w panelu bazy i zaktualizuj zmienną środowiskową.

## Deploy na Render

1. Utwórz bazę PostgreSQL w Render.
2. Utwórz Web Service dla backendu i połącz go z repozytorium zawierającym kompletny projekt oraz konfigurację buildu.
3. W Web Service ustaw zmienne `SPRING_DATASOURCE_*` zgodnie z danymi bazy. Użyj adresu bazy dostępnego z Web Service; dla usług w Render preferuj wewnętrzny adres bazy, jeśli oba serwisy są w tym samym regionie.
4. Nie ustawiaj portu na stałe, jeśli konfiguracja usługi wymaga portu dostarczanego przez platformę. Aplikacja używa `SERVER_PORT`, domyślnie `8080`.
5. Po deployu sprawdź logi oraz endpoint `https://<adres-uslugi>/api/health`.

## Endpointy API

| Metoda | Ścieżka | Opis |
| --- | --- | --- |
| `GET` | `/api/health` | Status aplikacji. |
| `GET` | `/api/test` | Test dostępności backendu. |
| `POST` | `/api/auth/register` | Rejestracja użytkownika. |
| `POST` | `/api/auth/login` | Logowanie użytkownika. |
| `GET` | `/api/auth/profile?email=...` | Pobranie profilu użytkownika. |
| `PUT` | `/api/auth/profile` | Aktualizacja profilu użytkownika. |
| `GET` | `/api/interests/options` | Lista dostępnych zainteresowań. |
| `GET` | `/api/interests/user?email=...` | Zainteresowania użytkownika. |
| `POST` | `/api/interests` | Zapis zainteresowań użytkownika. |
| `GET` | `/api/conditions/today?lat=...&lon=...` | Dzisiejsze warunki pogodowe; domyślnie Warszawa. |
| `GET` | `/api/widget/status?email=...` | Stan widgetu; parametr `email` jest opcjonalny. |

Dokumentacja OpenAPI/Swagger jest udostępniana pod `/v3/api-docs` i `/swagger-ui.html`.

## Test dostępności

Po uruchomieniu backendu lub deployu:

```text
GET https://<adres-uslugi>/api/health
```

Oczekiwana odpowiedź zawiera status `UP` i nazwę usługi `LumoGo Backend`.