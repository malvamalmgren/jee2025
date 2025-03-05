import dto.CreateGame;
import dto.GameResponse;
import dto.UpdateGame;
import entity.Game;
import exceptions.NotFound;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.java.Log;
import mapper.GameMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//Plural bra yes yes
//Klassen som syns utåt
@Path("games") //gör det till jax rs klass, skannas/enable:as med application som vår ... extendar
@Log
public class GameResource {
    //Heter i Spring Boot Controllers istället för Resources, som MVC

    //private static final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    private GameRepository repository;

    @Inject
    public GameResource(GameRepository repository) {
        this.repository = repository;
    }

    public GameResource() {
    }

    //URI/Sökväg
    @GET
    @Produces(MediaType.APPLICATION_JSON) //fler typer {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML}
    public List<GameResponse> getGames() {
        return repository.findAll()
                .map(GameResponse::new)
                .filter(Objects::nonNull)
                .toList();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Game getGame(@PathParam("id") Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFound("Game with id " + id + " not found")
        );
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createGame(CreateGame game) {
        Game newGame = GameMapper.map(game);

        newGame = repository.save(newGame);
        return Response
                .status(Response.Status.CREATED)
                .header("Location", "/api/games/" + newGame.getId())
                .build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateGame(Game game, @PathParam("id") Long id) {
        repository.findById(id).orElseThrow(() -> new NotFound("Game with id " + id + " not found"));
        game.setId(id);
        repository.save(game);
        return Response.noContent().build();
    }

    @PATCH
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateGameFieldByField(UpdateGame game, @PathParam("id") Long id) {
        var oldGame = repository.findById(id).orElseThrow(() -> new NotFound("Game with id " + id + " not found"));
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
