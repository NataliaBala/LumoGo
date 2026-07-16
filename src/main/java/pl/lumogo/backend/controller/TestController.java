package pl.lumogo.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Test", description = "Endpoint testowy")
public class TestController {

    @GetMapping("/test")
    @Operation(summary = "Test połączenia", description = "Sprawdza czy backend jest dostępny")
    public ResponseEntity<Map<String, String>> test() {
        return ResponseEntity.ok(Map.of(
            "status", "OK",
            "message", "LumoGo backend is reachable",
            "timestamp", String.valueOf(System.currentTimeMillis())
        ));
    }
}

