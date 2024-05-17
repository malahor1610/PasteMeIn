package com.github.malahor.pastemein.util;

import com.github.malahor.pastemein.InputData;
import com.github.malahor.pastemein.PasteError;
import com.github.malahor.pastemein.PasteRepository;
import com.github.malahor.pastemein.domain.Paste;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Component
@RequiredArgsConstructor
public class RedirectHandler {

  private final PasteRepository repository;

  public RedirectView validationError(
          RedirectAttributes attributes, InputData input, BindingResult result) {
    attributes.addFlashAttribute("input", input);
    PasteError.error(attributes, result.getAllErrors().get(0).getDefaultMessage());
    return new RedirectView("/start");
  }

  public RedirectView notFound(RedirectAttributes attributes) {
    PasteError.error(attributes, PasteError.NOT_FOUND);
    return new RedirectView("/start");
  }

  public RedirectView expired(RedirectAttributes attributes, Paste paste) {
    PasteError.error(attributes, PasteError.EXPIRED);
    repository.deleteById(paste.getId());
    return new RedirectView("/start");
  }

  public RedirectView passwordProtected(RedirectAttributes attributes, String token) {
    attributes.addAttribute("token", token);
    return new RedirectView("/protect");
  }

  public RedirectView incorrectPassword(RedirectAttributes attributes, String token) {
    PasteError.error(attributes, PasteError.INCORRECT_PASSWORD);
    attributes.addAttribute("token", token);
    return new RedirectView("/protect");
  }

  public RedirectView nonEditable(RedirectAttributes attributes) {
    PasteError.error(attributes, PasteError.NON_EDITABLE);
    return new RedirectView("/paste");
  }
}
