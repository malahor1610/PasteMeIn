package com.github.malahor.pastemein.util;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorizationStorage {

  private static final String STORAGE_NAME = "authorizedTokens";
  private final HttpServletRequest request;

  public void store(String token) {
    var tokens = tokens();
    if (tokens.contains(token)) return;
    tokens.add(token);
    request.getSession().setAttribute(STORAGE_NAME, tokens);
  }

  public boolean contains(String token) {
    return tokens().contains(token);
  }

  private List<String> tokens() {
    return Optional.ofNullable((List<String>) request.getSession().getAttribute(STORAGE_NAME))
        .orElseGet(ArrayList::new);
  }
}
