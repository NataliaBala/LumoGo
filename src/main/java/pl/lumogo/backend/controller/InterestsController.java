package pl.lumogo.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import pl.lumogo.backend.dto.AuthResponse;
import pl.lumogo.backend.dto.InterestsRequest;
import pl.lumogo.backend.service.InterestsService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interests")
@Tag(name = "Interests", description = "Zarządzanie zainteresowaniami użytkownika")
public class InterestsController {

    private final InterestsService interestsService;

    public InterestsController(InterestsService interestsService) {
        this.interestsService = interestsService;
    }

    @GetMapping("/options")
    @Operation(summary = "Lista dostępnych zainteresowań", description = "Zwraca listę 10 kategorii sportowych do wyboru")
    @ApiResponse(responseCode = "200", description = "Lista kategorii zainteresowań")
    public ResponseEntity<Object> getOptions() {
        return ResponseEntity.ok(interestsService.getAvailableInterests());
    }

    @GetMapping("/user")
    @Operation(summary = "Pobierz zainteresowania użytkownika", description = "Zwraca zapisane zainteresowania dla danego użytkownika")
    @ApiResponse(responseCode = "200", description = "Zainteresowania pobrane")
    @ApiResponse(responseCode = "400", description = "E-mail jest wymagany")
    public ResponseEntity<?> getUserInterests(@RequestParam @Parameter(description = "E-mail użytkownika") String email) {
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(new AuthResponse("error", "E-mail jest wymagany"));
        }

        return interestsService.getUserInterests(email)
            .map(interests -> {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("email", email);
                response.put("interests", interests);
                return ResponseEntity.ok(response);
            })
            .orElseGet(() -> {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("email", email);
                response.put("interests", List.of());
                return ResponseEntity.ok(response);
            });
    }

    @PostMapping
    @Operation(summary = "Zapisz zainteresowania użytkownika", description = "Po rejestracji użytkownik wybiera interesujące go kategorie sportowe")
    @ApiResponse(responseCode = "200", description = "Zainteresowania zapisane pomyślnie")
    @ApiResponse(responseCode = "400", description = "Błąd w żądaniu")
    public ResponseEntity<AuthResponse> saveInterests(@RequestBody InterestsRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body(new AuthResponse("error", "E-mail jest wymagany"));
        }
        if (request.getInterests() == null || request.getInterests().isEmpty()) {
            return ResponseEntity.badRequest().body(new AuthResponse("error", "Wybierz przynajmniej jedno zainteresowanie"));
        }

        return interestsService.saveUserInterests(request.getEmail(), request.getInterests())
            .map(user -> ResponseEntity.ok(new AuthResponse("success", "Zainteresowania zapisane", user.getEmail(), user.getFirstName(), user.getLastName())))
            .orElseGet(() -> ResponseEntity.badRequest().body(new AuthResponse("error", "Użytkownik nie znaleziony")));
    }
}
