package com.github.malahor.pastemein;

import lombok.Getter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Getter
public enum PasteError {
  NOT_FOUND("No content attached to given link"),
  INCORRECT_PASSWORD("Given password is incorrect"),
  NON_EDITABLE("Content is not editable"),
  EXPIRED("Content already expired");

  private final String description;

  PasteError(String description) {
    this.description = description;
  }

  public static void error(RedirectAttributes attributes, PasteError error) {
    error(attributes, error.description);
  }

  public static void error(RedirectAttributes attributes, String error) {
    attributes.addFlashAttribute("error", error);
  }
}
