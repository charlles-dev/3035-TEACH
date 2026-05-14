package dev.charlles.teachgram.feed;

import dev.charlles.teachgram.posts.PostDtos.PostResponse;
import dev.charlles.teachgram.posts.PostService;
import dev.charlles.teachgram.shared.pagination.CursorPage;
import dev.charlles.teachgram.shared.security.AuthUser;
import dev.charlles.teachgram.shared.security.CurrentUser;
import dev.charlles.teachgram.socialgraph.RelationshipService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class FeedController {

    private final PostService posts;
    private final RelationshipService relationships;
    private final CurrentUser currentUser;

    public FeedController(PostService posts, RelationshipService relationships, CurrentUser currentUser) {
        this.posts = posts;
        this.relationships = relationships;
        this.currentUser = currentUser;
    }

    @GetMapping("/feed")
    CursorPage<PostResponse> feed(
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "20") int limit
    ) {
        AuthUser viewer = currentUser.require();
        return posts.feed(viewer, relationships.followedIds(viewer.id()), cursor, limit);
    }

    @GetMapping("/users/{username}/posts")
    CursorPage<PostResponse> profilePosts(
            @PathVariable String username,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "20") int limit
    ) {
        AuthUser viewer = currentUser.optional();
        boolean own = viewer != null && viewer.username().equalsIgnoreCase(username);
        return posts.profilePosts(username, viewer, own, cursor, limit);
    }
}

