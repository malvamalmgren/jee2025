package com.example.rules;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//Target är vart annotationen får användas
@Target({ElementType.TYPE}) //TYPE är Klasser och records, kan lägga till ', ElementType.FIELD'
//Retention gör så att valideringen finns kvar under körning, tas bort normalt sätt
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidGameValidator.class})
public @interface ValidGame {
    String message() default "Invalid game request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
