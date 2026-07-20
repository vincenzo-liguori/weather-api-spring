package it.vlig.weather_api_spring.util;


import it.vlig.weather_api_spring.controller.WeatherController;
import it.vlig.weather_api_spring.entity.ApiClient;
import it.vlig.weather_api_spring.repository.ApiClientRepository;
import it.vlig.weather_api_spring.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
class ApiKeyAuthFilterTest {

  private static final String WEATHER_ENDPOINT = "/weather-api";
  private static final String VALID_KEY = "valid-test-key";

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private WeatherService weatherService;

  @MockitoBean
  private ApiClientRepository apiClientRepository;

  @Test
  void shouldReturnUnauthorizedWhenApiKeyHeaderIsMissing() throws Exception {
    mockMvc.perform(
        get(WEATHER_ENDPOINT)
          .queryParam("city", "New York"))
      .andExpect(status().isUnauthorized())
      .andExpect(jsonPath("$.cod").value(401))
      .andExpect(jsonPath("$.message").value("Missing API Key"));
  }

  @Test
  void shouldReturnUnauthorizedWhenApiKeyIsInvalid() throws Exception {
    when(apiClientRepository.findByApiKey("Wrong-key")).thenReturn(Optional.empty());

    mockMvc.perform(
        get(WEATHER_ENDPOINT)
          .queryParam("city", "New York")
          .header("X-API-KEY", "Wrong-key"))
      .andExpect(status().isUnauthorized())
      .andExpect(jsonPath("$.cod").value(401))
      .andExpect(jsonPath("$.message").value("Invalid API Key"));
  }

  @Test
  void shouldProceedWhenApiKeyIsValid() throws Exception {
    when(apiClientRepository.findByApiKey(VALID_KEY))
      .thenReturn(
        Optional.of(new ApiClient("test-client", VALID_KEY))
      );

    mockMvc.perform(
        get(WEATHER_ENDPOINT)
          .queryParam("city", "New York")
          .header("X-API-KEY", VALID_KEY))
      .andExpect(status().isOk());
  }
}