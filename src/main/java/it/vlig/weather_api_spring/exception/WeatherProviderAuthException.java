package it.vlig.weather_api_spring.exception;

public class WeatherProviderAuthException extends RuntimeException {
  public WeatherProviderAuthException() {
    super("Internal Server Error, please contact the support");
  }
}
