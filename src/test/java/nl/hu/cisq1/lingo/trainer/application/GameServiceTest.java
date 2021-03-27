package nl.hu.cisq1.lingo.trainer.application;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameServiceTest {
    WordService wordService = mock(WordService.class);
    SpringGameRepository gameRepository = mock(SpringGameRepository.class);
    GameService gameService = new GameService(gameRepository, wordService);

    Game inRound1Game;
    Game betweenRound1And2Game;
    Game betweenRound2And3Game;
    Game lostGame;

    @BeforeEach
    void beforeEach() {
        when(wordService.provideRandomWord(5)).thenReturn("apple");
        when(wordService.provideRandomWord(6)).thenReturn("though");
        when(wordService.provideRandomWord(7)).thenReturn("thunder");


        inRound1Game = new Game();
        betweenRound1And2Game = new Game();
        betweenRound2And3Game = new Game();
        lostGame = new Game();

        inRound1Game.newRound(wordService.provideRandomWord(inRound1Game.getWordLengthForNextRound()));

        betweenRound1And2Game.newRound(wordService.provideRandomWord(betweenRound1And2Game.getWordLengthForNextRound()));
        betweenRound1And2Game.makeGuess("apple");

        betweenRound2And3Game.newRound(wordService.provideRandomWord(betweenRound2And3Game.getWordLengthForNextRound()));
        betweenRound2And3Game.makeGuess("apple");
        betweenRound2And3Game.newRound(wordService.provideRandomWord(betweenRound2And3Game.getWordLengthForNextRound()));
        betweenRound2And3Game.makeGuess("though");

        lostGame.newRound(wordService.provideRandomWord(lostGame.getWordLengthForNextRound()));
        lostGame.makeGuess("words");
        lostGame.makeGuess("words");
        lostGame.makeGuess("words");
        lostGame.makeGuess("words");
        lostGame.makeGuess("words");

        when(gameRepository.findAll()).thenReturn(List.of(lostGame, inRound1Game, betweenRound1And2Game, betweenRound2And3Game));
        when(gameRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(inRound1Game));
        when(gameRepository.findById(2L)).thenReturn(java.util.Optional.ofNullable(betweenRound1And2Game));
        when(gameRepository.findById(3L)).thenReturn(java.util.Optional.ofNullable(betweenRound2And3Game));
        when(gameRepository.findById(4L)).thenReturn(java.util.Optional.ofNullable(lostGame));
        when(gameRepository.findById(5L)).thenReturn(java.util.Optional.ofNullable(null));
    }

    @Test
    void getAllGames() {
        try {
            List<Game> games = gameService.getAllGames();
            assertTrue(games.contains(lostGame) && games.contains(inRound1Game) && games.contains(betweenRound1And2Game)&& games.contains(betweenRound2And3Game));
            assertEquals(4, games.size());
        } catch (NotFoundException e) {
            fail();
        }
    }

    @Test
    void getAllGamesNoAvailable() {
        when(gameRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(
                NotFoundException.class,
                () -> gameService.getAllGames()
        );
    }

    @Test
    void getGameById() {
        try {
            assertEquals(inRound1Game, gameService.getGameById(1L));
            assertEquals(betweenRound1And2Game, gameService.getGameById(2L));
            assertEquals(betweenRound2And3Game, gameService.getGameById(3L));
            assertEquals(lostGame, gameService.getGameById(4L));
        } catch (NotFoundException e) {
            fail();
        }
    }

    @Test
    void getGameByIdFail() {
        assertThrows(
                NotFoundException.class,
                () -> gameService.getGameById(5L)
        );
    }

    @Test
    void getActiveGames() {
        try {
            List<Game> games = gameService.getActiveGames();
            assertTrue(games.contains(inRound1Game) && games.contains(betweenRound1And2Game)&& games.contains(betweenRound2And3Game));
            assertEquals(3, games.size());
        } catch (NotFoundException e) {
            fail();
        }
    }

    @Test
    void getActiveGamesNoAvailable() {
        when(gameRepository.findAll()).thenReturn(List.of(lostGame));
        assertThrows(
                NotFoundException.class,
                () -> gameService.getActiveGames()
        );
    }

    @Test
    void startNewGame() {
        Game game = gameService.startNewGame();
        assertEquals(1, game.getRounds().size());
        assertEquals("apple", game.getRounds().get(0).getWordToGuess());
    }

    @Test
    void startNewRound() {
        try {
            Game game = gameService.getGameById(2L);
            assertEquals(1,game.getRounds().size());
            Round round = gameService.startNewRound(2L);
            assertTrue(2 == game.getRounds().size() && 0 == round.getGuesses().size() && round.getWordToGuess().equals("though"));
        } catch (NotFoundException | IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    void startNewRoundWithAnotherActive() {
        assertThrows(
                IllegalArgumentException.class,
                () -> gameService.startNewRound(1L)
        );
    }

    @Test
    void startNewRoundWhenGameIsOver() {
        assertThrows(
                IllegalArgumentException.class,
                () -> gameService.startNewRound(4L)
        );
    }

    @Test
    void guess() {
        try {

            assertEquals(List.of("a","p","p","*","*"), gameService.guess(1L, "appel"));
            assertEquals(List.of("a","p","p","l","e"), gameService.guess(1L, "apple"));
            assertEquals(1, gameService.getGameById(1L).getScore());

        } catch (NotFoundException | IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    void guessWithNoActiveRound() {
        assertThrows(
                IllegalArgumentException.class,
                () -> gameService.guess(2L, "apple")
        );
    }

    @Test
    void guessWhenGameIsOver() {
        assertThrows(
                IllegalArgumentException.class,
                () -> gameService.guess(4L, "apple")
        );
    }

}