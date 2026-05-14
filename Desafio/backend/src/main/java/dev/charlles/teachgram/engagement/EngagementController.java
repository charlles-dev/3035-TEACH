package dev.charlles.teachgram.engagement;

import dev.charlles.teachgram.engagement.EngagementDtos.CommentRequest;
import dev.charlles.teachgram.engagement.EngagementDtos.CommentResponse;
import dev.charlles.teachgram.engagement.EngagementDtos.CommentsPage;
import dev.charlles.teachgram.engagement.EngagementDtos.LikeResponse;
import dev.charlles.teachgram.engagement.EngagementDtos.SaveResponse;
import dev.charlles.teachgram.engagement.EngagementDtos.SavedPostsPage;
import dev.charlles.teachgram.shared.security.CurrentUser;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class EngagementController {

    private final EngagementService engagement;
    private final CurrentUser currentUser;

    public EngagementController(EngagementService engagement, CurrentUser currentUser) {
        this.engagement = engagement;
        this.currentUser = currentUser;
    }

    @PostMapping("/posts/{postId}/likes")
    LikeResponse like(@PathVariable UUID postId) {
        return engagement.like(currentUser.require(), postId);
    }

    @DeleteMapping("/posts/{postId}/likes")
    LikeResponse unlike(@PathVariable UUID postId) {
        return engagement.unlike(currentUser.require(), postId);
    }

    @PostMapping("/posts/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    CommentResponse comment(@PathVariable UUID postId, @Valid @RequestBody CommentRequest request) {
        return engagement.comment(currentUser.require(), postId, request);
    }

    @GetMapping("/posts/{postId}/comments")
    CommentsPage comments(@PathVariable UUID postId, @RequestParam(defaultValue = "20") int limit) {
        return engagement.comments(postId, limit);
    }

    @PostMapping("/posts/{postId}/saves")
    SaveResponse save(@PathVariable UUID postId) {
        return engagement.save(currentUser.require(), postId);
    }

    @DeleteMapping("/posts/{postId}/saves")
    SaveResponse unsave(@PathVariable UUID postId) {
        return engagement.unsave(currentUser.require(), postId);
    }

    @GetMapping("/saved")
    SavedPostsPage saved(@RequestParam(defaultValue = "20") int limit) {
        return engagement.saved(currentUser.require(), limit);
    }
}

