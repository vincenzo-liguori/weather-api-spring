package it.vlig.weather_api_spring.dto;

public record ErrorResponse(
  String message,
  int cod
) {
}
