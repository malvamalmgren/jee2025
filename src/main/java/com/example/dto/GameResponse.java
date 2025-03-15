package com.example.dto;

import com.example.entity.Game;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link Game}
 */
public record GameResponse(Long id, String title, Integer release, String publisher, String description,
                           List<String> genres, BigDecimal price) {

    public GameResponse(Game game) {
        this(game.getId(), game.getTitle(), game.getRelease(), game.getPublisher(), game.getDescription(), game.getGenres(), game.getPrice());
    }

    public static GameResponse map(Game game) {
        return new GameResponse(game.getId(), game.getTitle(), game.getRelease(), game.getPublisher(), game.getDescription(), game.getGenres(), game.getPrice());
    }
}