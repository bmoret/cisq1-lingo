package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    @DisplayName("Tests if a new round is added to a game")
    void newRound() {
        Game game = new Game();

        Round round = game.newRound("woord");

        assertEquals(1, game.getRounds().size());
        assertTrue(game.getRounds().contains(round));
        assertEquals(game, round.getGame());
    }

    @Test
    @DisplayName("Tests if a new round can be started if the game is won")
    void newRoundWhenRoundIsWon() {
        Game game = new Game();

        Round round1 = game.newRound("woord");

        game.makeGuess("woord");

        Round round2 = game.newRound("woord");

        assertEquals(2, game.getRounds().size());
        assertTrue(game.getRounds().contains(round1));
        assertTrue(game.getRounds().contains(round2));
    }


    @Test
    @DisplayName("Tests if a new round can't be started if there is an active round")
    void newRoundWhenOnIsAlreadyActive() {
        Game game = new Game();

        game.newRound("woord");
        assertThrows(
                IllegalArgumentException.class,
                () -> game.newRound("woord")
        );
    }

    @Test
    @DisplayName("Tests if a new round can't be started if the game is over")
    void newRoundWhenGameIsOver() {
        Game game = new Game();

        game.newRound("woord");

        game.makeGuess("doord");
        game.makeGuess("doord");
        game.makeGuess("doord");
        game.makeGuess("doord");
        game.makeGuess("doord");

        assertThrows(
                IllegalArgumentException.class,
                () -> game.newRound("woord")
        );

    }

    @Test
    @DisplayName("Tests if a guess is added to a round")
    void makeGuess() {
        Game game = new Game();

        Round round = game.newRound("woord");

        assertEquals(0, round.getGuesses().size());

        game.makeGuess("doord");

        assertEquals(1, round.getGuesses().size());
    }

    @Test
    @DisplayName("Tests if a guess can't be made when no round is active")
    void makeGuessWhenNoRoundHasStarted() {
        Game game = new Game();

        assertThrows(
                IllegalArgumentException.class,
                () -> game.makeGuess("woord")
        );

    }

    @Test
    @DisplayName("Tests if a guess can't be made when the game is over")
    void makeGuessWhenGameIsOver() {
        Game game = new Game();

        game.newRound("woord");

        game.makeGuess("doord");
        game.makeGuess("doord");
        game.makeGuess("doord");
        game.makeGuess("doord");
        game.makeGuess("doord");

        assertThrows(
                IllegalArgumentException.class,
                () -> game.makeGuess("woord")
        );

    }

    @Test
    @DisplayName("Tests if the games score is increased after guessing the word")
    void scoreIsIncreasedWhenRoundIsWon() {
        Game game = new Game();

        assertEquals(0, game.getScore());

        game.newRound("woord");
        game.makeGuess("woord");

        assertEquals(1, game.getScore());

        game.newRound("woord");
        game.makeGuess("boord");
        game.makeGuess("woord");

        assertEquals(2, game.getScore());

    }

    @Test
    @DisplayName("Tests if the games score is not increased after not guessing the word")
    void scoreIsNotIncreasedWhenWordIsNotGuessed() {
        Game game = new Game();

        assertEquals(0, game.getScore());

        game.newRound("woord");
        game.makeGuess("woord");

        assertEquals(1, game.getScore());

        game.newRound("woord");
        game.makeGuess("boord");

        assertEquals(1, game.getScore());

    }

    @Test
    @DisplayName("Tests if the games score is increased after losing the game")
    void scoreIsNotIncreasedWhenRoundIsLost() {
        Game game = new Game();

        assertEquals(0, game.getScore());

        game.newRound("woord");
        game.makeGuess("woord");

        assertEquals(1, game.getScore());

        game.newRound("woord");
        game.makeGuess("boord");
        game.makeGuess("boord");
        game.makeGuess("boord");
        game.makeGuess("boord");
        game.makeGuess("boord");

        assertEquals(1, game.getScore());

    }

    @Test
    @DisplayName("Tests if the word length is returned correctly at the start of the game")
    void wordLengthStart() {
        Game game = new Game();

        assertEquals(5, game.getWordLengthForNextRound());
    }

    @Test
    @DisplayName("Tests if the word length is returned correctly during round 1 of the game")
    void wordLengthRound1() {
        Game game = new Game();

        for (int x = 0; x < 3; x++) {
            assertEquals(5, game.getWordLengthForNextRound());
            game.newRound("woord");
            game.makeGuess("woord");
            assertEquals(6, game.getWordLengthForNextRound());
            game.newRound("wooord");
            game.makeGuess("wooord");
            assertEquals(7, game.getWordLengthForNextRound());
            game.newRound("woooord");
            game.makeGuess("woooord");
            assertEquals(5, game.getWordLengthForNextRound());
        }

    }
}