package id.academy.springailabs.model;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;

public class DemoChatModel implements ChatModel {

    private static final Set<String> IGNORED_WORDS = Set.of(
            "and", "are", "for", "from", "has", "is", "its", "of", "that", "the", "this", "was", "with",
            "yang", "dan", "dari", "di", "ke", "pada", "untuk", "adalah", "dengan");

    @Override
    public ChatResponse call(Prompt prompt) {
        String text = prompt.getInstructions().stream()
                .map(Message::getText)
                .collect(Collectors.joining(System.lineSeparator()));

        String answer;
        if (text.toLowerCase(Locale.ROOT).contains("claim:")) {
            answer = evaluateClaim(text);
        }
        else if (text.toLowerCase(Locale.ROOT).contains("query:")
                && text.toLowerCase(Locale.ROOT).contains("context:")) {
            answer = evaluateRelevance(text);
        }
        else if (text.toLowerCase(Locale.ROOT).contains("json object")) {
            answer = """
                    {"concept":"Spring AI","keyPoints":["Menghubungkan aplikasi Java dengan model AI","Menyediakan API yang konsisten untuk prompt dan respons"]}
                    """;
        }
        else if (text.toLowerCase(Locale.ROOT).contains("two short sentences")) {
            answer = "Spring AI menghubungkan aplikasi Java dengan model bahasa melalui abstraksi Spring. " +
                    "ChatClient membantu menyusun prompt dan mengambil respons dengan API yang ringkas.";
        }
        else {
            answer = "Spring AI menyediakan cara terpadu bagi aplikasi Java untuk mengirim prompt ke model AI " +
                    "dan menggunakan responsnya.";
        }

        return new ChatResponse(List.of(new Generation(new AssistantMessage(answer))));
    }

    private String evaluateClaim(String prompt) {
        String document = section(prompt, "Document:", "Claim:");
        String claim = section(prompt, "Claim:", null);
        return containsAllKeyTerms(document, claim) ? "yes" : "no";
    }

    private String evaluateRelevance(String prompt) {
        String response = section(prompt, "Response:", "Context:");
        String context = section(prompt, "Context:", "Answer:");
        return containsAllKeyTerms(context, response) ? "YES" : "NO";
    }

    private boolean containsAllKeyTerms(String source, String claim) {
        Set<String> sourceWords = words(source);
        List<String> keyTerms = words(claim).stream()
                .filter(word -> word.length() > 2 && !IGNORED_WORDS.contains(word))
                .toList();
        return !keyTerms.isEmpty() && sourceWords.containsAll(keyTerms);
    }

    private Set<String> words(String text) {
        Matcher matcher = Pattern.compile("[\\p{L}\\p{N}]+").matcher(text.toLowerCase(Locale.ROOT));
        Set<String> words = new java.util.HashSet<>();
        while (matcher.find()) {
            words.add(matcher.group());
        }
        return words;
    }

    private String section(String prompt, String startMarker, String endMarker) {
        int start = prompt.indexOf(startMarker);
        if (start < 0) {
            return "";
        }
        start += startMarker.length();
        int end = endMarker == null ? -1 : prompt.indexOf(endMarker, start);
        return prompt.substring(start, end < 0 ? prompt.length() : end).trim();
    }
}
