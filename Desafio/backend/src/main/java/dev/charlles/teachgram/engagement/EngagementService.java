package dev.charlles.teachgram.engagement;

import dev.charlles.teachgram.engagement.EngagementDtos.CommentRequest;
import dev.charlles.teachgram.engagement.EngagementDtos.CommentResponse;
import dev.charlles.teachgram.engagement.EngagementDtos.CommentsPage;
import dev.charlles.teachgram.engagement.EngagementDtos.LikeResponse;
import dev.charlles.teachgram.engagement.EngagementDtos.SaveResponse;
import dev.charlles.teachgram.engagement.EngagementDtos.SavedPostsPage;
import dev.charlles.teachgram.identity.UserAccount;
import dev.charlles.teachgram.identity.UserAccountRepository;
import dev.charlles.teachgram.posts.Post;
import dev.charlles.teachgram.posts.PostDtos.PostResponse;
import dev.charlles.teachgram.posts.PostService;
import dev.charlles.teachgram.profiles.ProfileService;
import dev.charlles.teachgram.shared.error.ApiException;
import dev.charlles.teachgram.shared.security.AuthUser;
import dev.charlles.teachgram.socialgraph.RelationshipService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EngagementService {

    private final PostLikeRepository likes;
    private final PostCommentRepository comments;
    private final SavedPostRepository saves;
    private final UserAccountRepository users;
    private final PostService posts;
    private final ProfileService profiles;
    private final RelationshipService relationships;
    private final ApplicationEventPublisher events;

    public EngagementService(
            PostLikeRepository likes,
            PostCommentRepository comments,
            SavedPostRepository saves,
            UserAccountRepository users,
            PostService posts,
            ProfileService profiles,
            RelationshipService relationships,
            ApplicationEventPublisher events
    ) {
        this.likes = likes;
        this.comments = comments;
        this.saves = saves;
        this.users = users;
        this.posts = posts;
        this.profiles = profiles;
        this.relationships = relationships;
        this.events = events;
    }

    @Transactional
    public LikeResponse like(AuthUser actor, UUID postId) {
        Post post = visiblePost(actor, postId);
        if (likes.existsByPostIdAndUserId(postId, actor.id())) {
            return new LikeResponse(true, post.getLikeCount());
        }
        UserAccount user = users.findById(actor.id()).orElseThrow(() -> ApiException.notFound("User not found."));
        likes.save(new PostLike(post, user));
        post.incrementLikes();
        events.publishEvent(new PostLikedEvent(post.getId(), post.getAuthor().getId(), actor.id(), Instant.now()));
        return new LikeResponse(true, post.getLikeCount());
    }

    @Transactional
    public LikeResponse unlike(AuthUser actor, UUID postId) {
        Post post = visiblePost(actor, postId);
        likes.findByPostIdAndUserId(postId, actor.id()).ifPresent(like -> {
            likes.delete(like);
            post.decrementLikes();
        });
        return new LikeResponse(false, post.getLikeCount());
    }

    @Transactional
    public CommentResponse comment(AuthUser actor, UUID postId, CommentRequest request) {
        Post post = visiblePost(actor, postId);
        UserAccount user = users.findById(actor.id()).orElseThrow(() -> ApiException.notFound("User not found."));
        PostComment comment = comments.save(new PostComment(post, user, request.content().trim()));
        post.incrementComments();
        events.publishEvent(new PostCommentedEvent(post.getId(), post.getAuthor().getId(), actor.id(), comment.getId(), Instant.now()));
        return toComment(comment);
    }

    @Transactional(readOnly = true)
    public CommentsPage comments(UUID postId, int limit) {
        List<CommentResponse> items = comments
                .findByPostIdAndDeletedAtIsNullOrderByCreatedAtAsc(postId, PageRequest.of(0, Math.min(Math.max(limit, 1), 50)))
                .stream()
                .map(this::toComment)
                .toList();
        return new CommentsPage(items, null, false);
    }

    @Transactional
    public SaveResponse save(AuthUser actor, UUID postId) {
        Post post = visiblePost(actor, postId);
        if (saves.existsByPostIdAndUserId(postId, actor.id())) {
            return new SaveResponse(true, post.getSaveCount());
        }
        UserAccount user = users.findById(actor.id()).orElseThrow(() -> ApiException.notFound("User not found."));
        saves.save(new SavedPost(post, user));
        post.incrementSaves();
        return new SaveResponse(true, post.getSaveCount());
    }

    @Transactional
    public SaveResponse unsave(AuthUser actor, UUID postId) {
        Post post = visiblePost(actor, postId);
        saves.findByPostIdAndUserId(postId, actor.id()).ifPresent(save -> {
            saves.delete(save);
            post.decrementSaves();
        });
        return new SaveResponse(false, post.getSaveCount());
    }

    @Transactional(readOnly = true)
    public SavedPostsPage saved(AuthUser actor, int limit) {
        List<PostResponse> items = saves.findByUserIdOrderByCreatedAtDesc(actor.id(), PageRequest.of(0, Math.min(Math.max(limit, 1), 50)))
                .stream()
                .map(SavedPost::getPost)
                .filter(post -> post.visibleTo(actor.id(), relationships.follows(actor.id(), post.getAuthor().getId())))
                .map(post -> posts.toResponse(post, actor.id(), liked(actor.id(), post.getId()), true))
                .toList();
        return new SavedPostsPage(items, null, false);
    }

    @Transactional(readOnly = true)
    public boolean liked(UUID userId, UUID postId) {
        return likes.existsByPostIdAndUserId(postId, userId);
    }

    @Transactional(readOnly = true)
    public boolean saved(UUID userId, UUID postId) {
        return saves.existsByPostIdAndUserId(postId, userId);
    }

    private Post visiblePost(AuthUser actor, UUID postId) {
        Post post = posts.requirePost(postId);
        boolean followsAuthor = relationships.follows(actor.id(), post.getAuthor().getId());
        if (!post.visibleTo(actor.id(), followsAuthor)) {
            throw ApiException.notFound("Post not found.");
        }
        return post;
    }

    private CommentResponse toComment(PostComment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                profiles.summary(comment.getAuthor().getId()),
                comment.getCreatedAt()
        );
    }
}
