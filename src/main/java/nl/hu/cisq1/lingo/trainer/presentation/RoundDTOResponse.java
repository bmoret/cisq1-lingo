package nl.hu.cisq1.lingo.trainer.presentation;

import nl.hu.cisq1.lingo.trainer.domain.Feedback;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.trainer.domain.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RoundDTOResponse {
    private State state;
    private List<String> hint;
    private List<FeedbackDTOResponse> attempts;
    private String wordToGuess;

    public RoundDTOResponse(Round round) {
        this.state = round.getState();
        this.hint = round.getHint();
        attempts = new ArrayList<>();
        for (Feedback feedback : round.getGuesses()) {
            this.attempts.add(new FeedbackDTOResponse(feedback));
        }
        if (state != State.PLAYING) {
            this.wordToGuess = round.getWordToGuess();
        }
    }

    public State getState() {
        return state;
    }

    public List<String> getHint() {
        return hint;
    }

    public List<nl.hu.cisq1.lingo.trainer.presentation.FeedbackDTOResponse> getAttempts() {
        return attempts;
    }

    public String getWordToGuess() {
        return wordToGuess;
    }
}
