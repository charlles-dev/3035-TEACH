package dev.charlles.teachgram.search;

import dev.charlles.teachgram.posts.PostService;
import dev.charlles.teachgram.profiles.ProfileDtos.ProfileSummary;
import dev.charlles.teachgram.profiles.UserProfileRepository;
import dev.charlles.teachgram.shared.security.CurrentUser;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

    private final UserProfileRepository profiles;
    private final PostService posts;
    private final CurrentUser currentUser;

    public SearchController(UserProfileRepository profiles, PostService posts, CurrentUser currentUser) {
        this.profiles = profiles;
        this.posts = posts;
        this.currentUser = currentUser;
    }

    @GetMapping
    SearchDtos.SearchResponse search(
            @RequestParam String q,
            @RequestParam(defaultValue = "all") String type,
            @RequestParam(defaultValue = "20") int limit
    ) {
        int bounded = Math.min(Math.max(limit, 1), 50);
        String term = q == null ? "" : q.trim();
        if (term.length() < 2) {
            return new SearchDtos.SearchResponse(List.of(), List.of());
        }
        boolean includeUsers = type.equalsIgnoreCase("all") || type.equalsIgnoreCase("users");
        boolean includePosts = type.equalsIgnoreCase("all") || type.equalsIgnoreCase("posts");
        List<ProfileSummary> users = includeUsers
                ? profiles.search(term, PageRequest.of(0, bounded)).stream()
                        .map(profile -> new ProfileSummary(
                                profile.getUser().getId(),
                                profile.getUser().getUsername(),
                                profile.getDisplayName(),
                                profile.getAvatarUrl(),
                                profile.getBio()
                        ))
                        .toList()
                : List.of();
        return new SearchDtos.SearchResponse(
                users,
                includePosts ? posts.searchPublic(term, currentUser.optional(), bounded) : List.of()
        );
    }

    @GetMapping("/semantic")
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    SearchDtos.SearchResponse semanticUnavailable() {
        return new SearchDtos.SearchResponse(List.of(), List.of());
    }
}

