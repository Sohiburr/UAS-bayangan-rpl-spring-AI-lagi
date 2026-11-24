package id.academy.springailabs.labs;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/labs")
public class LabsController {

    private final ChatClient chatClient;
    private final RelevancyEvaluator relevancyEvaluator;
    private final FactCheckingEvaluator factCheckingEvaluator;

    public LabsController(
            ChatClient.Builder chatClientBuilder,
            RelevancyEvaluator relevancyEvaluator,
            FactCheckingEvaluator factCheckingEvaluator) {
        this.chatClient = chatClientBuilder.build();
        this.relevancyEvaluator = relevancyEvaluator;
        this.factCheckingEvaluator = factCheckingEvaluator;
    }

    @GetMapping("/chapter-1/basic")
    public ConceptAnswer basicPrompt(
            @RequestParam(defaultValue = "Spring AI") String concept) {
        String answer = chatClient.prompt()
                .user("Give a concise beginner-friendly definition of " + concept + ".")
                .call()
                .content();
        return new ConceptAnswer(concept, answer);
    }

    @GetMapping("/chapter-1/structured")
    public ConceptCard structuredPrompt(
            @RequestParam(defaultValue = "Spring AI") String concept) {
        return chatClient.prompt()
                .system("Return one JSON object with a concept string and a keyPoints array.")
                .user(user -> user.text("Return a JSON object describing the concept: {concept}.")
                        .param("concept", concept))
                .call()
                .entity(ConceptCard.class);
    }

    @PostMapping("/chapter-2/relevancy")
    public EvaluationResult evaluateRelevancy(@Valid @RequestBody EvaluationInput input) {
        EvaluationRequest request = new EvaluationRequest(
                input.question(),
                List.of(new Document(input.context())),
                input.response());
        EvaluationResponse response = relevancyEvaluator.evaluate(request);
        return new EvaluationResult(
                "relevancy",
                response.isPass(),
                response.getScore(),
                response.getFeedback());
    }

    @PostMapping("/chapter-2/fact-check")
    public EvaluationResult evaluateFactCheck(@Valid @RequestBody EvaluationInput input) {
        EvaluationRequest request = new EvaluationRequest(
                input.question(),
                List.of(new Document(input.context())),
                input.response());
        EvaluationResponse response = factCheckingEvaluator.evaluate(request);
        return new EvaluationResult(
                "fact-check",
                response.isPass(),
                response.getScore(),
                response.getFeedback());
    }

    public record ConceptAnswer(String concept, String answer) {
    }
}
