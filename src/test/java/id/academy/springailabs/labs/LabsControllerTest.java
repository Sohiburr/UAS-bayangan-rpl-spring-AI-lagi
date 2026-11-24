package id.academy.springailabs.labs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("demo")
class LabsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void basicPromptReturnsGeneratedText() throws Exception {
        mockMvc.perform(get("/api/labs/chapter-1/basic"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.concept").value("Spring AI"))
                .andExpect(jsonPath("$.answer", containsString("aplikasi Java")));
    }

    @Test
    void structuredPromptMapsResponseToConceptCard() throws Exception {
        mockMvc.perform(get("/api/labs/chapter-1/structured"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.concept").value("Spring AI"))
                .andExpect(jsonPath("$.keyPoints.length()").value(2));
    }

    @Test
    void relevancyEvaluatorPassesGroundedAnswer() throws Exception {
        mockMvc.perform(post("/api/labs/chapter-2/relevancy")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "question": "What is the boiling point of water?",
                                  "context": "At sea level, pure water boils at 100 degrees Celsius.",
                                  "response": "At sea level, pure water boils at 100 degrees Celsius."
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.passed").value(true))
                .andExpect(jsonPath("$.score").value(1.0));
    }

    @Test
    void relevancyEvaluatorRejectsUnrelatedAnswer() throws Exception {
        mockMvc.perform(post("/api/labs/chapter-2/relevancy")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "question": "What is the boiling point of water?",
                                  "context": "At sea level, pure water boils at 100 degrees Celsius.",
                                  "response": "Koalas sleep for many hours each day."
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.passed").value(false));
    }

    @Test
    void factCheckerRejectsUnsupportedClaim() throws Exception {
        mockMvc.perform(post("/api/labs/chapter-2/fact-check")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "question": "Which planet is closest to the Sun?",
                                  "context": "Mercury is the closest planet to the Sun.",
                                  "response": "Mars is the closest planet to the Sun."
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.passed").value(false));
    }
}
