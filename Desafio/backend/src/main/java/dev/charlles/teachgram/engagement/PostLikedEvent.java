package dev.charlles.teachgram.engagement;

import java.time.Instant;
import java.util.UUID;

public record PostLikedEvent(UUID postId, UUID postAuthorId, UUID actorId, Instant occurredAt) {
}

