package com.github.malahor.pastemein;

import com.github.malahor.pastemein.domain.ExpiryUnit;
import com.github.malahor.pastemein.domain.Paste;
import com.github.malahor.pastemein.util.AuthorizationStorage;
import com.github.malahor.pastemein.util.RedirectHandler;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PasteController {

  private final PasteRepository repository;
  private final AuthorizationStorage authorizationStorage;
  private final RedirectHandler redirect;

  @GetMapping
  public RedirectView home() {
    return new RedirectView("/start");
  }

  @GetMapping("/start")
  public ModelAndView start(
      ModelMap model,
      @ModelAttribute("input") InputData input,
      @ModelAttribute("link") String link,
      @ModelAttribute("error") String error) {
    model.addAttribute("input", input);
    model.addAttribute("units", ExpiryUnit.values());
    model.addAttribute("link", link);
    model.addAttribute("error", error);
    return new ModelAndView("start");
  }

  @PostMapping("/paste")
  public RedirectView paste(
      RedirectAttributes attributes,
      @Valid @ModelAttribute("input") InputData input,
      BindingResult result) {
    if (result.hasErrors()) return redirect.validationError(attributes, input, result);
    var paste = repository.save(new Paste(input));
    attributes.addFlashAttribute("link", buildLink(paste));
    return new RedirectView("/start");
  }

  @GetMapping("/paste")
  public ModelAndView link(
      ModelMap model,
      RedirectAttributes attributes,
      @RequestParam("token") String token,
      @ModelAttribute("error") String error) {
    var optionalPaste = repository.findById(UUID.fromString(token));
    if (optionalPaste.isEmpty()) return new ModelAndView(redirect.notFound(attributes));
    var paste = optionalPaste.get();
    if (paste.expired()) return new ModelAndView(redirect.expired(attributes, paste));
    if (unauthorized(paste)) return new ModelAndView(redirect.passwordProtected(attributes, token));
    model.addAttribute("paste", paste);
    model.addAttribute("input", new InputData(paste.getText()));
    model.addAttribute("token", token);
    model.addAttribute("error", error);
    return new ModelAndView("show");
  }

  @PutMapping("/paste")
  public RedirectView update(
      RedirectAttributes attributes,
      @RequestParam("token") String token,
      @ModelAttribute("input") InputData input) {
    var optionalPaste = repository.findById(UUID.fromString(token));
    if (optionalPaste.isEmpty()) return redirect.notFound(attributes);
    var paste = optionalPaste.get();
    attributes.addAttribute("token", token);
    if (!paste.isEditable()) return redirect.nonEditable(attributes);
    paste.update(input);
    repository.save(paste);
    return new RedirectView("/paste");
  }

  @DeleteMapping("/paste")
  public RedirectView delete(@RequestParam("token") String token) {
    repository.deleteById(UUID.fromString(token));
    return new RedirectView("/start");
  }

  @GetMapping("/protect")
  public ModelAndView protect(
      ModelMap model, @RequestParam("token") String token, @ModelAttribute("error") String error) {
    model.addAttribute("password", "");
    model.addAttribute("token", token);
    model.addAttribute("error", error);
    return new ModelAndView("protect");
  }

  @PostMapping("/authorize")
  public RedirectView authorize(
      RedirectAttributes attributes,
      @RequestParam("token") String token,
      @RequestParam("password") String password) {
    var paste = repository.findById(UUID.fromString(token));
    if (paste.isEmpty()) return redirect.notFound(attributes);
    if (!paste.get().passwordMatch(password)) return redirect.incorrectPassword(attributes, token);
    authorizationStorage.store(token);
    attributes.addAttribute("token", token);
    return new RedirectView("/paste");
  }

  private String buildLink(Paste paste) {
    return ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("paste")
        .queryParam("token", paste.getId())
        .build()
        .toUriString();
  }

  private boolean unauthorized(Paste paste) {
    return paste.passwordProtected() && !authorizationStorage.contains(paste.getId().toString());
  }
}
