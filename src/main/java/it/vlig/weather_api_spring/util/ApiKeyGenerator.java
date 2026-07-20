package it.vlig.weather_api_spring.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class ApiKeyGenerator {

  private static final SecureRandom SECURE_RANDOM = new SecureRandom();
  private static final int KEY_LENGTH_BYTES = 32;

  public String generateKey() {
    byte[] randomBytes = new byte[KEY_LENGTH_BYTES];
    SECURE_RANDOM.nextBytes(randomBytes);

    return Base64.getUrlEncoder()
      .withoutPadding()
      .encodeToString(randomBytes);
  }

}
