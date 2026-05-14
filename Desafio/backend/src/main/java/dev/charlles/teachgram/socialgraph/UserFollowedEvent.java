package dev.charlles.teachgram.socialgraph;

import java.time.Instant;
import java.util.UUID;

public record UserFollowedEvent(UUID followerId, UUID followedId, Instant occurredAt) {
}

