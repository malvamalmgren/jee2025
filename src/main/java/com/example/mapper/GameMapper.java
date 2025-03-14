package com.example.mapper;

import com.example.dto.CreateGame;
import com.example.dto.GameResponse;
import com.example.entity.Game;

import java.util.ArrayList;
import java.util.List;

public class GameMapper {
    //Går ut på att ta in en datatyp och returnera en annan
    public static GameResponse map(Game game) {
        if (game == null) {
            return null;
        }
        return new GameResponse(game.getId(), game.getTitle(), game.getRelease(), game.getPublisher(), game.getDescription(), game.getGenres(), game.getPrice());
    }

    public static Game map(CreateGame game) {
        if (game == null) {
            return null;
        }
        Game newGame = new Game();

        newGame.setTitle(game.title());
        newGame.setRelease(game.release());
        newGame.setPublisher(game.publisher());
        newGame.setDescription(game.description());
        newGame.setGenres(game.genres());
        newGame.setPrice(game.price());
        return newGame;
    }
}
