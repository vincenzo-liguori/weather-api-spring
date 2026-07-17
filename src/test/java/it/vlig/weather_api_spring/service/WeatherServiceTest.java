package it.vlig.weather_api_spring.service;

import it.vlig.weather_api_spring.client.WeatherClient;
import it.vlig.weather_api_spring.dto.api.Main;
import it.vlig.weather_api_spring.dto.api.WeatherDetail;
import it.vlig.weather_api_spring.dto.api.WeatherResponse;
import it.vlig.weather_api_spring.enums.UnitEnum;
import it.vlig.weather_api_spring.repository.WeatherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

  private static final String CITY = "New York";

  @Mock
  private WeatherClient weatherClient;
  @Mock
  private WeatherRepository weatherRepository;

  @InjectMocks
  private WeatherService sut;

  @Test
  void shouldGetWeatherByCityIfUnitIsNull() {
    Main mockMain = new Main(25.0, 26.6, 40);
    WeatherDetail mockDetail = new WeatherDetail("sunny", "clear");
    WeatherResponse mockWeather = new WeatherResponse(CITY, 200, mockMain, List.of(mockDetail));

    when(weatherClient.findByCity(CITY)).thenReturn(mockWeather);

    WeatherResponse response = sut.getWeatherByCityAndUnit(CITY, null);

    verify(weatherClient).findByCity(CITY);
    verify(weatherClient, never()).findByCityAndUnit(any(), any());
    assertEquals(mockWeather, response);
  }

  @Test
  void shouldGetWeatherByCityAndUnit() {
    Main mockMain = new Main(25.0, 26.6, 40);
    WeatherDetail mockDetail = new WeatherDetail("sunny", "clear");
    WeatherResponse mockWeather = new WeatherResponse(CITY, 200, mockMain, List.of(mockDetail));

    when(weatherClient.findByCityAndUnit(CITY, UnitEnum.METRIC)).thenReturn(mockWeather);

    WeatherResponse response = sut.getWeatherByCityAndUnit(CITY, UnitEnum.METRIC);

    verify(weatherClient, never()).findByCity(any());
    verify(weatherClient).findByCityAndUnit(CITY, UnitEnum.METRIC);
    assertEquals(mockWeather, response);
  }
}