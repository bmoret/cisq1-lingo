package nl.hu.cisq1.lingo.trainer.application;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    private final SpringGameRepository REPOSITORY;

    public GameService(SpringGameRepository repository) {
        REPOSITORY = repository;
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


}
