package com.example.rules;

import com.example.dto.CreateGame;
import com.example.dto.UpdateGame;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.Value;


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
        } else if (createGame.genres().size() == 0) {
            context
                    .buildConstraintViolationWithTemplate("Genres cannot be empty.")
                    .addPropertyNode("genres").addConstraintViolation();
            valid = false;
        }
        return valid;
    }


//    @Override
//    public boolean isValid(Object value, ConstraintValidatorContext context) {
//        return switch (value) {
//            case CreateGame game -> validateCreateGame(CreateGame game);
//            case UpdateGame game -> validateUpdateGame(game.title());
//            default -> false;
//        }
//    }
}