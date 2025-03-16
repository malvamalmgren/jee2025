package com.example.presentation;

import com.example.dto.CreateGame;
import com.example.dto.GameResponse;
import com.example.entity.Game;
import com.example.mapper.GameMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameMapperTest {

    @Test
    void mapGameToResponseMapsCorrectly() {
        Game game = new Game();
        game.setId(1L);
        game.setTitle("Pong");
        game.setRelease(1972);
        game.setPublisher("Atari");
        game.setDescription("Kinda tennis");
        game.setGenres(List.of("PVP"));
        game.setPrice(BigDecimal.TEN);

        GameResponse response = GameMapper.map(game);
        assertEquals(game.getId(), response.id());
        assertEquals(game.getTitle(), response.title());
        assertEquals(game.getRelease(), response.release());
        assertEquals(game.getPublisher(), response.publisher());
        assertEquals(game.getDescription(), response.description());
        assertEquals(game.getGenres(), response.genres());
        assertEquals(game.getPrice(), response.price());
    }

    @Test
    void mapCreateGameToEntityMapsCorrectly() {
        CreateGame createGame = new CreateGame("Pong", 1972, "Atari", "Kinda tennis", List.of("PVP"), BigDecimal.TEN);
        Game game = GameMapper.map(createGame);
        assertEquals(createGame.title(), game.getTitle());
        assertEquals(createGame.release(), game.getRelease());
        assertEquals(createGame.publisher(), game.getPublisher());
        assertEquals(createGame.description(), game.getDescription());
        assertEquals(createGame.genres(), game.getGenres());
        assertEquals(createGame.price(), game.getPrice());
    }

}
