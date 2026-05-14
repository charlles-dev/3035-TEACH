package dev.charlles.teachgram.engagement;

import java.time.Instant;
import java.util.UUID;

public record PostCommentedEvent(UUID postId, UUID postAuthorId, UUID actorId, UUID commentId, Instant occurredAt) {
}

