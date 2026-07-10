package it.vlig.weather_api_spring.controller;

import it.vlig.weather_api_spring.dto.Main;
import it.vlig.weather_api_spring.dto.WeatherDetail;
import it.vlig.weather_api_spring.dto.WeatherResponse;
import it.vlig.weather_api_spring.enums.UnitEnum;
import it.vlig.weather_api_spring.exception.WeatherGenericException;
import it.vlig.weather_api_spring.exception.WeatherNotFoundException;
import it.vlig.weather_api_spring.exception.WeatherProviderAuthException;
import it.vlig.weather_api_spring.exception.WeatherRateLimitException;
import it.vlig.weather_api_spring.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

  private static final String CITY = "New York";
  private static final String WEATHER_ENDPOINT = "/weather-api";

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private WeatherService weatherService;

  @Test
  void shouldReturnWeatherByCityAndUnitSuccess() throws Exception {
    WeatherDetail mockDetail = new WeatherDetail("Clear", "clear");
    Main mockMain = new Main(23.5, 23, 60);
    WeatherResponse mockResponse =
      new WeatherResponse(CITY, 200, mockMain, List.of(mockDetail));

    when(weatherService.getWeatherByCityAndUnit(eq(CITY), any(UnitEnum.class)))
      .thenReturn(mockResponse);

    mockMvc.perform(get(WEATHER_ENDPOINT)
        .queryParam("city", CITY)
        .queryParam("unit", UnitEnum.METRIC.name()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.cod").value(200));
  }

  @Test
  void shouldReturnWeatherByCitySuccess() throws Exception {
    WeatherDetail mockDetail = new WeatherDetail("Clear", "clear");
    Main mockMain = new Main(23.5, 23, 60);
    WeatherResponse mockResponse =
      new WeatherResponse(CITY, 200, mockMain, List.of(mockDetail));

    when(weatherService.getWeatherByCityAndUnit(eq(CITY), isNull()))
      .thenReturn(mockResponse);

    mockMvc.perform(get(WEATHER_ENDPOINT)
        .queryParam("city", CITY))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.cod").value(200));
  }

  @Test
  void shouldThrowNotFoundWhenCityNameDoesNotExists() throws Exception {
    String fakeCity = "test-fake-name";
    String notFoundMessage = "Could not find weather data for city: " + fakeCity;

    when(weatherService.getWeatherByCityAndUnit(any(String.class), eq(UnitEnum.METRIC)))
      .thenThrow(new WeatherNotFoundException(fakeCity));

    mockMvc.perform(get(WEATHER_ENDPOINT)
        .queryParam("city", fakeCity)
        .queryParam("unit", UnitEnum.METRIC.name()))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.cod").value(404))
      .andExpect(jsonPath("$.message").value(notFoundMessage));
  }

  @Test
  void shouldThrowWeatherProviderAuthExceptionAndStatusInternalServerError() throws Exception {
    when(weatherService.getWeatherByCityAndUnit(any(String.class), eq(UnitEnum.METRIC)))
      .thenThrow(new WeatherProviderAuthException());

    mockMvc.perform(get(WEATHER_ENDPOINT)
        .queryParam("city", CITY)
        .queryParam("unit", UnitEnum.METRIC.name()))
      .andExpect(status().isInternalServerError())
      .andExpect(jsonPath("$.cod").value(500))
      .andExpect(jsonPath("$.message").value("Internal Server Error, please contact the support"));
  }

  @Test
  void shouldThrowWeatherRateLimitExceptionAndStatusTooManyrequests() throws Exception {
    when(weatherService.getWeatherByCityAndUnit(any(String.class), eq(UnitEnum.METRIC)))
      .thenThrow(new WeatherRateLimitException());

    mockMvc.perform(get(WEATHER_ENDPOINT)
        .queryParam("city", CITY)
        .queryParam("unit", UnitEnum.METRIC.name()))
      .andExpect(status().isTooManyRequests())
      .andExpect(jsonPath("$.cod").value(429))
      .andExpect(jsonPath("$.message").value("Rate limit calls for the provider exceeded"));
  }

  @Test
  void shouldThrowWeatherGenericExceptionStatusAndStatusSame() throws Exception {
    when(weatherService.getWeatherByCityAndUnit(any(String.class), eq(UnitEnum.METRIC)))
      .thenThrow(new WeatherGenericException());

    mockMvc.perform(get(WEATHER_ENDPOINT)
        .queryParam("city", CITY)
        .queryParam("unit", UnitEnum.METRIC.name()))
      .andExpect(status().isBadGateway())
      .andExpect(jsonPath("$.cod").value(502))
      .andExpect(jsonPath("$.message").value("Weather service temporarily unavailable. Try Later"));
  }

  @Test
  void shouldReturnBadRequestWhenUnitParamIsInvalid() throws Exception {
    mockMvc.perform(get(WEATHER_ENDPOINT)
        .queryParam("city", CITY)
        .queryParam("unit", "fahrenheit"))
      .andExpect(status().isBadRequest());

    verify(weatherService, never()).getWeatherByCityAndUnit(any(), any());
  }

}