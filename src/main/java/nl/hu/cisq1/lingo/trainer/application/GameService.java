package nl.hu.cisq1.lingo.trainer.application;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {
    private final SpringGameRepository REPOSITORY;
    private final WordService WORD_SERVICE;

    public GameService(SpringGameRepository repository, WordService word_service) {
        REPOSITORY = repository;
        WORD_SERVICE = word_service;
    }

    public List<Game> getAllGames() throws NotFoundException {
        List<Game> games = REPOSITORY.findAll();
        if(games.isEmpty()) throw new NotFoundException("There are no games available.");
        return games;
    }

    public Game getGameById(Long id) throws NotFoundException {
        return REPOSITORY.findById(id)
                .orElseThrow(() -> new NotFoundException("No game with this id: "+id+" found"));
    }

    public List<Game> getActiveGames() throws NotFoundException {
        List<Game> activeGames = getAllGames().stream().filter(e -> !e.isFinished()).collect(Collectors.toList());

        if(activeGames.isEmpty()) throw new NotFoundException("There are no active games available.");
        return activeGames;
    }

    public Game startNewGame() {
        Game game = new Game();

        game.newRound(WORD_SERVICE.provideRandomWord(game.getWordLengthForNextRound()));

        REPOSITORY.save(game);

        return game;
    }

    public Round startNewRound(Long id) throws NotFoundException, IllegalArgumentException {
        Game game = getGameById(id);

        Round round = game.newRound(WORD_SERVICE.provideRandomWord(game.getWordLengthForNextRound()));

        REPOSITORY.save(game);

        return round;
    }

    public List<String> guess(Long id, String guess) throws NotFoundException, IllegalArgumentException {
        Game game = getGameById(id);

        Round round = game.makeGuess(guess);

        REPOSITORY.save(game);

        return round.getHint();
    }


}
