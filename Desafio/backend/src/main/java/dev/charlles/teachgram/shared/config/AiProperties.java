package dev.charlles.teachgram.shared.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.ai")
public record AiProperties(
        boolean enabled,
        String provider,
        boolean fallbackEnabled,
        int timeoutMs,
        Groq groq
) {
    public record Groq(
            String apiKey,
            String baseUrl,
            String textModel,
            String fastModel,
            String modelVersion
    ) {
    }
}
