package com.github.malahor.pastemein.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ExpiryValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExpiryValid {

  String message() default "Expiry value must not be less than 1";

  String expiryValue() default "expiryValue";

  String expiryUnit() default "expiryUnit";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
