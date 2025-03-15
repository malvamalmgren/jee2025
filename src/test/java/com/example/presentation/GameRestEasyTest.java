package com.example.presentation;

import com.example.business.GameService;
import com.example.dto.CreateGame;
import com.example.dto.GameResponse;
import com.example.dto.UpdateGame;
import com.example.entity.Game;
import com.example.exceptions.EntryAlreadyExistsException;
import com.example.exceptions.NotFoundException;
import com.example.exceptions.mappers.EntryAlreadyExistsMapper;
import com.example.exceptions.mappers.NotFoundMapper;
import com.example.mapper.GameMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.undertow.Undertow;
import io.undertow.servlet.api.DeploymentInfo;
import org.hamcrest.Matchers;
import org.jboss.resteasy.core.ResteasyDeploymentImpl;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class GameRestEasyTest {
    @Mock
    GameService gameService;

    private GameResource gameResource;
    private UndertowJaxrsServer server;
    private int port;

    @BeforeEach
    public void setup() throws IOException {
        gameResource = new GameResource(gameService);

        // Find an available port
        try (ServerSocket socket = new ServerSocket(0)) {
            port = socket.getLocalPort();
        }

        // Create and start the server
        server = new UndertowJaxrsServer();
        server.start(Undertow.builder().addHttpListener(port, "localhost"));

        // Create deployment
        ResteasyDeployment deployment = new ResteasyDeploymentImpl();

        // Register resources and providers
        deployment.getResources().add(gameResource);
        deployment.getProviders().add(new NotFoundMapper());
        deployment.getProviders().add(new EntryAlreadyExistsMapper());

        // Deploy our application at /api
        DeploymentInfo deploymentInfo = server.undertowDeployment(deployment);
        deploymentInfo.setClassLoader(GameResourceTest.class.getClassLoader());
        deploymentInfo.setDeploymentName("REST API");
        deploymentInfo.setContextPath("/api");

        server.deploy(deploymentInfo);

        // Configure REST-Assured
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = "/api";
    }

    @AfterEach
    public void tearDown() {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    void getAllGamesReturnsAllGames() {
        Mockito.when(gameService.getAllGames()).thenReturn(List.of(
                new GameResponse(1L, "Pong", 1972, "Atari", "Kinda tennis", List.of("PVP"), BigDecimal.TWO)));
        RestAssured.given()
                .when()
                .get("/games")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title", Matchers.hasItem("Pong"));
    }

    @Test
    void getGameByIdFound() {
        Long id = 1L;
        Game game = new Game();
        game.setId(id);
        game.setTitle("Pong");
        game.setPublisher("Atari");
        game.setRelease(1972);
        game.setDescription("Kinda tennis");
        game.setGenres(List.of("PVP"));

        Mockito.when(gameService.getGameById(id)).thenReturn(game);

        RestAssured.given()
                .when()
                .get("/games/{id}", id)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", Matchers.equalTo(1))
                .body("title", Matchers.equalTo("Pong"));
    }

    @Test
    void getGameByIdNotFound() {
        Long id = 100L;
        Mockito.when(gameService.getGameById(id))
                .thenThrow(new NotFoundException("Game with id " + id + " not found"));

        RestAssured.given()
                .when()
                .get("/games/{id}", id)
                .then()
                .statusCode(404);
    }

    @Test
    void createGameReturnsCreatedResponse() {
        CreateGame createGame = new CreateGame("Pong", 1972, "Atari", "Kinda tennis", List.of("PVP"), BigDecimal.TEN);
        Game game = GameMapper.map(createGame);
        Mockito.when(gameService.createGame(Mockito.any(CreateGame.class))).thenReturn(game);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createGame)
                .when()
                .post("/games")
                .then()
                .statusCode(201);
    }

    @Test
    void ifGameAlreadyExistsCreateGameReturnsConflictResponse() {
        CreateGame createGame = new CreateGame("Pong", 1972, "Atari", "Kinda tennis", List.of("PVP"), BigDecimal.TEN);

        Mockito.when(gameService.createGame(Mockito.any(CreateGame.class)))
                .thenThrow(new EntryAlreadyExistsException("Game entry already exists"));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createGame)
                .when()
                .post("/games")
                .then()
                .statusCode(409);
    }

    @Test
    void updateGameReturnsOkResponse() {
        Long id = 1L;
        CreateGame createGame = new CreateGame("Pong", 1972, "Atari", "Kinda tennis", List.of("PVP"), BigDecimal.TEN);
        Game updatedGame = GameMapper.map(createGame);
        updatedGame.setId(id);

        Mockito.when(gameService.updateGame(Mockito.any(CreateGame.class), Mockito.eq(id))).thenReturn(updatedGame);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createGame)
                .when()
                .put("/games/{id}", id)
                .then()
                .statusCode(200);
    }

    @Test
    void updateGameFieldByFieldReturnsOkResponse() {
        Long id = 1L;
        UpdateGame updateGame = new UpdateGame("Ping", null, null, null, null, null);
        Game patchedGame = new Game();
        patchedGame.setId(id);
        patchedGame.setTitle("Ping");

        Mockito.when(gameService.updateGameFieldByField(Mockito.any(UpdateGame.class), Mockito.eq(id)))
                .thenReturn(patchedGame);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updateGame)
                .when()
                .patch("/games/{id}", id)
                .then()
                .statusCode(200);
    }

    @Test
    void deleteGameReturnsOkResponse() {
        RestAssured.given()
                .when()
                .delete("/games/{id}", 1L)
                .then()
                .statusCode(200);
    }

    @Test
    void getGamesByPublisherReturnGames() {
        String publisher = "Atari";
        Game game = new Game();
        game.setId(1L);
        game.setTitle("Pong");
        game.setPublisher(publisher);
        game.setRelease(1972);
        game.setDescription("Kinda tennis");
        game.setGenres(List.of("PVP"));
        List<GameResponse> responses = List.of(new GameResponse(game));

        Mockito.when(gameService.getGamesByPublisher(publisher)).thenReturn(responses);

        RestAssured.given()
                .get("/games/publisher/{publisher}", publisher)
                .then()
                .statusCode(200)
                .body("id",Matchers.hasItem(1))
                .body("publisher",Matchers.hasItem(publisher));
    }
}
