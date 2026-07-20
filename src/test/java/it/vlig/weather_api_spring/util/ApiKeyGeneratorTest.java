package it.vlig.weather_api_spring.util;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ApiKeyGeneratorTest {

  private final ApiKeyGenerator sut = new ApiKeyGenerator();

  @Test
  void shouldGenerateUniqueKeyOfExpectedLength() {
    String firstKey = sut.generateKey();
    String secondKey = sut.generateKey();

    assertEquals(43, firstKey.length());
    assertEquals(43, secondKey.length());
    assertNotEquals(firstKey, secondKey);
  }

}