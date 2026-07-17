package it.vlig.weather_api_spring.exception;

import it.vlig.weather_api_spring.dto.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(WeatherNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(WeatherNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
      .body(new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()));
  }

  @ExceptionHandler(WeatherProviderAuthException.class)
  public ResponseEntity<ErrorResponse> handleProviderAuth(WeatherProviderAuthException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
  }

  @ExceptionHandler(WeatherRateLimitException.class)
  public ResponseEntity<ErrorResponse> handleRateLimit(WeatherRateLimitException ex) {
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
      .body(new ErrorResponse(ex.getMessage(), HttpStatus.TOO_MANY_REQUESTS.value()));
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    String message = "Invalid value '" + ex.getValue() + "' for parameter '" + ex.getName() + "'";
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(new ErrorResponse(message, HttpStatus.BAD_REQUEST.value()));
  }

  @ExceptionHandler(WeatherGenericException.class)
  public ResponseEntity<ErrorResponse> handleGeneric(WeatherGenericException ex) {
    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
      .body(new ErrorResponse(ex.getMessage(), HttpStatus.BAD_GATEWAY.value()));
  }
}
