package it.vlig.weather_api_spring.dto.api_client;

import it.vlig.weather_api_spring.entity.ApiClient;

public record RegisterClientResponse(
  String name,
  String apiKey
) {

  public static RegisterClientResponse fromEntity(ApiClient entity) {
    return new RegisterClientResponse(entity.getName(), entity.getApiKey());
  }
}
