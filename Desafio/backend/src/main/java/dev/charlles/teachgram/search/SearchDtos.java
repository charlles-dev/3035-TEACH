package dev.charlles.teachgram.search;

import dev.charlles.teachgram.posts.PostDtos.PostResponse;
import dev.charlles.teachgram.profiles.ProfileDtos.ProfileSummary;
import java.util.List;

public final class SearchDtos {
    private SearchDtos() {
    }

    public record SearchResponse(List<ProfileSummary> users, List<PostResponse> posts) {
    }
}

