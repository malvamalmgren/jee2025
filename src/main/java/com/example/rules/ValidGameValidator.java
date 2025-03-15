package com.example.rules;

import com.example.dto.CreateGame;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class ValidGameValidator implements ConstraintValidator<ValidGame, CreateGame> {
    @Override
    public boolean isValid(CreateGame createGame, ConstraintValidatorContext context) {
        boolean valid = true;
        if (createGame.title().length() == 0) {
            context
                    .buildConstraintViolationWithTemplate("Title cannot be blank")
                    .addPropertyNode("title").addConstraintViolation();
            valid = false;
        } else if (!Character.isUpperCase(createGame.title().charAt(0))) {
            context
                    .buildConstraintViolationWithTemplate("Title must start with a capital letter.")
                    .addPropertyNode("title").addConstraintViolation();
            valid = false;
        } else if (createGame.genres() == null) {
            context
                    .buildConstraintViolationWithTemplate("Genres cannot be null.")
                    .addPropertyNode("genres").addConstraintViolation();
            valid = false;
        } else if (createGame.genres().size() == 0) {
            context
                    .buildConstraintViolationWithTemplate("Genres cannot be empty.")
                    .addPropertyNode("genres").addConstraintViolation();
            valid = false;
        }
        return valid;
    }
}