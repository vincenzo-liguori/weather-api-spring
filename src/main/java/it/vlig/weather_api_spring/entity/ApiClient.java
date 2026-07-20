package it.vlig.weather_api_spring.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
public class ApiClient {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  @Column(unique = true)
  private String apiKey;
  @CreationTimestamp
  private Instant createdAt;

  public ApiClient() {
  }

  public ApiClient(String name, String apiKey) {
    this.name = name;
    this.apiKey = apiKey;
  }

  public Long getId() {
    return this.id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getApiKey() {
    return apiKey;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  @Override
  public String toString() {
    return "ApiClient{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", apiKey='" + apiKey + '\'' +
      ", createdAt=" + createdAt +
      '}';
  }
}
