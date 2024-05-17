package com.github.malahor.pastemein.domain;

import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ExpiryTime {

  private LocalDateTime value;

  public ExpiryTime(int value, ExpiryUnit unit) {
    var now = LocalDateTime.now();
    this.value =
        switch (unit) {
          case NEVER -> LocalDateTime.MAX;
          case YEARS -> now.plusYears(value);
          case MONTHS -> now.plusMonths(value);
          case WEEKS -> now.plusWeeks(value);
          case DAYS -> now.plusDays(value);
          case MINUTES -> now.plusMinutes(value);
        };
  }
}
