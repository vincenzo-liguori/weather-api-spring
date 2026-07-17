package it.vlig.weather_api_spring.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UnitEnum {
  METRIC("metric"),
  IMPERIAL("imperial"),
  KELVIN("kelvin");

  private final String value;

  UnitEnum(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return this.value;
  }

  @JsonCreator
  public static UnitEnum fromValue(String value) {
    for (UnitEnum unitEnum: UnitEnum.values()) {
      if (unitEnum.value.equalsIgnoreCase(value) || unitEnum.name().equalsIgnoreCase(value)) {
        return unitEnum;
      }
    }
    throw new IllegalArgumentException("Unknown unit value: " + value);
  }
}
