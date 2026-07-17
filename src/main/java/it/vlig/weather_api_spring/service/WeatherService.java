package it.vlig.weather_api_spring.service;

import it.vlig.weather_api_spring.client.WeatherClient;
import it.vlig.weather_api_spring.dto.WeatherQueryResponse;
import it.vlig.weather_api_spring.dto.api.Main;
import it.vlig.weather_api_spring.dto.api.WeatherDetail;
import it.vlig.weather_api_spring.dto.api.WeatherResponse;
import it.vlig.weather_api_spring.entity.WeatherQuery;
import it.vlig.weather_api_spring.enums.UnitEnum;
import it.vlig.weather_api_spring.repository.WeatherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeatherService {

  private final WeatherClient client;

  private final WeatherRepository repository;

  public WeatherService(WeatherClient client, WeatherRepository repository) {
    this.client = client;
    this.repository = repository;
  }

  public WeatherResponse getWeatherByCityAndUnit(String city, UnitEnum unitEnum) {
    WeatherResponse response = unitEnum == null
      ? client.findByCity(city)
      : client.findByCityAndUnit(city, unitEnum);

    UnitEnum unitToSave = unitEnum == null ? UnitEnum.KELVIN : unitEnum;
    saveHistory(response, unitToSave);
    return response;
  }

  public List<WeatherQueryResponse> getHistoryByCity(String city) {
    return repository.findByCityOrderByQueriedAtDesc(city)
      .stream()
      .map(WeatherQueryResponse::fromEntity)
      .toList();
  }

  private void saveHistory(WeatherResponse response, UnitEnum unit) {
    Main main = response.main();
    WeatherDetail detail = response.weather().getFirst();
    WeatherQuery weatherQuery = new WeatherQuery(
      response.name(), unit,
      main.temp(), main.feelsLike(),
      main.humidity(), detail.description());
    repository.save(weatherQuery);
  }
}
