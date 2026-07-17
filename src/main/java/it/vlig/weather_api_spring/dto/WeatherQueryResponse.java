package it.vlig.weather_api_spring.dto;

import it.vlig.weather_api_spring.entity.WeatherQuery;
import it.vlig.weather_api_spring.enums.UnitEnum;

import java.time.Instant;

public record WeatherQueryResponse(
  String city,
  String description,
  UnitEnum unitEnum,
  double temperature,
  double feelsLike,
  int humidity,
  Instant queriedAt
) {

  public static WeatherQueryResponse fromEntity(WeatherQuery entity) {
    return new WeatherQueryResponse(
      entity.getCity(), entity.getDescription(),
      entity.getUnitEnum(), entity.getTemperature(),
      entity.getFeelsLike(), entity.getHumidity(),
      entity.getQueriedAt()
    );
  }
}
