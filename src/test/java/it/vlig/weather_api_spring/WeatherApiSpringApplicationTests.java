package it.vlig.weather_api_spring;

import it.vlig.weather_api_spring.client.WeatherClient;
import it.vlig.weather_api_spring.dto.api.Main;
import it.vlig.weather_api_spring.dto.api.WeatherDetail;
import it.vlig.weather_api_spring.dto.api.WeatherResponse;
import it.vlig.weather_api_spring.entity.WeatherQuery;
import it.vlig.weather_api_spring.enums.UnitEnum;
import it.vlig.weather_api_spring.repository.WeatherRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WeatherApiSpringApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@MockitoBean
	private WeatherClient weatherClient;
	@MockitoBean
	private WeatherRepository weatherRepository;

	@Test
	void shouldReturnWeatherEndToEndWithMockedClient() {
		WeatherResponse mockResponse = new WeatherResponse(
			"New York", 200,
			new Main(25.5, 26.0, 40),
			List.of(new WeatherDetail("clear", "clear")));

		when(weatherClient.findByCityAndUnit("New York", UnitEnum.METRIC))
			.thenReturn(mockResponse);

		ResponseEntity<WeatherResponse> response =
			restTemplate.getForEntity("/weather-api?city=New York&unit=METRIC", WeatherResponse.class);

		ArgumentCaptor<WeatherQuery> captor = ArgumentCaptor.forClass(WeatherQuery.class);

		verify(weatherRepository).save(captor.capture());

		WeatherQuery saved = captor.getValue();
		assertEquals("New York", saved.getCity());
		assertEquals(UnitEnum.METRIC, saved.getUnitEnum());
		assertEquals(25.5, saved.getTemperature());

		assertEquals(200, response.getStatusCode().value());
		assertEquals(mockResponse, response.getBody());
	}

}
