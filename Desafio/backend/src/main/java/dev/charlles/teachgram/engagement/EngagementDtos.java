package dev.charlles.teachgram.engagement;

import dev.charlles.teachgram.posts.PostDtos.PostResponse;
import dev.charlles.teachgram.profiles.ProfileDtos.ProfileSummary;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class EngagementDtos {
    private EngagementDtos() {
    }

    public record LikeResponse(boolean liked, int likeCount) {
    }

    public record SaveResponse(boolean saved, int saveCount) {
    }

    public record CommentRequest(@NotBlank @Size(max = 500) String content) {
    }

    public record CommentResponse(UUID id, String content, ProfileSummary author, Instant createdAt) {
    }

    public record CommentsPage(List<CommentResponse> items, String nextCursor, boolean hasNext) {
    }

    public record SavedPostsPage(List<PostResponse> items, String nextCursor, boolean hasNext) {
    }
}

