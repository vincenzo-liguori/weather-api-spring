package it.vlig.weather_api_spring.entity;


import it.vlig.weather_api_spring.enums.UnitEnum;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "weather_history")
public class WeatherQuery {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String city;
  @Column(name = "unit")
  @Enumerated(EnumType.STRING)
  private UnitEnum unitEnum;
  private double temperature;
  private double feelsLike;
  private int humidity;
  private String description;
  @Column(updatable = false)
  @CreationTimestamp
  private Instant queriedAt;

  public WeatherQuery() {
  }

  public WeatherQuery(String city, UnitEnum unitEnum,
                      double temperature, double feelsLike,
                      int humidity, String description) {
    this.city = city;
    this.unitEnum = unitEnum;
    this.temperature = temperature;
    this.feelsLike = feelsLike;
    this.humidity = humidity;
    this.description = description;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public UnitEnum getUnitEnum() {
    return unitEnum;
  }

  public void setUnitEnum(UnitEnum unitEnum) {
    this.unitEnum = unitEnum;
  }

  public double getTemperature() {
    return temperature;
  }

  public void setTemperature(double temperature) {
    this.temperature = temperature;
  }

  public double getFeelsLike() {
    return feelsLike;
  }

  public void setFeelsLike(double feelsLike) {
    this.feelsLike = feelsLike;
  }

  public int getHumidity() {
    return humidity;
  }

  public void setHumidity(int humidity) {
    this.humidity = humidity;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Instant getQueriedAt() {
    return queriedAt;
  }

  @Override
  public String toString() {
    return "WeatherQuery{" +
      "id=" + id +
      ", city='" + city + '\'' +
      ", unitEnum=" + unitEnum +
      ", temperature=" + temperature +
      ", feelsLike=" + feelsLike +
      ", humidity=" + humidity +
      ", description='" + description + '\'' +
      ", queriedAt=" + queriedAt +
      '}';
  }
}
