package nl.hu.cisq1.lingo.trainer.presentation;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.application.GameService;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/lingo")
public class GameController {
    private final GameService SERVICE;

    public GameController(GameService service) {
        SERVICE = service;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<GameDTOResponse>> getGames() throws NotFoundException {
        List<Game> games = SERVICE.getAllGames();
        List<GameDTOResponse> gameDTOResponses = new ArrayList<>();

        for (Game game : games) {
            gameDTOResponses.add(new GameDTOResponse(game));
        }

        CollectionModel<GameDTOResponse> results = CollectionModel.of(gameDTOResponses);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}
