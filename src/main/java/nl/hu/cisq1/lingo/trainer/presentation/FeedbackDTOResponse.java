package nl.hu.cisq1.lingo.trainer.presentation;

import nl.hu.cisq1.lingo.trainer.domain.Feedback;
import nl.hu.cisq1.lingo.trainer.domain.Mark;

import java.util.List;

public class FeedbackDTOResponse {
    private String attempt;
    private List<Mark> mark;

    public FeedbackDTOResponse(Feedback feedback) {
        this.attempt = feedback.getAttempt();
        this.mark = feedback.getMark();
    }

    public String getAttempt() {
        return attempt;
    }

    public List<Mark> getMark() {
        return mark;
    }
}
