package dev.charlles.teachgram.posts;

import dev.charlles.teachgram.media.MediaType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class PostDtos {
    private PostDtos() {
    }

    public record MediaRequest(
            @NotNull MediaType type,
            @NotBlank String url,
            @Size(max = 255) String altText
    ) {
    }

    public record PostCreateRequest(
            @NotBlank @Size(max = 50) String title,
            @Size(max = 200) String description,
            @NotNull PostVisibility visibility,
            @Size(max = 4) List<@Valid MediaRequest> media
    ) {
    }

    public record PostUpdateRequest(
            @NotBlank @Size(max = 50) String title,
            @Size(max = 200) String description,
            @NotNull PostVisibility visibility,
            @Size(max = 4) List<@Valid MediaRequest> media
    ) {
    }

    public record PrivacyRequest(@NotNull PostVisibility visibility) {
    }

    public record AuthorResponse(UUID id, String username, String displayName, String avatarUrl) {
    }

    public record MediaResponse(UUID id, MediaType type, String url, String altText) {
    }

    public record PostStats(int likes, int comments, int saves) {
    }

    public record ViewerState(boolean liked, boolean saved, boolean owner) {
    }

    public record PostResponse(
            UUID id,
            AuthorResponse author,
            String title,
            String description,
            PostVisibility visibility,
            ModerationStatus moderationStatus,
            List<MediaResponse> media,
            PostStats stats,
            ViewerState viewerState,
            Instant createdAt,
            Instant updatedAt
    ) {
    }
}

