package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.exception.InvalidFeedbackException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "feedback_id")
    private Long id;
    private String attempt;
    @Enumerated
    @ElementCollection(targetClass = Mark.class)
    private List<Mark> mark;
    @ManyToOne
    @JoinColumn(name = "round_id")
    private Round round;

    public Feedback() {}

    public Feedback(String attempt, List<Mark> mark) throws InvalidFeedbackException {
        if (attempt.length() != mark.size()) throw new InvalidFeedbackException();
        this.attempt = attempt;
        this.mark = mark;
    }

    public boolean isWordGuessed() {
        return Mark.CORRECT == mark.stream().filter(e -> !e.equals(Mark.CORRECT)).findFirst().orElse(Mark.CORRECT);
    }

    public boolean isWordValid() {
        return Mark.INVALID != mark.stream().filter(e -> !e.equals(Mark.INVALID)).findFirst().orElse(Mark.INVALID);
    }

    public List<String> giveHint(List<String> previousHint, String wordToGuess) {
        int charactar = 0;
        if (previousHint == null || previousHint.isEmpty()) {
            previousHint = new ArrayList<>();
            for (int x = 0; x<wordToGuess.length(); x++) {
                previousHint.add("");
            }
        }
        for (Mark mark : this.mark) {
            if (mark.equals(Mark.CORRECT)) {
                previousHint.add(charactar, wordToGuess.charAt(charactar)+"");
                previousHint.remove(charactar+1);
            }
            charactar += 1;
        }
        return previousHint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Feedback)) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(attempt, feedback.attempt) && Objects.equals(mark, feedback.mark) && Objects.equals(round, feedback.round);
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
                ", round=" + round +
                '}';
    }
}
