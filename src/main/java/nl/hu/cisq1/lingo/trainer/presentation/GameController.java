package nl.hu.cisq1.lingo.trainer.presentation;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.application.GameService;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/lingo")
public class GameController {
    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    public List<GameDTOResponse> makeResponseList(List<Game> games) {
        List<GameDTOResponse> gameDTOResponses = new ArrayList<>();

        for (Game game : games) {
            gameDTOResponses.add(new GameDTOResponse(game));
        }

        return gameDTOResponses;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<GameDTOResponse>> getGames() throws NotFoundException {
        CollectionModel<GameDTOResponse> results = CollectionModel.of(makeResponseList(service.getAllGames()));

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @GetMapping(path = "/active")
    public ResponseEntity<CollectionModel<GameDTOResponse>> getActiveGames() throws NotFoundException {
        CollectionModel<GameDTOResponse> results = CollectionModel.of(makeResponseList(service.getActiveGames()));

        return new ResponseEntity<>(results, HttpStatus.OK);
    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<GameDTOResponse> getGameById(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(new GameDTOResponse(service.getGameById(id)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GameDTOResponse> startGame() {
        return new ResponseEntity<>(new GameDTOResponse(service.startNewGame()), HttpStatus.CREATED);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<RoundDTOResponse> startRound(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(new RoundDTOResponse(service.startNewRound(id)), HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}/guess")
    public ResponseEntity<RoundDTOResponse> makeGuess(@PathVariable Long id, @RequestBody GuessDTORequest dto) throws NotFoundException {
        return new ResponseEntity<>(new RoundDTOResponse(service.guess(id, dto.guess)), HttpStatus.OK);
    }
}
