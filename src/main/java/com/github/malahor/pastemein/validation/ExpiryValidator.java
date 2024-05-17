package com.github.malahor.pastemein.validation;

import com.github.malahor.pastemein.domain.ExpiryUnit;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class ExpiryValidator implements ConstraintValidator<ExpiryValid, Object> {
  private String expiryValue;
  private String expiryUnit;

  public void initialize(ExpiryValid constraintAnnotation) {
    this.expiryValue = constraintAnnotation.expiryValue();
    this.expiryUnit = constraintAnnotation.expiryUnit();
  }

  public boolean isValid(Object value, ConstraintValidatorContext context) {
    var expiryValue = (int) new BeanWrapperImpl(value).getPropertyValue(this.expiryValue);
    var expiryUnit = (ExpiryUnit) new BeanWrapperImpl(value).getPropertyValue(this.expiryUnit);
    if (!ExpiryUnit.NEVER.equals(expiryUnit)) return expiryValue > 0;
    else return true;
  }
}
