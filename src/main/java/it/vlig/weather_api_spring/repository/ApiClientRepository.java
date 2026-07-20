package it.vlig.weather_api_spring.repository;

import it.vlig.weather_api_spring.entity.ApiClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiClientRepository extends JpaRepository<ApiClient, Long> {

  Optional<ApiClient> findByApiKey(String apiKey);
}
