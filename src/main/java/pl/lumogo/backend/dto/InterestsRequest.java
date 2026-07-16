package pl.lumogo.backend.dto;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "InterestsRequest", description = "Żądanie zapisu zainteresowań użytkownika")
public class InterestsRequest {
    @Schema(description = "E-mail użytkownika", example = "anna@example.com")
    private String email;
    @Schema(description = "Lista wybranych zainteresowań", example = "[\"Bieganie\", \"Spacer\", \"Trekking\"]")
    private List<String> interests;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
}
