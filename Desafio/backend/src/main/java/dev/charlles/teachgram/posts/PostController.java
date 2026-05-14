package dev.charlles.teachgram.posts;

import dev.charlles.teachgram.posts.PostDtos.PostCreateRequest;
import dev.charlles.teachgram.posts.PostDtos.PostResponse;
import dev.charlles.teachgram.posts.PostDtos.PostUpdateRequest;
import dev.charlles.teachgram.posts.PostDtos.PrivacyRequest;
import dev.charlles.teachgram.shared.security.CurrentUser;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;
    private final CurrentUser currentUser;

    public PostController(PostService postService, CurrentUser currentUser) {
        this.postService = postService;
        this.currentUser = currentUser;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    PostResponse create(@Valid @RequestBody PostCreateRequest request) {
        return postService.create(currentUser.require(), request);
    }

    @GetMapping("/{postId}")
    PostResponse get(@PathVariable UUID postId) {
        return postService.get(postId, currentUser.require(), false, false, false);
    }

    @PatchMapping("/{postId}")
    PostResponse update(@PathVariable UUID postId, @Valid @RequestBody PostUpdateRequest request) {
        return postService.update(currentUser.require(), postId, request);
    }

    @PatchMapping("/{postId}/privacy")
    PostResponse privacy(@PathVariable UUID postId, @Valid @RequestBody PrivacyRequest request) {
        return postService.changePrivacy(currentUser.require(), postId, request.visibility());
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable UUID postId) {
        postService.delete(currentUser.require(), postId);
    }
}

