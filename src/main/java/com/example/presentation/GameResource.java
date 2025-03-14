package com.example.presentation;

import com.example.business.GameService;
import com.example.dto.CreateGame;
import com.example.dto.GameResponse;
import com.example.dto.UpdateGame;
import com.example.entity.Game;
import com.example.persistance.GameRepository;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.java.Log;
import com.example.mapper.GameMapper;

import java.util.List;

//Plural föredraget
//Klassen som syns utåt
@Path("games") //gör det till jax rs klass, skannas/enable:as med application som vår ... extendar
@Log
public class GameResource {
    //Heter i Spring Boot Controllers istället för Resources, som MVC
    //private static final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    private GameRepository repository;
    private GameService gameService;

    @Inject //dependency injection
    public GameResource(GameRepository repository,  GameService gameService) {
        this.repository = repository;
        this.gameService = gameService;
    }

    public GameResource() {
    }

    //URI/Sökväg
    @GET
    @Produces(MediaType.APPLICATION_JSON) //fler typer {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML}
    public List<GameResponse> getAllGames() {
        return gameService.getAllGames();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Game getGameById(@PathParam("id") Long id) {
        return gameService.getGameById(id);
    }

    @GET
    @Path("title/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Game getGameByTitle(@PathParam("title") String title) {
        return gameService.getGameByTitle(title);
    }

    @GET
    @Path("publisher/{publisher}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<GameResponse> getGamesByPublisher(@PathParam("publisher") String publisher) {
        return gameService.getGamesByPublisher(publisher);
    }

    @GET
    @Path("/release")
    @Produces(MediaType.APPLICATION_JSON)
    public List<GameResponse> getAllGamesByRelease() {
        return gameService.getAllGamesByRelease();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createGame(@Valid CreateGame game) {
        if (game == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Game cannot be null")
                    .build();
        }
        Game newGame = GameMapper.map(game);
        newGame = repository.insert(newGame);
        log.info("Creating game " + game); //För att se om valideringen fungerar
        return Response
                .status(Response.Status.CREATED)
                .header("Location", "/api/games/" + newGame.getId())
                .build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateGame(Game game, @PathParam("id") Long id) { //Innan Long @Positive validering t.ex
        gameService.getGameById(id);
        game.setId(id);
        repository.save(game);
        return Response.noContent().build();
    }

    @PATCH
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateGameFieldByField(@Valid UpdateGame game, @PathParam("id") Long id) {
        var oldGame = gameService.getGameById(id);
        if (game.title() != null)
            oldGame.setTitle(game.title());
        if (game.release() != null)
            oldGame.setRelease(game.release());
        if (game.publisher() != null)
            oldGame.setPublisher(game.publisher());
        if (game.description() != null)
            oldGame.setDescription(game.description());
        if (game.genres() != null)
            oldGame.setGenres(game.genres());
        if (game.price() != null)
            oldGame.setPrice(game.price());
        repository.update(oldGame);
        return Response.noContent().build();
    }
}
