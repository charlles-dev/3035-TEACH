package dev.charlles.teachgram.socialgraph;

import dev.charlles.teachgram.profiles.ProfileDtos.ProfileSummary;
import java.time.Instant;
import java.util.List;

public final class RelationshipDtos {
    private RelationshipDtos() {
    }

    public record FollowResponse(boolean following, Instant followedAt) {
    }

    public record ConnectionsResponse(List<ProfileSummary> items) {
    }
}

