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
    @ManyToOne(optional = false)
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
        int charactarIndex = 0;
        if (previousHint == null) {
            previousHint = new ArrayList<>();
            for (int x = 0; x<wordToGuess.length(); x++) {
                previousHint.add("");
            }
        }
        for (Mark mark : this.mark) {
            if (previousHint.get(charactarIndex).equals("*")) {
                previousHint.remove(charactarIndex);
                previousHint.add(charactarIndex, "");
            }
            if (mark.equals(Mark.PRESENT)) {
                if (previousHint.get(charactarIndex).isEmpty()) {
                    previousHint.remove(charactarIndex);
                    previousHint.add(charactarIndex, "*");
                }
            }
            if (mark.equals(Mark.CORRECT)) {
                previousHint.remove(charactarIndex);
                previousHint.add(charactarIndex, wordToGuess.charAt(charactarIndex)+"");
            }
            charactarIndex += 1;
        }
        return previousHint;
    }

    public String getAttempt() {
        return attempt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAttempt(String attempt) {
        this.attempt = attempt;
    }

    public List<Mark> getMark() {
        return mark;
    }

    public void setMark(List<Mark> mark) {
        this.mark = mark;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
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
