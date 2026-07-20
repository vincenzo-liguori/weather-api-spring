package it.vlig.weather_api_spring.integration;

import it.vlig.weather_api_spring.dto.api_client.RegisterClientRequest;
import it.vlig.weather_api_spring.dto.api_client.RegisterClientResponse;
import it.vlig.weather_api_spring.dto.weather_api.WeatherResponse;
import it.vlig.weather_api_spring.enums.UnitEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("integration-real")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherApiIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  private String apiKey;

  private static final String CITY = "Milan";
  private static final String CITY_PARAM = "?city=";
  private static final String UNIT_PARAM = "&unit=";
  private static final String BASE_ENDPOINT = "/weather-api";

  @BeforeEach
  void registerTestClient() {
    RegisterClientRequest request = new RegisterClientRequest("test-client");

    ResponseEntity<RegisterClientResponse> response =
      restTemplate.postForEntity("/api-clients", request, RegisterClientResponse.class);

    assertNotNull(response.getBody());
    apiKey = response.getBody().apiKey();
  }

  @Test
  void shouldReturnRealWeatherData() {
    HttpEntity<Void> requestEntity = getHttpEntity();

    ResponseEntity<WeatherResponse> response =
      restTemplate.exchange(buildRequestURI(CITY),
        HttpMethod.GET,
        requestEntity,
        WeatherResponse.class
      );

    assertEquals(200, response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertEquals(CITY, response.getBody().name());
  }

  @Test
  void shouldReturnNotFoundForUnknownCity() {
    HttpEntity<Void> httpEntity = getHttpEntity();

    ResponseEntity<WeatherResponse> response =
      restTemplate.exchange(buildRequestURI("fakeName"),
        HttpMethod.GET,
        httpEntity,
        WeatherResponse.class
      );

    assertEquals(404, response.getStatusCode().value());
    assertNotNull(response.getBody());
  }

  private HttpEntity<Void> getHttpEntity() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-API-KEY", apiKey);

    return new HttpEntity<>(headers);
  }

  private static String buildRequestURI(String city) {
    return BASE_ENDPOINT + CITY_PARAM + city + UNIT_PARAM + UnitEnum.METRIC;
  }

}
