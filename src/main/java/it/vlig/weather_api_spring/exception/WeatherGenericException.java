package it.vlig.weather_api_spring.exception;

public class WeatherGenericException extends RuntimeException {

  public WeatherGenericException() {
    super("Weather service temporarily unavailable. Try Later");
  }

}
