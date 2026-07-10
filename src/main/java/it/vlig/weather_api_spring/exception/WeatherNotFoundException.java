package it.vlig.weather_api_spring.exception;

public class WeatherNotFoundException extends RuntimeException {
  public WeatherNotFoundException(String city) {
    super("Could not find weather data for city: " + city);
  }
}
