package com.github.malahor.pastemein;

import com.github.malahor.pastemein.domain.ExpiryUnit;
import com.github.malahor.pastemein.validation.ExpiryValid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ExpiryValid
public class InputData {

  @NotEmpty(message = "Text can not be empty")
  private String text;

  private int expiryValue;
  private ExpiryUnit expiryUnit;
  private boolean usePassword;
  private String password = RandomStringUtils.randomAlphanumeric(8);
  private boolean editable;

  public InputData(String text) {
    this.text = text;
  }
}
