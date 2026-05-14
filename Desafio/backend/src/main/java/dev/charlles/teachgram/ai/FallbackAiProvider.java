package dev.charlles.teachgram.ai;

import dev.charlles.teachgram.ai.AiDtos.BioRequest;
import dev.charlles.teachgram.ai.AiDtos.BioResponse;
import dev.charlles.teachgram.ai.AiDtos.CaptionRequest;
import dev.charlles.teachgram.ai.AiDtos.CaptionResponse;
import dev.charlles.teachgram.ai.AiDtos.HashtagRequest;
import dev.charlles.teachgram.ai.AiDtos.HashtagResponse;
import dev.charlles.teachgram.ai.AiDtos.ModerationRequest;
import dev.charlles.teachgram.ai.AiDtos.ModerationResponse;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FallbackAiProvider implements AiProvider {

    @Override
    public CaptionResponse caption(CaptionRequest request) {
        String base = request.topic() == null || request.topic().isBlank()
                ? "Compartilhando um novo momento no TeachGram."
                : "Compartilhando: " + trim(request.topic(), 160);
        return new CaptionResponse("FALLBACK", null, true, List.of(base));
    }

    @Override
    public HashtagResponse hashtags(HashtagRequest request) {
        List<String> tags = Arrays.stream((request.title() + " " + nullToEmpty(request.description())).split("\\s+"))
                .map(this::clean)
                .filter(word -> word.length() >= 4)
                .distinct()
                .limit(5)
                .map(word -> "#" + Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .toList();
        if (tags.isEmpty()) {
            tags = List.of("#TeachGram", "#Projeto", "#Fullstack");
        }
        return new HashtagResponse("FALLBACK", true, tags);
    }

    @Override
    public BioResponse bio(BioRequest request) {
        return new BioResponse("FALLBACK", true,
                List.of("Criando, aprendendo e compartilhando projetos no TeachGram."));
    }

    @Override
    public ModerationResponse moderate(ModerationRequest request) {
        String text = request.text().toLowerCase();
        if (text.contains("odio") || text.contains("violencia")) {
            return new ModerationResponse("REVIEW_REQUIRED", List.of("LOCAL_SENSITIVE_TERM"), "FALLBACK", null);
        }
        return new ModerationResponse("SAFE", List.of(), "FALLBACK", null);
    }

    private String clean(String value) {
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return normalized.replaceAll("[^a-zA-Z0-9]", "");
    }

    private static String trim(String value, int max) {
        return value.length() <= max ? value : value.substring(0, max);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}

