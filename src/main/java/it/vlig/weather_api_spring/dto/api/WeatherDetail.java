package it.vlig.weather_api_spring.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherDetail(
  String main,
  String description
) {
}
