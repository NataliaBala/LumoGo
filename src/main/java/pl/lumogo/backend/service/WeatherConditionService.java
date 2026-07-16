package pl.lumogo.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Iterator;

@Service
public class WeatherConditionService {

    private static final double DEFAULT_LATITUDE = 52.2297;
    private static final double DEFAULT_LONGITUDE = 21.0122;
    private static final String WEATHER_URL = "https://api.open-meteo.com/v1/forecast";
    private static final String AIR_QUALITY_URL = "https://air-quality-api.open-meteo.com/v1/air-quality";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WeatherConditionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherConditionResponse getTodayConditions(Double latitude, Double longitude) {
        double lat = latitude != null ? latitude : DEFAULT_LATITUDE;
        double lon = longitude != null ? longitude : DEFAULT_LONGITUDE;

        JsonNode weatherNode = fetchJson(buildWeatherUrl(lat, lon));
        JsonNode airNode = fetchJson(buildAirQualityUrl(lat, lon));

        JsonNode currentWeather = weatherNode.path("current_weather");
        if (currentWeather.isMissingNode() || currentWeather.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Unable to retrieve current weather data");
        }

        double temperature = currentWeather.path("temperature").asDouble(Double.NaN);
        double windSpeed = currentWeather.path("windspeed").asDouble(Double.NaN);
        String currentTime = currentWeather.path("time").asText();

        double humidity = findHourlyValue(weatherNode.path("hourly").path("time"), weatherNode.path("hourly").path("relativehumidity_2m"), currentTime, 0.0);
        int aqi = (int) Math.round(findHourlyValue(airNode.path("hourly").path("time"), airNode.path("hourly").path("us_aqi"), currentTime, 0.0));

        LocalDate date = parseDateOnly(currentTime);

        int temperatureScore = calculateTemperatureScore(temperature, date.getMonth());
        int humidityScore = calculateHumidityScore(humidity);
        int windScore = calculateWindScore(windSpeed);
        int aqiScore = calculateAqiScore(aqi);

        int totalScore = calculateTotalScore(temperatureScore, humidityScore, windScore, aqiScore);

        return new WeatherConditionResponse(
            date.toString(),
            round(temperature, 1),
            round(windSpeed, 1),
            round(humidity, 1),
            aqi,
            totalScore,
            scoreCategory(totalScore),
            lat,
            lon
        );
    }

    private JsonNode fetchJson(String url) {
        try {
            String response = restTemplate.getForObject(url, String.class);
            return objectMapper.readTree(response);
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Failed to parse external weather API response", ex);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "External weather API unavailable", ex);
        }
    }

    private String buildWeatherUrl(double latitude, double longitude) {
        return WEATHER_URL + "?latitude=" + latitude + "&longitude=" + longitude + "&current_weather=true&hourly=relativehumidity_2m&timezone=auto";
    }

    private String buildAirQualityUrl(double latitude, double longitude) {
        return AIR_QUALITY_URL + "?latitude=" + latitude + "&longitude=" + longitude + "&hourly=us_aqi&timezone=auto";
    }

    private double findHourlyValue(JsonNode timeNodes, JsonNode valueNodes, String targetTime, double defaultValue) {
        if (!timeNodes.isArray() || !valueNodes.isArray() || timeNodes.size() != valueNodes.size()) {
            return defaultValue;
        }

        Iterator<JsonNode> timeIterator = timeNodes.elements();
        Iterator<JsonNode> valueIterator = valueNodes.elements();
        while (timeIterator.hasNext() && valueIterator.hasNext()) {
            String time = timeIterator.next().asText();
            double value = valueIterator.next().asDouble(defaultValue);
            if (time.equals(targetTime)) {
                return value;
            }
        }

        return valueNodes.get(0).asDouble(defaultValue);
    }

    private LocalDate parseDateOnly(String time) {
        try {
            return LocalDate.parse(time.substring(0, 10));
        } catch (Exception ex) {
            return LocalDate.now();
        }
    }

    private int calculateTemperatureScore(double temperature, Month month) {
        double low;
        double high;
        switch (month) {
            case DECEMBER, JANUARY, FEBRUARY -> {
                low = -5; high = 5;
            }
            case MARCH, APRIL, MAY -> {
                low = 8; high = 18;
            }
            case JUNE, JULY, AUGUST -> {
                low = 16; high = 26;
            }
            default -> {
                low = 8; high = 18;
            }
        }

        if (temperature >= low && temperature <= high) {
            return 100;
        }

        double distance = temperature < low ? low - temperature : temperature - high;
        int score = (int) Math.max(0, 100 - distance * 5);
        return Math.min(score, 100);
    }

    private int calculateHumidityScore(double humidity) {
        double diff = Math.abs(humidity - 45.0);
        int score = (int) Math.max(0, 100 - diff * 1.5);
        return Math.min(score, 100);
    }

    private int calculateWindScore(double windSpeed) {
        if (windSpeed <= 10) {
            return 100;
        }
        if (windSpeed <= 20) {
            return 80;
        }
        if (windSpeed <= 30) {
            return 60;
        }
        return 40;
    }

    private int calculateAqiScore(int aqi) {
        if (aqi <= 50) {
            return 100;
        }
        if (aqi <= 100) {
            return 80;
        }
        if (aqi <= 150) {
            return 60;
        }
        if (aqi <= 200) {
            return 40;
        }
        if (aqi <= 300) {
            return 20;
        }
        return 0;
    }

    private int calculateTotalScore(int temperatureScore, int humidityScore, int windScore, int aqiScore) {
        double combined = temperatureScore * 0.3 + humidityScore * 0.2 + windScore * 0.1 + aqiScore * 0.4;
        return (int) Math.round(Math.max(0, Math.min(100, combined)));
    }

    private String scoreCategory(int score) {
        if (score >= 85) {
            return "Bardzo dobre";
        }
        if (score >= 70) {
            return "Dobre";
        }
        if (score >= 50) {
            return "Umiarkowane";
        }
        if (score >= 30) {
            return "Słabe";
        }
        return "Bardzo słabe";
    }

    private double round(double value, int decimals) {
        double factor = Math.pow(10, decimals);
        return Math.round(value * factor) / factor;
    }

    public static class WeatherConditionResponse {
        public final String date;
        public final double temperatureC;
        public final double windSpeedKmh;
        public final double humidity;
        public final int aqi;
        public final int score;
        public final String scoreCategory;
        public final double latitude;
        public final double longitude;

        public WeatherConditionResponse(String date, double temperatureC, double windSpeedKmh, double humidity, int aqi, int score, String scoreCategory, double latitude, double longitude) {
            this.date = date;
            this.temperatureC = temperatureC;
            this.windSpeedKmh = windSpeedKmh;
            this.humidity = humidity;
            this.aqi = aqi;
            this.score = score;
            this.scoreCategory = scoreCategory;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}
