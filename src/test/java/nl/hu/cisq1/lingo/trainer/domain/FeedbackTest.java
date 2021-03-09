package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        Feedback feedback = new Feedback("@woord", List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID));

        assertFalse(feedback.isWordValid());
    }

    @Test
    @DisplayName("word is not guessed if not all letters are correct")
    void wordIsNotInvalid() {
        Feedback feedback = new Feedback("woord", List.of(Mark.PRESENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));

        assertTrue(feedback.isWordValid());
    }
}