package nl.hu.cisq1.lingo.trainer.presentation;

import nl.hu.cisq1.lingo.trainer.domain.Feedback;

import java.util.Objects;

public class FeedbackDTOResponse {
    private String attempt;

    public FeedbackDTOResponse(Feedback feedback) {
        this.attempt = feedback.getAttempt();
    }

    public String getAttempt() {
        return attempt;
    }
}
