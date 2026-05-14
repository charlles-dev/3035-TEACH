package dev.charlles.teachgram.socialgraph;

import dev.charlles.teachgram.shared.security.CurrentUser;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/relationships")
public class RelationshipController {

    private final RelationshipService relationships;
    private final CurrentUser currentUser;

    public RelationshipController(RelationshipService relationships, CurrentUser currentUser) {
        this.relationships = relationships;
        this.currentUser = currentUser;
    }

    @PostMapping("/{userId}/follow")
    @ResponseStatus(HttpStatus.CREATED)
    RelationshipDtos.FollowResponse follow(@PathVariable UUID userId) {
        return relationships.follow(currentUser.require(), userId);
    }

    @DeleteMapping("/{userId}/follow")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void unfollow(@PathVariable UUID userId) {
        relationships.unfollow(currentUser.require(), userId);
    }

    @GetMapping("/me/following")
    RelationshipDtos.ConnectionsResponse following() {
        return relationships.following(currentUser.require());
    }

    @GetMapping("/me/followers")
    RelationshipDtos.ConnectionsResponse followers() {
        return relationships.followers(currentUser.require());
    }
}

