package it.vlig.weather_api_spring.dto.error;

public record ErrorResponse(
  String message,
  int cod
) {
}
