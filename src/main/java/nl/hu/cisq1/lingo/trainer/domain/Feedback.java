package nl.hu.cisq1.lingo.trainer.domain;

import java.util.List;
import java.util.Objects;

public class Feedback {
    private String attempt;
    private List<Mark> mark;

    public Feedback(String attempt, List<Mark> mark) {
        this.attempt = attempt;
        this.mark = mark;
    }

    public boolean isWordGuessed() {
        return Mark.CORRECT == mark.stream().filter(e -> !e.equals(Mark.CORRECT)).findFirst().orElse(Mark.CORRECT);
    }

    public boolean isWordValid() {
        return Mark.INVALID != mark.stream().filter(e -> !e.equals(Mark.INVALID)).findFirst().orElse(Mark.INVALID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Feedback)) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(attempt, feedback.attempt) && Objects.equals(mark, feedback.mark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attempt, mark);
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "attempt='" + attempt + '\'' +
                ", mark=" + mark +
                '}';
    }
}
