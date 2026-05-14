package dev.charlles.teachgram.identity;

import java.time.Instant;
import java.util.UUID;

public record UserRegisteredEvent(
        UUID userId,
        String displayName,
        String phone,
        String bio,
        String avatarUrl,
        Instant occurredAt
) {
}

