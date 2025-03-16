package com.example.presentation;

import com.example.business.GameService;
import com.example.dto.CreateGame;
import com.example.dto.GameResponse;
import com.example.dto.UpdateGame;
import com.example.entity.Game;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.java.Log;

import java.util.List;

@Path("games")
@Log
public class GameResource {

    private GameService gameService;

    @Inject
    public GameResource(GameService gameService) {
        this.gameService = gameService;
    }

    public GameResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<GameResponse> getAllGames(@QueryParam("page") int page, @QueryParam("size") int size) {
        return gameService.getAllGames(page, size);
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
    public List<GameResponse> getGamesByPublisher(@PathParam("publisher") String publisher, @QueryParam("page") int page, @QueryParam("size") int size) {
        return gameService.getGamesByPublisher(publisher, page, size);
    }

    @GET
    @Path("/release")
    @Produces(MediaType.APPLICATION_JSON)
    public List<GameResponse> getAllGamesByRelease(@QueryParam("page") int page, @QueryParam("size") int size) {
        return gameService.getAllGamesByRelease(page, size);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createGame(@Valid CreateGame game) {
        if (game == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Game cannot be null")
                    .build();
        }

        Game newGame = gameService.createGame(game);
        log.info("Creating game " + newGame);

        return Response
                .status(Response.Status.CREATED)
                .header("Location", "/api/games/" + newGame.getId())
                .build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateGame(@Valid CreateGame createGame, @PathParam("id") Long id) {
        gameService.updateGame(createGame, id);
        return Response.ok().build();
    }

    @PATCH
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateGameFieldByField(@Valid UpdateGame game, @PathParam("id") Long id) {
        gameService.updateGameFieldByField(game, id);
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteGame(@PathParam("id") Long id) {
        gameService.deleteGame(id);
        return Response.ok().build();
    }
}
