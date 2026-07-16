package pl.lumogo.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AuthResponse", description = "Odpowiedź na operację autentykacji")
public class AuthResponse {
    @Schema(description = "Status odpowiedzi", example = "success", allowableValues = {"success", "error"})
    private String status;
    @Schema(description = "Wiadomość", example = "Zalogowano pomyślnie")
    private String message;
    @Schema(description = "E-mail użytkownika", example = "anna@example.com")
    private String email;
    @Schema(description = "Imię użytkownika", example = "Anna")
    private String firstName;
    @Schema(description = "Nazwisko użytkownika", example = "Kowalska")
    private String lastName;

    public AuthResponse() {
    }

    public AuthResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public AuthResponse(String status, String message, String email, String firstName, String lastName) {
        this.status = status;
        this.message = message;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
