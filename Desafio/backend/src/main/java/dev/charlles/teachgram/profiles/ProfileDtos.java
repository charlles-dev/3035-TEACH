package dev.charlles.teachgram.profiles;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public final class ProfileDtos {
    private ProfileDtos() {
    }

    public record MeResponse(
            UUID id,
            String username,
            String email,
            String displayName,
            String phone,
            String bio,
            String avatarUrl
    ) {
    }

    public record ProfileUpdateRequest(
            @Size(max = 120) String displayName,
            @Pattern(regexp = "^[a-zA-Z0-9._]{3,40}$") String username,
            @Email String email,
            @Size(max = 30) String phone,
            @Size(max = 500) String bio,
            String avatarUrl
    ) {
    }

    public record ProfileSummary(
            UUID id,
            String username,
            String displayName,
            String avatarUrl,
            String bio
    ) {
    }

    public record PublicProfileResponse(
            UUID id,
            String username,
            String displayName,
            String bio,
            String avatarUrl,
            ProfileStats stats,
            ViewerState viewerState
    ) {
    }

    public record ProfileStats(long posts, long followers, long following) {
    }

    public record ViewerState(boolean following, boolean ownProfile) {
    }
}

