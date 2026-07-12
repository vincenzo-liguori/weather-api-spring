package it.vlig.weather_api_spring;

import it.vlig.weather_api_spring.client.WeatherClient;
import it.vlig.weather_api_spring.dto.Main;
import it.vlig.weather_api_spring.dto.WeatherDetail;
import it.vlig.weather_api_spring.dto.WeatherResponse;
import it.vlig.weather_api_spring.enums.UnitEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WeatherApiSpringApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@MockitoBean
	private WeatherClient weatherClient;

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

		assertEquals(200, response.getStatusCode().value());
		assertEquals(mockResponse, response.getBody());
	}

}
