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
        List<Mark> mark = List.of(Mark.CORRECT);
        assertThrows(
                InvalidFeedbackException.class,
                () -> new Feedback("woord", mark)
        );
    }

    @ParameterizedTest
    @MethodSource("provideListsForGiveHint")
    @DisplayName("hint given in various scenarios")
    void giveHint(List<String> previousHint, Feedback feedback, List<String> expected) {
        assertEquals(expected, feedback.giveHint(previousHint,"woord"));
    }

    private static Stream<Arguments> provideListsForGiveHint() {
        List<String> expectedA = List.of("","o","o","r","d");
        List<String> expectedB = List.of("","","o","r","d");
        List<String> expectedC = List.of("","o","o","r","d");


        List<String> a1 = new ArrayList<>();
        a1.add("");
        a1.add("o");
        a1.add("o");
        a1.add("");
        a1.add("");

        Feedback a2 = new Feedback("bwerd", List.of(Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.CORRECT, Mark.CORRECT));

        List<String> b1 = null;

        Feedback b2 = new Feedback("bword", List.of(Mark.ABSENT, Mark.PRESENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));

        List<String> c1 = new ArrayList<>();
        c1.add("");
        c1.add("");
        c1.add("o");
        c1.add("");
        c1.add("d");

        Feedback c2 = new Feedback("boert", List.of(Mark.ABSENT, Mark.CORRECT, Mark.ABSENT, Mark.CORRECT, Mark.ABSENT));

        return Stream.of(
                Arguments.of(a1, a2, expectedA),
                Arguments.of(b1, b2, expectedB),
                Arguments.of(c1, c2, expectedC)
        );
    }

    @Test
    void testEqualsTrue() {
        Feedback feedback1 = new Feedback("woord", List.of(Mark.CORRECT,Mark.PRESENT,Mark.CORRECT,Mark.ABSENT,Mark.ABSENT));
        Feedback feedback2 = new Feedback("woord", List.of(Mark.CORRECT,Mark.PRESENT,Mark.CORRECT,Mark.ABSENT,Mark.ABSENT));

        assertEquals(feedback1, feedback2);
    }

    @Test
    void testEqualsTrueSame() {
        Feedback feedback1 = new Feedback("woord", List.of(Mark.CORRECT,Mark.PRESENT,Mark.CORRECT,Mark.ABSENT,Mark.ABSENT));

        assertEquals(feedback1, feedback1);
    }

    @Test
    void testEqualsFalseNull() {
        Feedback feedback1 = new Feedback("woord", List.of(Mark.CORRECT,Mark.PRESENT,Mark.CORRECT,Mark.ABSENT,Mark.ABSENT));
        Feedback feedback2 = null;

        assertNotEquals(feedback1, feedback2);
    }

    @Test
    void testEqualsFalseMark() {
        Feedback feedback1 = new Feedback("woord", List.of(Mark.CORRECT,Mark.PRESENT,Mark.CORRECT,Mark.ABSENT,Mark.ABSENT));
        Feedback feedback2 = new Feedback("woord", List.of(Mark.CORRECT,Mark.PRESENT,Mark.CORRECT,Mark.ABSENT,Mark.CORRECT));

        assertNotEquals(feedback1, feedback2);
    }

    @Test
    void testEqualsFalseWord() {
        Feedback feedback1 = new Feedback("woord", List.of(Mark.CORRECT,Mark.PRESENT,Mark.CORRECT,Mark.ABSENT,Mark.ABSENT));
        Feedback feedback2 = new Feedback("hallo", List.of(Mark.CORRECT,Mark.PRESENT,Mark.CORRECT,Mark.ABSENT,Mark.ABSENT));

        assertNotEquals(feedback1, feedback2);
    }

    @Test
    void testToString() {
        String expected = "Feedback{" +
                "attempt='woord'" +
                ", mark=" + List.of(Mark.CORRECT,Mark.PRESENT,Mark.CORRECT,Mark.ABSENT,Mark.ABSENT) +
                ", round=" + null +
                '}';
        Feedback feedback = new Feedback("woord", List.of(Mark.CORRECT,Mark.PRESENT,Mark.CORRECT,Mark.ABSENT,Mark.ABSENT));

        assertEquals(expected, feedback.toString());
    }

}