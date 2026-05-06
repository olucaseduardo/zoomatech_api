package com.olucaseduardo.zoomatech_api.util.interfaces;

import com.olucaseduardo.zoomatech_api.util.validator.ServiceTopicValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ServiceTopicValidator.class)
@Documented
public @interface AtLeastOneField {
    String message() default "É necessário preencher ao menos 'description' ou 'items'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
