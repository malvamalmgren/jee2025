package com.example.dto;

import com.example.rules.ValidUpdateGame;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;
@ValidUpdateGame()
public record UpdateGame(String title,
                         @Positive(message = "Release year must be positive")
                         @Min(1970)
                         Integer release,
                         String publisher,
                         String description,
                         List<String> genres,
                         @Positive(message = "Price must be positive")
                         BigDecimal price) {
}
