package it.vlig.weather_api_spring.controller;

import it.vlig.weather_api_spring.dto.WeatherQueryResponse;
import it.vlig.weather_api_spring.dto.weather_api.WeatherResponse;
import it.vlig.weather_api_spring.enums.UnitEnum;
import it.vlig.weather_api_spring.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/weather-api")
public class WeatherController {

  private final WeatherService service;

  public WeatherController(WeatherService service) {
    this.service = service;
  }

  @GetMapping()
  public ResponseEntity<WeatherResponse> getWeatherByCityAndUnit(
    @RequestParam String city,
    @RequestParam(required = false) UnitEnum unit) {

    WeatherResponse response = service.getWeatherByCityAndUnit(city, unit);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/history")
  public ResponseEntity<List<WeatherQueryResponse>> getHistoryByCity(
    @RequestParam String city) {
    return ResponseEntity.ok(service.getHistoryByCity(city));
  }
}
