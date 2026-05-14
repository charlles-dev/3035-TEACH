package dev.charlles.teachgram.ai;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public final class AiDtos {
    private AiDtos() {
    }

    public record CaptionRequest(
            @NotBlank @Size(max = 200) String topic,
            @Size(max = 40) String tone,
            @Size(max = 10) String language
    ) {
    }

    public record CaptionResponse(String provider, String model, boolean fallbackUsed, List<String> suggestions) {
    }

    public record HashtagRequest(@NotBlank @Size(max = 50) String title, @Size(max = 200) String description) {
    }

    public record HashtagResponse(String provider, boolean fallbackUsed, List<String> hashtags) {
    }

    public record BioRequest(List<String> keywords, @Size(max = 40) String tone, @Size(max = 500) String currentBio) {
    }

    public record BioResponse(String provider, boolean fallbackUsed, List<String> suggestions) {
    }

    public record ModerationRequest(@NotBlank String resourceType, UUID resourceId, @NotBlank @Size(max = 800) String text) {
    }

    public record ModerationResponse(String status, List<String> categories, String provider, String model) {
    }
}

