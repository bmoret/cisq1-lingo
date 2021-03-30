package nl.hu.cisq1.lingo.trainer.presentation;

import nl.hu.cisq1.lingo.trainer.domain.Game;

import java.util.Objects;

public class GameDTOResponse {
    private Long id;
    private int score;
    private Boolean isFinished;
    private RoundDTOResponse activeRound;

    public GameDTOResponse(Game game) {
        this.id = game.getId();
        this.score = game.getScore();
        this.isFinished = game.isFinished();
        System.out.println(game.getActiveRound());
        if (game.getActiveRound() != null) {
            this.activeRound = new RoundDTOResponse(game.getActiveRound());
        }
    }

    public int getScore() {
        return score;
    }

    public Boolean getFinished() {
        return isFinished;
    }

    public Long getId() {
        return id;
    }

    public RoundDTOResponse getActiveRound() {
        return activeRound;
    }
}
