package it.vlig.weather_api_spring.service;

import it.vlig.weather_api_spring.client.WeatherClient;
import it.vlig.weather_api_spring.dto.WeatherQueryResponse;
import it.vlig.weather_api_spring.dto.api.Main;
import it.vlig.weather_api_spring.dto.api.WeatherDetail;
import it.vlig.weather_api_spring.dto.api.WeatherResponse;
import it.vlig.weather_api_spring.entity.WeatherQuery;
import it.vlig.weather_api_spring.enums.UnitEnum;
import it.vlig.weather_api_spring.repository.WeatherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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

    ArgumentCaptor<WeatherQuery> captor = ArgumentCaptor.forClass(WeatherQuery.class);
    verify(weatherRepository).save(captor.capture());

    WeatherQuery saved = captor.getValue();
    assertEquals(CITY, saved.getCity());
    assertEquals(UnitEnum.KELVIN, saved.getUnitEnum());
    assertEquals(25.0, saved.getTemperature());

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
    ArgumentCaptor<WeatherQuery> captor = ArgumentCaptor.forClass(WeatherQuery.class);
    verify(weatherRepository).save(captor.capture());
    WeatherQuery saved = captor.getValue();
    assertEquals(CITY, saved.getCity());
    assertEquals(UnitEnum.METRIC, saved.getUnitEnum());
    assertEquals(25.0, saved.getTemperature());

    verify(weatherClient, never()).findByCity(any());
    verify(weatherClient).findByCityAndUnit(CITY, UnitEnum.METRIC);
    assertEquals(mockWeather, response);
  }

  @Test
  void shouldReturnHistoryByCity() {
    String city = "Salerno";
    WeatherQuery recent = new WeatherQuery(
      city, UnitEnum.METRIC, 21.5, 22.0, 40,
      "Clear");
    WeatherQuery old = new WeatherQuery(
      city, UnitEnum.METRIC, 23.5, 25.0, 65,
      "Clouds");
    List<WeatherQuery> expectedQuery = List.of(recent, old);

    when(weatherRepository.findByCityOrderByQueriedAtDesc(city)).thenReturn(expectedQuery);
    List<WeatherQueryResponse> response = sut.getHistoryByCity(city);

    assertAll("check fields",
      () -> assertEquals(city, response.getFirst().city()),
      () -> assertEquals(UnitEnum.METRIC, response.getFirst().unitEnum()),
      () -> assertEquals(21.5, response.getFirst().temperature()),
      () -> assertEquals(22.0, response.getFirst().feelsLike()),
      () -> assertEquals(40, response.getFirst().humidity()),
      () -> assertEquals("Clear", response.getFirst().description()),
      () -> assertEquals(city, response.getLast().city()),
      () -> assertEquals(UnitEnum.METRIC, response.getLast().unitEnum()),
      () -> assertEquals(23.5, response.getLast().temperature()),
      () -> assertEquals(25.0, response.getLast().feelsLike()),
      () -> assertEquals(65, response.getLast().humidity()),
      () -> assertEquals("Clouds", response.getLast().description()));
  }
}