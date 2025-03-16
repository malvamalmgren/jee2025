package com.example.business;

import com.example.dto.CreateGame;
import com.example.dto.UpdateGame;
import com.example.exceptions.EntryAlreadyExistsException;
import com.example.mapper.GameMapper;
import com.example.persistence.GameRepository;
import com.example.dto.GameResponse;
import com.example.entity.Game;
import com.example.exceptions.NotFoundException;
import jakarta.data.Order;
import jakarta.data.Sort;
import jakarta.data.page.PageRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class GameService {
    private final static int DEFAULT_PAGE = 1;
    private final static int DEFAULT_PAGE_SIZE = 10;

    private GameRepository repository;

    @Inject
    public GameService(GameRepository repository) {
        this.repository = repository;
    }

    public GameService() {
    }

    private PageRequest createPageRequest(int page, int size) {
        return PageRequest.ofPage(page == 0 ? DEFAULT_PAGE : page).size(size == 0 ? DEFAULT_PAGE_SIZE : size);
    }

    public List<GameResponse> getAllGames(int page, int size) {
        return repository.findAll(createPageRequest(page, size), Order.by(Sort.asc("id"))).stream()
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

    public List<GameResponse> getAllGamesByRelease(int page, int size) {
        return repository.findAllByRelease(createPageRequest(page, size)).stream()
                .map(GameResponse::new)
                .filter(Objects::nonNull)
                .toList();
    }

    public List<GameResponse> getGamesByPublisher(String publisher, int page, int size) {
        return repository.findByPublisher(publisher, createPageRequest(page, size)).stream()
                .map(GameResponse::new)
                .filter(Objects::nonNull)
                .toList();
    }

    public Game createGame(CreateGame createGame) {
        getGameByTitlePublisherRelease(createGame.title(), createGame.publisher(), createGame.release())
                .ifPresent(_ -> {
                    throw new EntryAlreadyExistsException("Game entry already exists");
                });
        Game game = GameMapper.map(createGame);

        return repository.insert(game);
    }

    public Game updateGame(CreateGame updateGame, Long id) {
        Game game = GameMapper.map(updateGame);
        game.setId(id);
        return repository.save(game);
    }

    public Game updateGameFieldByField(UpdateGame updateGame, Long id) {
        Game oldGame = getGameById(id);
        if (updateGame.title() != null)
            oldGame.setTitle(updateGame.title());
        if (updateGame.release() != null)
            oldGame.setRelease(updateGame.release());
        if (updateGame.publisher() != null)
            oldGame.setPublisher(updateGame.publisher());
        if (updateGame.description() != null)
            oldGame.setDescription(updateGame.description());
        if (updateGame.genres() != null)
            oldGame.setGenres(updateGame.genres());
        if (updateGame.price() != null)
            oldGame.setPrice(updateGame.price());
        return repository.update(oldGame);
    }

    public void deleteGame(Long id) {
        Game game = getGameById(id);
        repository.delete(game);
    }
}
