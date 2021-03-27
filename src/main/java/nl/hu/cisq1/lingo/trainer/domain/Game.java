package nl.hu.cisq1.lingo.trainer.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "game_id")
    private Long id;
    private int score;
    private Boolean isFinished = false;
    @OneToMany(mappedBy = "game")
    private List<Round> rounds = new ArrayList<>();;

    public Game() {}

    public Round newRound(String wordToGuess) throws IllegalArgumentException {
        if (!isFinished()) {
            List<Round> activeRounds = this.rounds.stream().filter(e -> e.getState() == State.PLAYING).collect(Collectors.toList());
            if (activeRounds.size() == 0) {
                Round round = new Round(wordToGuess);
                rounds.add(round);
                return round;
            }
            throw new IllegalArgumentException("Can't start a new round while other rounds are active");
        }
        throw new IllegalArgumentException("Game is finished, no new round started");
    }

    public Round makeGuess(String guess) throws IllegalArgumentException {
        if (!isFinished()) {
            List<Round> activeRounds = this.rounds.stream().filter(e -> e.getState() == State.PLAYING).collect(Collectors.toList());
            if (activeRounds.size() == 1) {
                Round activeRound = activeRounds.get(0);
                activeRound.makeGuess(guess);
                if (activeRound.getState().equals(State.LOST)) {
                    isFinished = true;
                } if (activeRound.getState().equals(State.WON)) {
                    score += 1;
                }
                return activeRound;
            }
            throw new IllegalArgumentException("No valid round found");
        }
        throw new IllegalArgumentException("Game is finished, no guess made");
    }

    public int getScore() {
        return score;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public Boolean isFinished() {
        return isFinished;
    }

    public int getWordLengthForNextRound() {
        if (this.rounds.isEmpty()) {
            return 5;
        }
        return rounds.size()%3+5;
    }
}
