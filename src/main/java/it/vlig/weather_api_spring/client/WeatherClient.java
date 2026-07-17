package it.vlig.weather_api_spring.client;

import it.vlig.weather_api_spring.dto.api.WeatherResponse;
import it.vlig.weather_api_spring.enums.UnitEnum;
import it.vlig.weather_api_spring.exception.WeatherGenericException;
import it.vlig.weather_api_spring.exception.WeatherNotFoundException;
import it.vlig.weather_api_spring.exception.WeatherProviderAuthException;
import it.vlig.weather_api_spring.exception.WeatherRateLimitException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class WeatherClient {

  private static final String WEATHER_PARAM = "/weather?q=";
  private static final String API_KEY_PARAM = "&appid=";
  private static final String UNIT_PARAM = "&units=";

  private final RestClient client;
  @Value("${openweather.url}")
  private String baseApi;
  private final String apiKey;

  public WeatherClient(@Value("${openweather.api.key}") String apiKey) {
    client = RestClient.create();
    this.apiKey = apiKey;
  }

  public WeatherResponse findByCity(String city) {
    return fetchWeather(buildUriWithCity(city), city);
  }

  public WeatherResponse findByCityAndUnit(String city, UnitEnum unit) {
    return fetchWeather(buildUriWithCityAndUnit(city, unit.getValue()), city);
  }

  private WeatherResponse fetchWeather(URI uri, String city) {
    RestClient.ResponseSpec responseSpec = client.get().uri(uri).retrieve();
    return applyStandardErrorHandling(responseSpec, city).body(WeatherResponse.class);
  }

  private RestClient.ResponseSpec applyStandardErrorHandling(RestClient.ResponseSpec responseSpec, String city) {
    return responseSpec
      .onStatus(status -> status.value() == 404,
        (req, res) -> {throw new WeatherNotFoundException(city);})
      .onStatus(status -> status.value() == 401,
        (req, res) -> {throw new WeatherProviderAuthException();})
      .onStatus(status -> status.value() == 429,
        (req, res) -> {throw new WeatherRateLimitException();})
      .onStatus(status -> status.value() != 200,
        (req, res) -> {throw new WeatherGenericException();});
  }

  private URI buildUriWithCity(String city) {
    String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
    return URI.create(baseApi
      + WEATHER_PARAM + encodedCity
      + API_KEY_PARAM + apiKey);
  }

  private URI buildUriWithCityAndUnit(String city, String unit) {
    String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
    return URI.create(
      baseApi
        + WEATHER_PARAM + encodedCity
        + API_KEY_PARAM + apiKey
        + UNIT_PARAM + unit);
  }
}
