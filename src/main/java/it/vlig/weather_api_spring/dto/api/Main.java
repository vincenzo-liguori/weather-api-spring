package it.vlig.weather_api_spring.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Main(
  double temp,
  @JsonProperty("feels_like")
  double feelsLike,
  int humidity
) {
}
