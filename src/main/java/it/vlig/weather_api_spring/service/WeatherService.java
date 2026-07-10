package it.vlig.weather_api_spring.service;

import it.vlig.weather_api_spring.client.WeatherClient;
import it.vlig.weather_api_spring.dto.WeatherResponse;
import it.vlig.weather_api_spring.enums.UnitEnum;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

  private final WeatherClient client;

  public WeatherService(WeatherClient client) {
    this.client = client;
  }

  public WeatherResponse getWeatherByCityAndUnit(String city, UnitEnum unitEnum) {
    return unitEnum == null
      ? client.findByCity(city)
      : client.findByCityAndUnit(city, unitEnum);
  }
}
