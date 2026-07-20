package it.vlig.weather_api_spring.controller;

import it.vlig.weather_api_spring.dto.api_client.RegisterClientRequest;
import it.vlig.weather_api_spring.dto.api_client.RegisterClientResponse;
import it.vlig.weather_api_spring.service.ApiClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-clients")
public class ApiClientController {

  private final ApiClientService apiClientService;

  public ApiClientController(ApiClientService apiClientService) {
    this.apiClientService = apiClientService;
  }

  @PostMapping()
  public ResponseEntity<RegisterClientResponse> registerClient(@RequestBody RegisterClientRequest registerClientRequest) {
    RegisterClientResponse response = apiClientService.registerClient(registerClientRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
