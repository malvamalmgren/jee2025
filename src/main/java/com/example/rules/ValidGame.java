package com.example.rules;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidGameValidator.class})
public @interface ValidGame {
    String message() default "Invalid game request";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
