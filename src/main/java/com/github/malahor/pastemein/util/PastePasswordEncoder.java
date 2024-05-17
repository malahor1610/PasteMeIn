package com.github.malahor.pastemein.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PastePasswordEncoder {

  private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

  public static String encode(String password) {
    return encoder.encode(password);
  }

  public static boolean matches(String password, String encoded) {
    return encoder.matches(password, encoded);
  }
}
