package it.vlig.weather_api_spring.service;

import it.vlig.weather_api_spring.dto.api_client.RegisterClientRequest;
import it.vlig.weather_api_spring.dto.api_client.RegisterClientResponse;
import it.vlig.weather_api_spring.entity.ApiClient;
import it.vlig.weather_api_spring.repository.ApiClientRepository;
import it.vlig.weather_api_spring.util.ApiKeyGenerator;
import org.springframework.stereotype.Service;

@Service
public class ApiClientService {

  private final ApiClientRepository apiClientRepository;
  private final ApiKeyGenerator apiKeyGenerator;

  public ApiClientService(ApiClientRepository apiClientRepository, ApiKeyGenerator apiKeyGenerator) {
    this.apiClientRepository = apiClientRepository;
    this.apiKeyGenerator = apiKeyGenerator;
  }

  public RegisterClientResponse registerClient(RegisterClientRequest registerClientRequest) {
    ApiClient saved = apiClientRepository
      .save(
        new ApiClient(
          registerClientRequest.name(),
          apiKeyGenerator.generateKey())
      );
    return RegisterClientResponse.fromEntity(saved);
  }
}
