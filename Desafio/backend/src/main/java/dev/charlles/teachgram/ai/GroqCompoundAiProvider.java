package dev.charlles.teachgram.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.charlles.teachgram.ai.AiDtos.BioRequest;
import dev.charlles.teachgram.ai.AiDtos.BioResponse;
import dev.charlles.teachgram.ai.AiDtos.CaptionRequest;
import dev.charlles.teachgram.ai.AiDtos.CaptionResponse;
import dev.charlles.teachgram.ai.AiDtos.HashtagRequest;
import dev.charlles.teachgram.ai.AiDtos.HashtagResponse;
import dev.charlles.teachgram.ai.AiDtos.ModerationRequest;
import dev.charlles.teachgram.ai.AiDtos.ModerationResponse;
import dev.charlles.teachgram.shared.config.AiProperties;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GroqCompoundAiProvider implements AiProvider {

    private static final String PROVIDER = "GROQ_COMPOUND";

    private final AiProperties properties;
    private final ObjectMapper objectMapper;
    private final RestClient restClient;

    public GroqCompoundAiProvider(AiProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofMillis(properties.timeoutMs()));
        requestFactory.setReadTimeout(Duration.ofMillis(properties.timeoutMs()));
        this.restClient = RestClient.builder()
                .baseUrl(properties.groq() == null || blank(properties.groq().baseUrl())
                        ? "https://api.groq.com/openai/v1"
                        : properties.groq().baseUrl())
                .requestFactory(requestFactory)
                .build();
    }

    @Override
    public CaptionResponse caption(CaptionRequest request) {
        ensureConfigured();
        String model = textModel();
        String content = chat(model, """
                Voce e o assistente de conteudo do TeachGram Pro.
                Gere legendas curtas, naturais e em portugues do Brasil.
                Responda apenas JSON no formato {"suggestions":["..."]}.
                """, "Tema: %s\nTom: %s\nIdioma: %s".formatted(
                request.topic(), nullToDefault(request.tone(), "natural"), nullToDefault(request.language(), "pt-BR")
        ));
        return new CaptionResponse(PROVIDER, model, false, parseStringList(content, "suggestions", 3));
    }

    @Override
    public HashtagResponse hashtags(HashtagRequest request) {
        ensureConfigured();
        String model = fastModel();
        String content = chat(model, """
                Gere hashtags para uma rede social de portfolio.
                Use termos especificos, sem acentos, sem espacos e sem frases longas.
                Responda apenas JSON no formato {"hashtags":["#Exemplo"]}.
                """, "Titulo: %s\nDescricao: %s".formatted(request.title(), nullToDefault(request.description(), "")));
        return new HashtagResponse(PROVIDER, false, parseStringList(content, "hashtags", 8));
    }

    @Override
    public BioResponse bio(BioRequest request) {
        ensureConfigured();
        String model = textModel();
        String content = chat(model, """
                Reescreva bios curtas para perfil do TeachGram Pro.
                Mantenha tom profissional, humano e direto.
                Responda apenas JSON no formato {"suggestions":["..."]}.
                """, "Bio atual: %s\nTom: %s\nPalavras-chave: %s".formatted(
                nullToDefault(request.currentBio(), ""), nullToDefault(request.tone(), "profissional"), request.keywords()
        ));
        return new BioResponse(PROVIDER, false, parseStringList(content, "suggestions", 3));
    }

    @Override
    public ModerationResponse moderate(ModerationRequest request) {
        ensureConfigured();
        String model = fastModel();
        String content = chat(model, """
                Classifique o texto para moderacao de uma rede social.
                Status permitido: SAFE, REVIEW_REQUIRED ou HIDDEN.
                Categorias devem ser strings curtas, como HATE, VIOLENCE, SPAM, SELF_HARM ou SEXUAL.
                Responda apenas JSON no formato {"status":"SAFE","categories":[]}.
                """, request.text());
        JsonNode root = parseJsonContent(content);
        String status = root.path("status").asText("REVIEW_REQUIRED");
        List<String> categories = jsonArrayToStrings(root.path("categories"), 5);
        return new ModerationResponse(status, categories, PROVIDER, model);
    }

    private String chat(String model, String systemPrompt, String userPrompt) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model", model);
        body.put("messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userPrompt)
        ));
        body.put("temperature", 0.4);
        body.put("max_completion_tokens", 700);
        body.put("response_format", Map.of("type", "json_object"));

        String raw = restClient.post()
                .uri("/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headers -> {
                    headers.setBearerAuth(properties.groq().apiKey());
                    if (!blank(properties.groq().modelVersion())) {
                        headers.set("Groq-Model-Version", properties.groq().modelVersion());
                    }
                })
                .body(body)
                .retrieve()
                .body(String.class);
        try {
            return objectMapper.readTree(raw).path("choices").path(0).path("message").path("content").asText();
        } catch (Exception exception) {
            throw new IllegalStateException("Could not parse Groq chat completion response.", exception);
        }
    }

    private List<String> parseStringList(String content, String field, int limit) {
        JsonNode node = parseJsonContent(content);
        JsonNode values = node.has(field) ? node.get(field) : node;
        List<String> parsed = jsonArrayToStrings(values, limit);
        if (parsed.isEmpty() && node.isTextual()) {
            parsed = List.of(node.asText());
        }
        return parsed;
    }

    private JsonNode parseJsonContent(String content) {
        try {
            return objectMapper.readTree(content);
        } catch (Exception exception) {
            throw new IllegalStateException("Groq response did not contain valid JSON content.", exception);
        }
    }

    private List<String> jsonArrayToStrings(JsonNode node, int limit) {
        List<String> values = new ArrayList<>();
        if (node == null || !node.isArray()) {
            return values;
        }
        for (JsonNode item : node) {
            String value = item.asText(null);
            if (!blank(value)) {
                values.add(value.trim());
            }
            if (values.size() >= limit) {
                break;
            }
        }
        return values;
    }

    private String textModel() {
        return blank(properties.groq().textModel()) ? "groq/compound" : properties.groq().textModel();
    }

    private String fastModel() {
        return blank(properties.groq().fastModel()) ? "groq/compound-mini" : properties.groq().fastModel();
    }

    private void ensureConfigured() {
        if (!properties.enabled()
                || !"groq".equalsIgnoreCase(properties.provider())
                || properties.groq() == null
                || blank(properties.groq().apiKey())) {
            throw new IllegalStateException("Groq provider is not configured.");
        }
    }

    private static boolean blank(String value) {
        return value == null || value.isBlank();
    }

    private static String nullToDefault(String value, String fallback) {
        return blank(value) ? fallback : value;
    }
}
