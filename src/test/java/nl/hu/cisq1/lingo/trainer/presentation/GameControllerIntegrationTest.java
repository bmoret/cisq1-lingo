package nl.hu.cisq1.lingo.trainer.presentation;


import nl.hu.cisq1.lingo.CiTestConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(CiTestConfiguration.class)
@AutoConfigureMockMvc
class GameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Testing if a game is started correctly")
    void newGameTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/lingo");

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.score", is(0)))
                .andExpect(jsonPath("$.finished", is(false)))
                .andExpect(jsonPath("$.activeRound.wordToGuess", nullValue()))
                .andExpect(jsonPath("$.activeRound.state", is("PLAYING")))
                .andExpect(jsonPath("$.activeRound.hint").isArray())
                .andExpect(jsonPath("$.activeRound.hint", hasSize(5)))
                .andExpect(jsonPath("$.activeRound.hint", is(List.of("p","","","",""))))
                .andExpect(jsonPath("$.activeRound.attempts").isArray())
                .andExpect(jsonPath("$.activeRound.attempts", hasSize(0)));
    }

    @Test
    @DisplayName("Testing if a guess is made correctly")
    void guessTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .patch("/lingo/1/guess")
                .content("{\"guess\":\"pizza\"}")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wordToGuess", is("pizza")))
                .andExpect(jsonPath("$.state", is("WON")))
                .andExpect(jsonPath("$.hint").isArray())
                .andExpect(jsonPath("$.hint", hasSize(5)))
                .andExpect(jsonPath("$.hint", is(List.of("p","i","z","z","a"))))
                .andExpect(jsonPath("$.attempts").isArray())
                .andExpect(jsonPath("$.attempts", hasSize(1)))
                .andExpect(jsonPath("$.attempts[0].attempt", is("pizza")))
                .andExpect(jsonPath("$.attempts[0].mark").isArray())
                .andExpect(jsonPath("$.attempts[0].mark", hasSize(5)))
                .andExpect(jsonPath("$.attempts[0].mark", is(List.of("CORRECT","CORRECT","CORRECT","CORRECT","CORRECT"))));
    }
}
