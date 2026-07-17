package it.vlig.weather_api_spring.integration;

import it.vlig.weather_api_spring.dto.api.WeatherResponse;
import it.vlig.weather_api_spring.enums.UnitEnum;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherApiIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  private static final String CITY = "Milan";
  private static final String CITY_PARAM = "?city=";
  private static final String UNIT_PARAM = "&unit=";
  private static final String BASE_ENDPOINT = "/weather-api";

  @Tag("integration-real")
  @Test
  void shouldReturnRealWeatherData() {
    ResponseEntity<WeatherResponse> response =
      restTemplate.getForEntity(BASE_ENDPOINT+CITY_PARAM+ CITY +UNIT_PARAM+UnitEnum.METRIC, WeatherResponse.class);

    assertEquals(200, response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertEquals(CITY, response.getBody().name());
  }

  @Tag("integration-real")
  @Test
  void shouldThrowWeatherNotFoundException() {
    ResponseEntity<WeatherResponse> response =
      restTemplate.getForEntity(BASE_ENDPOINT+CITY_PARAM+ "fakeName", WeatherResponse.class);

    assertEquals(404, response.getStatusCode().value());
    assertNotNull(response.getBody());
  }
}
