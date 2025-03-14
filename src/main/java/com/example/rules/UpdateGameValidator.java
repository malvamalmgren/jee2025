package com.example.rules;

import com.example.dto.CreateGame;
import com.example.dto.UpdateGame;
import com.example.entity.Game;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.Value;

import java.io.Serializable;


public class UpdateGameValidator implements ConstraintValidator<ValidUpdateGame, UpdateGame> {
    @Override
    public boolean isValid(UpdateGame updateGame, ConstraintValidatorContext context) {
        boolean valid = true;
        if (updateGame.title() != null) {
            if (updateGame.title().length() == 0) {
                context
                        .buildConstraintViolationWithTemplate("Title cannot be blank")
                        .addPropertyNode("title").addConstraintViolation();
                valid = false;
            } else if (!Character.isUpperCase(updateGame.title().charAt(0))) {
                context
                        .buildConstraintViolationWithTemplate("Title must start with a capital letter.")
                        .addPropertyNode("title").addConstraintViolation();
                valid = false;
            }
        }
        if (updateGame.genres() != null) {
            if (updateGame.genres().size() == 0) {
                context
                        .buildConstraintViolationWithTemplate("Genres cannot be empty.")
                        .addPropertyNode("genres").addConstraintViolation();
                valid = false;
            }
        }
        if (updateGame.publisher() != null && updateGame.publisher().length() == 0) {
            context
                    .buildConstraintViolationWithTemplate("Publisher cannot be blank.")
                    .addPropertyNode("publisher").addConstraintViolation();
            valid = false;
        }
        return valid;
    }
}