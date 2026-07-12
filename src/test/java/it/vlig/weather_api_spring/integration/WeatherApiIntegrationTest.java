package it.vlig.weather_api_spring.integration;

import it.vlig.weather_api_spring.dto.WeatherResponse;
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

  @Tag("integration-real")
  @Test
  void shouldReturnRealWeatherData() {
    String city = "Milan";
    String CITY_PARAM = "?city=";
    String UNIT_PARAM = "&unit=";
    String BASE_ENDPOINT = "/weather-api";
    ResponseEntity<WeatherResponse> response =
      restTemplate.getForEntity(BASE_ENDPOINT+CITY_PARAM+ city +UNIT_PARAM+UnitEnum.METRIC, WeatherResponse.class);

    assertEquals(200, response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertEquals("Milan", response.getBody().name());
  }
}
