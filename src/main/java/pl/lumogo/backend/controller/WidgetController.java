package pl.lumogo.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import pl.lumogo.backend.dto.WidgetStatusResponse;
import pl.lumogo.backend.service.WidgetService;

@RestController
@RequestMapping("/api/widget")
@Tag(name = "Widget", description = "Widget powiadomień, przypomnień i motywacji")
public class WidgetController {

    private final WidgetService widgetService;

    public WidgetController(WidgetService widgetService) {
        this.widgetService = widgetService;
    }

    @GetMapping("/status")
    @Operation(summary = "Pobierz stan widgeta", description = "Zwraca przypomnienia, motywację oraz stan widgeta do wyświetlenia w aplikacji.")
    @ApiResponse(responseCode = "200", description = "Stan widgeta pobrany pomyślnie")
    public ResponseEntity<WidgetStatusResponse> getStatus(@RequestParam(required = false) String email) {
        WidgetStatusResponse response = widgetService.getWidgetStatus(email);
        return ResponseEntity.ok(response);
    }
}
