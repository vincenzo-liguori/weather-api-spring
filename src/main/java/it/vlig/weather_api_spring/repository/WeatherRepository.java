package it.vlig.weather_api_spring.repository;

import it.vlig.weather_api_spring.entity.WeatherQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeatherRepository extends JpaRepository<WeatherQuery, Long> {

  List<WeatherQuery> findByCityOrderByQueriedAtDesc(String city);
}
