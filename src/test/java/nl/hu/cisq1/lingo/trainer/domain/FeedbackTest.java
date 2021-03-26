package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.exception.InvalidFeedbackException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {
    @Test
    @DisplayName("word is guessed if all letters are correct")
    void wordIsGuessed() {
        Feedback feedback = new Feedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));

        assertTrue(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("word is not guessed if not all letters are correct")
    void wordIsNotGuessed() {
        Feedback feedback = new Feedback("boord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));

        assertFalse(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("word is invalid when to many or to few letters are given and when none alphabetic characters are given")
    void wordIsInvalid() {
        Feedback feedback = new Feedback("@oord", List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID));

        assertFalse(feedback.isWordValid());
    }

    @Test
    @DisplayName("word is valid when all of its letters are alphabetic characters and has the right amount of letters")
    void wordIsNotInvalid() {
        Feedback feedback = new Feedback("woord", List.of(Mark.PRESENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));

        assertTrue(feedback.isWordValid());
    }

    @Test
    @DisplayName("feedback is invalid when the word and feedback are not the same length")
    void wordHasInvalidFeedback() {
        assertThrows(
                InvalidFeedbackException.class,
                () -> new Feedback("woord", List.of(Mark.CORRECT))
        );
    }

    @ParameterizedTest
    @MethodSource("provideListsForGiveHint")
    @DisplayName("hint given in various scenarios")
    void giveHint(List<String> previousHint, Feedback feedback, List<String> expected) {
        assertEquals(expected, feedback.giveHint(previousHint,"woord"));
    }

    private static Stream<Arguments> provideListsForGiveHint() {
        List<String> expected = new ArrayList<>();
        expected.add("");
        expected.add("o");
        expected.add("o");
        expected.add("r");
        expected.add("d");

        List<String> a1 = new ArrayList<>();
        a1.add("");
        a1.add("o");
        a1.add("o");
        a1.add("");
        a1.add("");

        Feedback a2 = new Feedback("beerd", List.of(Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.CORRECT, Mark.CORRECT));

        List<String> b1 = null;

        Feedback b2 = new Feedback("boord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));

        List<String> c1 = new ArrayList<>();
        c1.add("");
        c1.add("");
        c1.add("o");
        c1.add("");
        c1.add("d");

        Feedback c2 = new Feedback("boert", List.of(Mark.ABSENT, Mark.CORRECT, Mark.ABSENT, Mark.CORRECT, Mark.ABSENT));

        return Stream.of(
                Arguments.of(a1, a2, expected),
                Arguments.of(b1, b2, expected),
                Arguments.of(c1, c2, expected)
        );
    }

}