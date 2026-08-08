package pl.lumogo.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "WidgetStatusResponse", description = "Stan widgeta przypomnień i motywacji")
public class WidgetStatusResponse {

    @Schema(description = "Status odpowiedzi", example = "success")
    private String status;

    @Schema(description = "Generowana wiadomość motywacyjna", example = "Dzisiaj dasz radę! Zrób kolejny krok do celu.")
    private String motivation;

    @Schema(description = "Treść przypomnienia", example = "Nie zapomnij przejrzeć swoich zainteresowań i ustawić celu na dziś.")
    private String notification;

    @Schema(description = "Następne przypomnienie", example = "2026-08-08 18:00")
    private String nextReminder;

    @Schema(description = "Liczba dni z rzędu, którą można potraktować jako streak", example = "5")
    private int streakDays;

    @Schema(description = "Lista propozycji działań dla użytkownika")
    private List<String> suggestions;

    @Schema(description = "Czy widget jest aktywny", example = "true")
    private boolean active;

    @Schema(description = "E-mail użytkownika, jeśli podany", example = "anna@example.com")
    private String email;

    public WidgetStatusResponse() {
    }

    public WidgetStatusResponse(String status, String motivation, String notification, String nextReminder, int streakDays, List<String> suggestions, boolean active, String email) {
        this.status = status;
        this.motivation = motivation;
        this.notification = notification;
        this.nextReminder = nextReminder;
        this.streakDays = streakDays;
        this.suggestions = suggestions;
        this.active = active;
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getNextReminder() {
        return nextReminder;
    }

    public void setNextReminder(String nextReminder) {
        this.nextReminder = nextReminder;
    }

    public int getStreakDays() {
        return streakDays;
    }

    public void setStreakDays(int streakDays) {
        this.streakDays = streakDays;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
