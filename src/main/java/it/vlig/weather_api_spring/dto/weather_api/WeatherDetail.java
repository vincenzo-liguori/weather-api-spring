package it.vlig.weather_api_spring.dto.weather_api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherDetail(
  String main,
  String description
) {
}
