package it.vlig.weather_api_spring.exception;

public class WeatherRateLimitException extends RuntimeException {
  public WeatherRateLimitException() {
    super("Rate limit calls for the provider exceeded");
  }
}
