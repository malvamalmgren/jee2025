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
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.spi.Dispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class GameResourceTest {

    @Mock
    GameService gameService;
    Dispatcher dispatcher;

    @BeforeEach
    public void setup() {
        dispatcher = MockDispatcherFactory.createDispatcher();
        GameResource gameResource = new GameResource(gameService);
        dispatcher.getRegistry().addSingletonResource(gameResource);
        ExceptionMapper<NotFoundException> notFoundMapper = new NotFoundMapper();
        ExceptionMapper<EntryAlreadyExistsException> entryAlreadyExistsMapper = new EntryAlreadyExistsMapper();
        dispatcher.getProviderFactory().registerProviderInstance(notFoundMapper);
        dispatcher.getProviderFactory().registerProviderInstance(entryAlreadyExistsMapper);
    }

    @Test
    void getAllGamesReturnsAllGames() throws URISyntaxException, UnsupportedEncodingException {
        Mockito.when(gameService.getAllGames(0, 0)).thenReturn(List.of(
                new GameResponse(1L, "Ping", 1971, "Atari", "Not tennis", List.of("PVP"), BigDecimal.ONE),
                new GameResponse(2L, "Pong", 1972, "Atari", "Kinda tennis", List.of("PVP"), BigDecimal.TWO)));
        MockHttpRequest request = MockHttpRequest.get("/games");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(200, response.getStatus());
        String content = response.getContentAsString();
        assert (content.contains("Ping"));
        assert (content.contains("Pong"));
    }

    @Test
    void createGameReturnsCreatedResponse() throws Exception {
        Game game = new Game();
        game.setId(1L);
        Mockito.when(gameService.createGame(Mockito.any())).thenReturn(game);
        MockHttpRequest request = MockHttpRequest.post("/games");

        String json = new ObjectMapper().writeValueAsString(
                new CreateGame("Pong", 1972, "Atari", "Basically tennis", List.of("PVP"), BigDecimal.TEN)
        );
        request.content(json.getBytes());
        request.contentType(MediaType.APPLICATION_JSON);
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(201, response.getStatus());
    }

    @Test
    void ifGameAlreadyExistsCreateGameReturnsConflictResponse() throws Exception {
        Mockito.when(gameService.createGame(Mockito.any()))
                .thenThrow(new EntryAlreadyExistsException("Game entry already exists"));
        MockHttpRequest request = MockHttpRequest.post("/games");
        String json = new ObjectMapper().writeValueAsString(
                new CreateGame("Pong", 1972, "Atari", "Basically tennis", List.of("PVP"), BigDecimal.TEN)
        );
        request.content(json.getBytes());
        request.contentType(MediaType.APPLICATION_JSON);
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(409, response.getStatus());
    }

    @Test
    void getGameByIdFound() throws URISyntaxException, UnsupportedEncodingException {
        Game game = new Game();
        game.setId(1L);
        game.setTitle("Pong");
        Mockito.when(gameService.getGameById(1L)).thenReturn(game);
        MockHttpRequest request = MockHttpRequest.get("/games/1");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(200, response.getStatus());
        String content = response.getContentAsString();
        assert (content.contains("Pong"));
    }

    @Test
    void getGameByIdNotFound() throws URISyntaxException {
        Mockito.when(gameService.getGameById(999L))
                .thenThrow(new NotFoundException("Game with id 999 not found"));
        MockHttpRequest request = MockHttpRequest.get("/games/999");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(404, response.getStatus());
    }

    @Test
    void getGameByTitleFound() throws URISyntaxException, UnsupportedEncodingException {
        Game game = new Game();
        game.setId(1L);
        game.setTitle("Pong");
        Mockito.when(gameService.getGameByTitle("Pong")).thenReturn(game);
        MockHttpRequest request = MockHttpRequest.get("/games/title/Pong");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(200, response.getStatus());
        String content = response.getContentAsString();
        assert (content.contains("Pong"));
    }

    @Test
    void getGameByTitleNotFound() throws URISyntaxException {
        Mockito.when(gameService.getGameByTitle("NonexistentGame"))
                .thenThrow(new NotFoundException("Game with title NonexistentGame not found"));
        MockHttpRequest request = MockHttpRequest.get("/games/title/NonexistentGame");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(404, response.getStatus());
    }

    @Test
    void getAllGamesByReleaseReturnGames() throws URISyntaxException, UnsupportedEncodingException {
        Mockito.when(gameService.getAllGamesByRelease(0, 0)).thenReturn(List.of(
                new GameResponse(1L, "Ping", 1971, "Atari", "Not tennis", List.of("PVP"), BigDecimal.ONE),
                new GameResponse(2L, "Pong", 1972, "Atari", "Kinda tennis", List.of("PVP"), BigDecimal.TWO)));
        MockHttpRequest request = MockHttpRequest.get("/games/release");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(200, response.getStatus());
        String content = response.getContentAsString();
        assert (content.contains("Ping"));
        assert (content.contains("Pong"));
    }

    @Test
    void getGamesByPublisherReturnGames() throws URISyntaxException, UnsupportedEncodingException {
        Mockito.when(gameService.getGamesByPublisher("Atari", 0, 0)).thenReturn(List.of(new GameResponse(1L, "Ping", 1971, "Atari", "Not tennis", List.of("PVP"), BigDecimal.ONE)));
        MockHttpRequest request = MockHttpRequest.get("/games/publisher/Atari");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(200, response.getStatus());
        String content = response.getContentAsString();
        assert (content.contains("Atari"));
    }

    @Test
    void updateGameReturnsOkResponse() throws Exception {
        Game updatedGame = new Game();
        updatedGame.setId(1L);
        updatedGame.setTitle("Pong Updated");
        Mockito.when(gameService.updateGame(Mockito.any(),Mockito.eq(1L) ))
                .thenReturn(updatedGame);
        MockHttpRequest request = MockHttpRequest.put("/games/1");
        String json = new ObjectMapper().writeValueAsString(
                new CreateGame("Pong Updated", 1972, "Atari", "Updated description", List.of("PVP"), BigDecimal.TEN)
        );
        request.content(json.getBytes());
        request.contentType(MediaType.APPLICATION_JSON);
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(200, response.getStatus());
    }

    @Test
    void updateGameNotFound() throws Exception {
        Mockito.when(gameService.updateGame(Mockito.any(), Mockito.eq(999L)))
                .thenThrow(new NotFoundException("Game with id 999 not found"));
        MockHttpRequest request = MockHttpRequest.put("/games/999");
        String json = new ObjectMapper().writeValueAsString(
                new CreateGame("Nonexistent", 1972, "Atari", "No game", List.of("PVP"), BigDecimal.TEN)
        );
        request.content(json.getBytes());
        request.contentType(MediaType.APPLICATION_JSON);
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(404, response.getStatus());
    }

    @Test
    void updateGameFieldByFieldReturnsOkResponse() throws Exception {
        Game updatedGame = new Game();
        updatedGame.setId(1L);
        updatedGame.setTitle("Pong Patched");
        Mockito.when(gameService.updateGameFieldByField(Mockito.any(), Mockito.eq(1L)))
                .thenReturn(updatedGame);
        MockHttpRequest request = MockHttpRequest.patch("/games/1");
        String json = new ObjectMapper().writeValueAsString(
                new UpdateGame("Pong Patched", null, null, null, null, null)
        );
        request.content(json.getBytes());
        request.contentType(MediaType.APPLICATION_JSON);
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(200, response.getStatus());
    }

    @Test
    void updateGameFieldByFieldNotFound() throws Exception {
        Mockito.when(gameService.updateGameFieldByField(Mockito.any(), Mockito.eq(999L)))
                .thenThrow(new NotFoundException("Game with id 999 not found"));
        MockHttpRequest request = MockHttpRequest.patch("/games/999");
        String json = new ObjectMapper().writeValueAsString(
                new UpdateGame("NonexistentGame", null, null, null, null, null)
        );
        request.content(json.getBytes());
        request.contentType(MediaType.APPLICATION_JSON);
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(404, response.getStatus());
    }

    @Test
    void deleteGameReturnsOkResponse() throws URISyntaxException {
        Game game = new Game();
        game.setId(1L);

        MockHttpRequest request = MockHttpRequest.delete("/games/1");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(200, response.getStatus());
    }

    @Test
    void deleteGameNotFound() throws URISyntaxException {
        Mockito.doThrow(new NotFoundException("Game with id 999 not found"))
                .when(gameService).deleteGame(999L);
        MockHttpRequest request = MockHttpRequest.delete("/games/999");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(404, response.getStatus());
    }

}
