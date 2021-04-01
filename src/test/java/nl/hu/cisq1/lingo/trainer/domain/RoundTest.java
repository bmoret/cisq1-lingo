package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {

    @Test
    @DisplayName("Testing if the hint is updated, after successive guesses")
    void hintIsSaved() {
        Round round = new Round("woord");

        round.makeGuess("aaard");

        assertEquals(List.of("w","","","r","d"), round.getHint());

        round.makeGuess("wooaa");

        assertEquals(List.of("w","o","o","r","d"), round.getHint());
    }

    @Test
    @DisplayName("Testing if a round is won, after a correct guess")
    void gameIsWonAfterOneGuess() {
        Round round = new Round("woord");

        Feedback feedback = round.makeGuess("woord");

        assertEquals(round, feedback.getRound());
        assertEquals(State.WON, round.getState());
    }

    @Test
    @DisplayName("Testing if a round is active, after a incorrect guess")
    void gameIsPlayingAfterOneGuess() {
        Round round = new Round("woord");

        round.makeGuess("boord");

        assertEquals(State.PLAYING, round.getState());
    }

    @Test
    @DisplayName("Testing if a round is won, after five guesses")
    void gameIsWonAfterFiveGuesses() {
        Round round = new Round("woord");

        round.makeGuess("doord");
        round.makeGuess("doord");
        round.makeGuess("doord");
        round.makeGuess("doord");
        round.makeGuess("woord");

        assertEquals(State.WON, round.getState());
    }

    @Test
    @DisplayName("Testing if a invalid guess returns an IllegalArgumentException")
    void gameThrowsExceptionAfterInvalidGuess() {
        Round round = new Round("woord");

        assertThrows(
                IllegalArgumentException.class,
                () -> round.makeGuess("asdoor")
        );
    }

    @Test
    @DisplayName("Testing if a round is won, even after guessing more than five times because of invalid guesses")
    void gameIsWonAfterInvalidGuesses() {
        Round round = new Round("woord");

        round.makeGuess("doord");
        round.makeGuess("doord");
        round.makeGuess("doord");
        round.makeGuess("doord");
        try {
            round.makeGuess("asdoor");
        } catch (IllegalArgumentException ignored) {}
        try {
            round.makeGuess("@door");
        } catch (IllegalArgumentException ignored) {}
        round.makeGuess("woord");

        assertEquals(State.WON, round.getState());
    }

    @Test
    @DisplayName("Testing if a round is lost, after five wrong guesses")
    void gameIsLostAfterFiveGuesses() {
        Round round = new Round("woord");

        round.makeGuess("doord");
        round.makeGuess("doord");
        round.makeGuess("doord");
        round.makeGuess("doord");
        round.makeGuess("doord");

        assertEquals(State.LOST, round.getState());
    }

    @Test
    @DisplayName("Testing if a exception is thrown, after guessing again, after losing")
    void throwsExceptionWhenGuessingAfterLoss() {
        Round round = new Round("woord");

        round.makeGuess("doord");
        round.makeGuess("doord");
        round.makeGuess("doord");
        round.makeGuess("doord");
        round.makeGuess("doord");

        assertThrows(
                IllegalArgumentException.class,
                () -> round.makeGuess("doord")
        );
    }

    @Test
    @DisplayName("Testing if a exception is thrown, after guessing again, after winning")
    void throwsExceptionWhenGuessingAfterWin() {
        Round round = new Round("woord");

        round.makeGuess("woord");

        assertThrows(
                IllegalArgumentException.class,
                () -> round.makeGuess("woord")
        );
    }

    @ParameterizedTest
    @MethodSource("provideListsForMakeGuess")
    @DisplayName("Testing if feedback is made correctly")
    void getFeedback(String guess, String wordToGuess, Feedback expected) {
        Round round = new Round(wordToGuess);

        assertEquals(expected, round.getFeedback(guess));
    }

    private static Stream<Arguments> provideListsForMakeGuess() {
        String a1 = "boord";
        String a2 = "woord";
        List<Mark> aMark = Arrays.asList(Mark.ABSENT,Mark.CORRECT,Mark.CORRECT,Mark.CORRECT,Mark.CORRECT);
        Feedback a3 = new Feedback(a1, aMark);

        String b1 = "ddddd";
        String b2 = "woord";
        List<Mark> bMark = Arrays.asList(Mark.ABSENT,Mark.ABSENT,Mark.ABSENT,Mark.ABSENT,Mark.CORRECT);
        Feedback b3 = new Feedback(b1, bMark);

        String c1 = "bosod";
        String c2 = "woord";
        List<Mark> cMark = Arrays.asList(Mark.ABSENT,Mark.CORRECT,Mark.ABSENT,Mark.PRESENT,Mark.CORRECT);
        Feedback c3 = new Feedback(c1, cMark);

        String d1 = "aaaao";
        String d2 = "woord";
        List<Mark> dMark = Arrays.asList(Mark.ABSENT,Mark.ABSENT,Mark.ABSENT,Mark.ABSENT,Mark.PRESENT);
        Feedback d3 = new Feedback(d1, dMark);

        String e1 = "boorde";
        String e2 = "woord";
        List<Mark> eMark = Arrays.asList(Mark.INVALID,Mark.INVALID,Mark.INVALID,Mark.INVALID,Mark.INVALID,Mark.INVALID);
        Feedback e3 = new Feedback(e1, eMark);

        String f1 = "aaa@o";
        String f2 = "woord";
        List<Mark> fMark = Arrays.asList(Mark.INVALID,Mark.INVALID,Mark.INVALID,Mark.INVALID,Mark.INVALID);
        Feedback f3 = new Feedback(f1, fMark);


        return Stream.of(
                Arguments.of(a1, a2, a3),
                Arguments.of(b1, b2, b3),
                Arguments.of(c1, c2, c3),
                Arguments.of(d1, d2, d3),
                Arguments.of(e1, e2, e3),
                Arguments.of(f1, f2, f3)
        );
    }
}