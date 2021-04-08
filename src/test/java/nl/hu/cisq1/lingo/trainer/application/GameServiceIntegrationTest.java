package nl.hu.cisq1.lingo.trainer.application;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.trainer.domain.State;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(CiTestConfiguration.class)
public class GameServiceIntegrationTest {

    @Autowired
    private GameService service;

    @Transactional
    @ParameterizedTest
    @MethodSource("provideGuesses")
    @DisplayName("Tests if guesses return the correct hint and change the rounds state")
    void guessTest(String guess, List<String> hint, State state) {
        Game game = service.startNewGame();

        Round round = null;
        try {
            round = service.guess(game.getId(), guess);
        } catch (NotFoundException e) {
            fail();
        }

        assertEquals(hint, round.getHint());
        assertEquals(state, round.getState());
    }

    private static Stream<Arguments> provideGuesses() {
        return Stream.of(
                Arguments.of("appel", List.of("p","","","",""),State.PLAYING),
                Arguments.of("pizza", List.of("p","i","z","z","a"),State.WON),
                Arguments.of("bijna", List.of("p","i","","","a"),State.PLAYING),
                Arguments.of("pinda", List.of("p","i","","","a"),State.PLAYING),
                Arguments.of("zzzzz", List.of("p","","z","z",""),State.PLAYING)
        );
    }

    @Transactional
    @Test
    @DisplayName("Tests if a game is over after losing a round")
    void guessLoseTest() {
        Game game = service.startNewGame();

        try {
            service.guess(game.getId(), "ander");
            service.guess(game.getId(), "ander");
            service.guess(game.getId(), "ander");
            service.guess(game.getId(), "ander");
            Round round = service.guess(game.getId(), "ander");

            assertEquals(State.LOST, round.getState());
            assertEquals(5, round.getGuesses().size());
            assertTrue(service.getGameById(game.getId()).isFinished());
        } catch (NotFoundException e) {
            fail();
        }

    }

    @Transactional
    @Test
    @DisplayName("Tests if an error is thrown when a invalid guess is made")
    void guessInvalidTest() {
        Game game = service.startNewGame();
        Long id = game.getId();

        assertThrows(
                IllegalArgumentException.class
                , () -> service.guess(id, "anders")
        );

    }


}
