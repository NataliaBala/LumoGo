package pl.lumogo.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import pl.lumogo.backend.service.WeatherConditionService;
import pl.lumogo.backend.service.WeatherConditionService.WeatherConditionResponse;

@RestController
@RequestMapping("/api")
@Tag(name = "Weather", description = "Warunki pogodowe i współczynnik przydatności do biegania")
public class WeatherConditionController {

    private final WeatherConditionService weatherConditionService;

    public WeatherConditionController(WeatherConditionService weatherConditionService) {
        this.weatherConditionService = weatherConditionService;
    }

    @GetMapping("/conditions/today")
    @Operation(summary = "Warunki pogodowe na dzisiaj", description = "Zwraca temperaturę, prędkość wiatru, wilgotność, AQI oraz ocenę przydatności do biegania (0-100). Domyślnie: Warszawa.")
    @ApiResponse(responseCode = "200", description = "Warunki pogodowe pobrane pomyślnie")
    public ResponseEntity<WeatherConditionResponse> getTodayConditions(
        @RequestParam(required = false) @Parameter(description = "Szerokość geograficzna (domyślnie 52.2297 - Warszawa)") Double lat,
        @RequestParam(required = false) @Parameter(description = "Długość geograficzna (domyślnie 21.0122 - Warszawa)") Double lon
    ) {
        return ResponseEntity.ok(weatherConditionService.getTodayConditions(lat, lon));
    }
}
