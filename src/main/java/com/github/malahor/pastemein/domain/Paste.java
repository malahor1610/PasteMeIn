package com.github.malahor.pastemein.domain;

import com.github.malahor.pastemein.InputData;
import com.github.malahor.pastemein.util.PastePasswordEncoder;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "paste")
@NoArgsConstructor
public class Paste {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false)
  private UUID id;

  private String text;
  private String password;
  private boolean editable;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "expiryTime"))
  private ExpiryTime expiryTime;

  public Paste(InputData input) {
    this.text = input.getText();
    this.expiryTime = new ExpiryTime(input.getExpiryValue(), input.getExpiryUnit());
    this.password = input.isUsePassword() ? PastePasswordEncoder.encode(input.getPassword()) : "";
    this.editable = input.isEditable();
  }

  public void update(InputData input) {
    this.text = input.getText();
  }

  public boolean expired() {
    return LocalDateTime.now().isAfter(expiryTime.getValue());
  }

  public boolean passwordProtected() {
    return !password.isEmpty();
  }

  public boolean passwordMatch(String password) {
    return PastePasswordEncoder.matches(password, this.password);
  }
}
