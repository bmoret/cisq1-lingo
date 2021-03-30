package nl.hu.cisq1.lingo.trainer.application;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameServiceTest {
    static WordService wordService = mock(WordService.class);
    static SpringGameRepository gameRepository = mock(SpringGameRepository.class);
    static GameService gameService = new GameService(gameRepository, wordService);

    static Game inRound1Game;
    static Game betweenRound1And2Game;
    static Game betweenRound2And3Game;
    static Game lostGame;

    @BeforeAll
    static void beforeAll() {
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

    @ParameterizedTest
    @MethodSource("provideGamesForGetGame")
    void getGameById(Game game, Long id) {
        try {
            assertEquals(game, gameService.getGameById(id));
        } catch (NotFoundException e) {
            fail();
        }
    }

    private static Stream<Arguments> provideGamesForGetGame() {
        return Stream.of(
                Arguments.of(inRound1Game, 1L),
                Arguments.of(betweenRound1And2Game, 2L),
                Arguments.of(betweenRound2And3Game, 3L),
                Arguments.of(lostGame, 4L)
        );

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
            gameService.guess(2L, "though");
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
            inRound1Game.getActiveRound();

            assertEquals(inRound1Game.getActiveRound(), gameService.guess(1L, "appel"));
            assertEquals(inRound1Game.getActiveRound(), gameService.guess(1L, "apple"));
            assertEquals(1, gameService.getGameById(1L).getScore());
            gameService.startNewRound(1L); // new round has to be started, so that the object is set back to the state it was in before this test

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