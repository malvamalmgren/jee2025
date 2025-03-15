package com.example.business;

import com.example.persistance.GameRepository;
import com.example.dto.GameResponse;
import com.example.entity.Game;
import com.example.exceptions.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class GameService {

    private GameRepository repository;

    //Kan inte ha variabler här då de delas mellan alla klienter som ställer frågor

    @Inject
    public GameService(GameRepository repository) {
        this.repository = repository;
    }

    public GameService() {}

    public List<GameResponse> getAllGames() {
        return repository.findAll()
                .map(GameResponse::new)
                .filter(Objects::nonNull)
                .toList();
    }


    public Game getGameById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("Game with id " + id + " not found")
        );
    }

    public Game getGameByTitle(String title) {
        return repository.findByTitle(title).orElseThrow(
                () -> new NotFoundException("Game with title " + title + " not found")
        );
    }

    public Optional<Game> getGameByTitlePublisherRelease(String title, String publisher, Integer release) {
        return repository.findByTitlePublisherRelease(title, publisher, release);
    }

    public List<GameResponse> getAllGamesByRelease() {
        return repository.findAllByRelease()
                .map(GameResponse::new)
                .filter(Objects::nonNull)
                .toList();
    }

    public List<GameResponse> getGamesByPublisher(String publisher) {
        return repository.findByPublisher(publisher)
                .map(GameResponse::new)
                .filter(Objects::nonNull)
                .toList();
    }
}
