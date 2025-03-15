package com.example.dto;

import com.example.rules.ValidGame;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

@ValidGame()
public record CreateGame(String title,
                         @Positive(message = "Release year must be positive")
                         @Min(1970)
                         @NotNull
                         Integer release,
                         @NotBlank String publisher,
                         String description,
                         List<String> genres,
                         @NotNull
                         @Positive(message = "Price must be positive")
                         BigDecimal price) {

}
