package nl.hu.cisq1.lingo.trainer.presentation;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.application.GameService;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameControllerTest {
    static GameService gameService = mock(GameService.class);
    static GameController gameController = new GameController(gameService);

    @Test
    @DisplayName("Testing if all games are returned on the getAllGames method")
    void getGames() {
        try {
            Game game = new Game();
            when(gameService.getAllGames()).thenReturn(List.of(game));
            ResponseEntity<CollectionModel<GameDTOResponse>> gamesResponse = gameController.getGames();

            Collection games = Objects.requireNonNull(gamesResponse.getBody()).getContent();
            assertEquals(1, games.size());
        } catch (NotFoundException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Testing if the correct game is returned when getting a game by id")
    void getGameById() {
        try {
            Game game = new Game();
            GameDTOResponse DTO = new GameDTOResponse(game);
            when(gameService.getGameById(1L)).thenReturn(game);
            assertEquals(DTO.getFinished(), Objects.requireNonNull(gameController.getGameById(1l).getBody()).getFinished());
            assertEquals(DTO.getActiveRound(), Objects.requireNonNull(gameController.getGameById(1l).getBody()).getActiveRound());
            assertEquals(DTO.getScore(), Objects.requireNonNull(gameController.getGameById(1l).getBody()).getScore());
            assertEquals(DTO.getId(), Objects.requireNonNull(gameController.getGameById(1l).getBody()).getId());
        } catch (NotFoundException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Testing if only active games are returned when getting the active games")
    void getActiveGames() {
        try {
            Game game = new Game();
            when(gameService.getActiveGames()).thenReturn(List.of(game));
            ResponseEntity<CollectionModel<GameDTOResponse>> gamesResponse = gameController.getActiveGames();

            Collection games = Objects.requireNonNull(gamesResponse.getBody()).getContent();
            assertEquals(1, games.size());
        } catch (NotFoundException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Testing if a game is created correctly")
    void startNewGame() {
        Game game = new Game();
        GameDTOResponse DTO = new GameDTOResponse(game);
        when(gameService.startNewGame()).thenReturn(game);

        assertEquals(DTO.getFinished(), Objects.requireNonNull(gameController.startGame().getBody()).getFinished());
        assertEquals(DTO.getActiveRound(), Objects.requireNonNull(gameController.startGame().getBody()).getActiveRound());
        assertEquals(DTO.getScore(), Objects.requireNonNull(gameController.startGame().getBody()).getScore());
        assertEquals(DTO.getId(), Objects.requireNonNull(gameController.startGame().getBody()).getId());
    }

    @Test
    @DisplayName("Testing if a round is started correctly")
    void startNewRound() {
        try {
            Game game = new Game();
            Round round = game.newRound("apple");
            RoundDTOResponse DTO = new RoundDTOResponse(round);
            when(gameService.startNewRound(1L)).thenReturn(round);

            assertEquals(DTO.getAttempts(), Objects.requireNonNull(gameController.startRound(1L).getBody()).getAttempts());
            assertEquals(DTO.getHint(), Objects.requireNonNull(gameController.startRound(1L).getBody()).getHint());
            assertEquals(DTO.getState(), Objects.requireNonNull(gameController.startRound(1L).getBody()).getState());
            assertEquals(DTO.getWordToGuess(), Objects.requireNonNull(gameController.startRound(1L).getBody()).getWordToGuess());
        } catch (NotFoundException | IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Testing if a wrong guess gets the correct feedback")
    void guessWrong() {
        try {
            Game game = new Game();
            game.newRound("apple");
            Round round = game.makeGuess("aaaaa");
            RoundDTOResponse DTO = new RoundDTOResponse(round);
            when(gameService.guess(1L, "aaaaa")).thenReturn(round);

            GuessDTORequest request = new GuessDTORequest();
            request.guess = "aaaaa";

            assertEquals(DTO.getAttempts().get(0).getAttempt(), Objects.requireNonNull(gameController.makeGuess(1L, request).getBody()).getAttempts().get(0).getAttempt());
            assertEquals(DTO.getHint(), Objects.requireNonNull(gameController.makeGuess(1L, request).getBody()).getHint());
            assertEquals(DTO.getState(), Objects.requireNonNull(gameController.makeGuess(1L, request).getBody()).getState());
            assertEquals(DTO.getWordToGuess(), Objects.requireNonNull(gameController.makeGuess(1L, request).getBody()).getWordToGuess());
        } catch (NotFoundException | IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Testing if a right guess gets the correct feedback")
    void guessRight() {
        try {
            Game game = new Game();
            game.newRound("apple");
            Round round = game.makeGuess("apple");
            RoundDTOResponse DTO = new RoundDTOResponse(round);
            when(gameService.guess(1L, "apple")).thenReturn(round);

            GuessDTORequest request = new GuessDTORequest();
            request.guess = "apple";

            assertEquals(DTO.getAttempts().get(0).getAttempt(), Objects.requireNonNull(gameController.makeGuess(1L, request).getBody()).getAttempts().get(0).getAttempt());
            assertEquals(DTO.getHint(), Objects.requireNonNull(gameController.makeGuess(1L, request).getBody()).getHint());
            assertEquals(DTO.getState(), Objects.requireNonNull(gameController.makeGuess(1L, request).getBody()).getState());
            assertEquals(DTO.getWordToGuess(), Objects.requireNonNull(gameController.makeGuess(1L, request).getBody()).getWordToGuess());
        } catch (NotFoundException | IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Testing if losing a game correctly changes the state")
    void guessLost() {
        try {
            Game game = new Game();
            game.newRound("apple");
            game.makeGuess("aaaaa");
            game.makeGuess("aaaaa");
            game.makeGuess("aaaaa");
            game.makeGuess("aaaaa");
            Round round = game.makeGuess("aaaaa");
            RoundDTOResponse DTO = new RoundDTOResponse(round);
            when(gameService.guess(1L, "aaaaa")).thenReturn(round);

            GuessDTORequest request = new GuessDTORequest();
            request.guess = "aaaaa";

            assertEquals(DTO.getAttempts().get(0).getAttempt(), Objects.requireNonNull(gameController.makeGuess(1L, request).getBody()).getAttempts().get(0).getAttempt());
            assertEquals(DTO.getAttempts().get(1).getAttempt(), Objects.requireNonNull(gameController.makeGuess(1L, request).getBody()).getAttempts().get(1).getAttempt());
            assertEquals(DTO.getAttempts().get(2).getAttempt(), Objects.requireNonNull(gameController.makeGuess(1L, request).getBody()).getAttempts().get(2).getAttempt());
            assertEquals(DTO.getAttempts().get(3).getAttempt(), Objects.requireNonNull(gameController.makeGuess(1L, request).getBody()).getAttempts().get(3).getAttempt());
            assertEquals(DTO.getAttempts().get(4).getAttempt(), Objects.requireNonNull(gameController.makeGuess(1L, request).getBody()).getAttempts().get(4).getAttempt());
            assertEquals(DTO.getHint(), Objects.requireNonNull(gameController.makeGuess(1L, request).getBody()).getHint());
            assertEquals(DTO.getState(), Objects.requireNonNull(gameController.makeGuess(1L, request).getBody()).getState());
            assertEquals(DTO.getWordToGuess(), Objects.requireNonNull(gameController.makeGuess(1L, request).getBody()).getWordToGuess());
        } catch (NotFoundException | IllegalArgumentException e) {
            fail();
        }
    }}