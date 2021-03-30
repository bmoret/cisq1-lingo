package nl.hu.cisq1.lingo.trainer.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Entity
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "round_id")
    private Long id;
    private String wordToGuess;
    @Enumerated
    private State state;
    @ElementCollection
    private List<String> hint = new ArrayList<>();

    @OneToMany(mappedBy = "round")
    private List<Feedback> guesses = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    public Round() {}

    public Round(String wordToGuess) {
        this.wordToGuess = wordToGuess;
        this.state = State.PLAYING;
        for (int x = 0; x < wordToGuess.length(); x++) {
            if (x == 0) {
                hint.add(wordToGuess.substring(0,1));
            } else {
                hint.add("");
            }
        }
    }

    public Feedback makeGuess(String guess) {
        if (state.equals(State.PLAYING)) {
            Feedback feedback = getFeedback(guess);
            if (feedback.isWordValid()) {
                guesses.add(feedback);
                hint = feedback.giveHint(hint, wordToGuess);
            }
            if (feedback.isWordGuessed()) {
                state = State.WON;
            } else if (guesses.size() == 5) {
                state = State.LOST;
            }
            return feedback;
        }
        throw new IllegalArgumentException("Game is finished, no new round started");

    }

    public Feedback getFeedback(String guess) {
        List<String> guessTemp = new ArrayList<>();
        List<String> wordToGuessTemp = new ArrayList<>();
        List<Mark> mark = new ArrayList<>();

        if (guess.length() == getWordToGuess().length()) {
            for (int x = 0; x < guess.length(); x++) {
                guessTemp.add(String.valueOf(guess.charAt(x)));
                wordToGuessTemp.add(String.valueOf(wordToGuess.charAt(x)));
            }
        }

        Pattern p = Pattern.compile("[^a-zA-Z0-9]");
        for (int x = 0; x < guess.length(); x++) {
            if (guess.length() != getWordToGuess().length()) {
                mark.add(Mark.INVALID);
            } else if (p.matcher(guess).find()) {
                mark.add(Mark.INVALID);
            } else if (guess.charAt(x) == wordToGuess.charAt(x)) {
                mark.add(Mark.CORRECT);
                guessTemp.remove(String.valueOf(guess.charAt(x)));
                wordToGuessTemp.remove(String.valueOf(wordToGuess.charAt(x)));
            }
        }
        for (int x = 0; x < guess.length(); x++) {
            if (guess.length() == getWordToGuess().length() && !p.matcher(guess).find() && guess.charAt(x) != wordToGuess.charAt(x)) {
                if (wordToGuessTemp.contains(guessTemp.get(0))) {
                    mark.add(x, Mark.PRESENT);
                    guessTemp.remove(String.valueOf(guess.charAt(x)));
                    wordToGuessTemp.remove(String.valueOf(guess.charAt(x)));
                } else {
                    mark.add(x, Mark.ABSENT);
                    guessTemp.remove(String.valueOf(guess.charAt(x)));
                }
            }
        }
        return new Feedback(guess, mark);
    }

    public List<Feedback> getGuesses() {
        return guesses;
    }

    public List<String> getHint() {
        return hint;
    }

    public State getState() {
        return state;
    }

    public String getWordToGuess() {
        return wordToGuess;
    }
}
