package com.example.dto;

import com.example.rules.ValidGame;
import jakarta.validation.constraints.*;
import jdk.jfr.Timespan;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

@ValidGame() //egen annotation
public record CreateGame(String title,
                         @Positive(message = "Release year must be positive")
                         @Min(1970)
                         @NotNull
                         Integer release,
                         @NotBlank String publisher,
                         String description,
                         @NotNull List<String> genres,
                         @NotNull
                         @Positive(message = "Price must be positive")
                         BigDecimal price) {

}
