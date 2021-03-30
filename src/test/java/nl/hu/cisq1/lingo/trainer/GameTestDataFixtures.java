package nl.hu.cisq1.lingo.trainer;

import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.words.data.SpringWordRepository;
import org.springframework.boot.CommandLineRunner;

public class GameTestDataFixtures implements CommandLineRunner {
    private final SpringGameRepository repository;

    public GameTestDataFixtures(SpringGameRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        Game game1 = new Game();
        game1.newRound("pizza");

        Game game2 = new Game();
        game2.newRound("pizza");
        game2.makeGuess("pizza");

        Game game3 = new Game();
        game3.newRound("pizza");
        game3.makeGuess("aaaaa");
        game3.makeGuess("aaaaa");
        game3.makeGuess("aaaaa");
        game3.makeGuess("aaaaa");
        game3.makeGuess("aaaaa");

        this.repository.save(game1);
        this.repository.save(game2);
        this.repository.save(game3);
    }
}
