package it.vlig.weather_api_spring.service;

import it.vlig.weather_api_spring.client.WeatherClient;
import it.vlig.weather_api_spring.dto.Main;
import it.vlig.weather_api_spring.dto.WeatherDetail;
import it.vlig.weather_api_spring.dto.WeatherResponse;
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

  public List<WeatherQuery> getHistoryByCity(String city) {
    return repository.findByCityOrderByQueriedAtDesc(city);
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
