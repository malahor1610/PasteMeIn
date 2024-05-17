package com.github.malahor.pastemein.domain;

import lombok.Getter;

@Getter
public enum ExpiryUnit {
  NEVER("Never"),
  YEARS("Years"),
  MONTHS("Months"),
  WEEKS("Weeks"),
  DAYS("Days"),
  MINUTES("Minutes");

  private final String description;

  ExpiryUnit(String description) {
    this.description = description;
  }
}
