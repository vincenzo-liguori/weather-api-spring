package it.vlig.weather_api_spring.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherResponse(
  String name,
  int cod,
  Main main,
  List<WeatherDetail> weather
) {
}
