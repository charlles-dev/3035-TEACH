package dev.charlles.teachgram.posts;

import dev.charlles.teachgram.identity.UserAccount;
import dev.charlles.teachgram.identity.UserAccountRepository;
import dev.charlles.teachgram.media.MediaAsset;
import dev.charlles.teachgram.media.MediaAssetRepository;
import dev.charlles.teachgram.posts.PostDtos.AuthorResponse;
import dev.charlles.teachgram.posts.PostDtos.MediaRequest;
import dev.charlles.teachgram.posts.PostDtos.MediaResponse;
import dev.charlles.teachgram.posts.PostDtos.PostCreateRequest;
import dev.charlles.teachgram.posts.PostDtos.PostResponse;
import dev.charlles.teachgram.posts.PostDtos.PostStats;
import dev.charlles.teachgram.posts.PostDtos.PostUpdateRequest;
import dev.charlles.teachgram.posts.PostDtos.ViewerState;
import dev.charlles.teachgram.profiles.ProfileService;
import dev.charlles.teachgram.profiles.ProfileDtos.ProfileSummary;
import dev.charlles.teachgram.shared.error.ApiException;
import dev.charlles.teachgram.shared.pagination.CursorPage;
import dev.charlles.teachgram.shared.pagination.CursorSupport;
import dev.charlles.teachgram.shared.security.AuthUser;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

    private final PostRepository posts;
    private final MediaAssetRepository mediaAssets;
    private final UserAccountRepository users;
    private final ProfileService profiles;
    private final CursorSupport cursors;

    public PostService(
            PostRepository posts,
            MediaAssetRepository mediaAssets,
            UserAccountRepository users,
            ProfileService profiles,
            CursorSupport cursors
    ) {
        this.posts = posts;
        this.mediaAssets = mediaAssets;
        this.users = users;
        this.profiles = profiles;
        this.cursors = cursors;
    }

    @Transactional
    public PostResponse create(AuthUser user, PostCreateRequest request) {
        UserAccount author = users.findById(user.id()).orElseThrow(() -> ApiException.notFound("User not found."));
        Post post = posts.save(new Post(author, request.title().trim(), blankToNull(request.description()), request.visibility()));
        replaceMedia(post, request.media());
        return toResponse(post, user.id(), false, false);
    }

    @Transactional(readOnly = true)
    public PostResponse get(UUID postId, AuthUser viewer, boolean viewerFollowsAuthor, boolean liked, boolean saved) {
        Post post = posts.findById(postId).orElseThrow(() -> ApiException.notFound("Post not found."));
        if (!post.visibleTo(viewer.id(), viewerFollowsAuthor)) {
            throw ApiException.notFound("Post not found.");
        }
        return toResponse(post, viewer.id(), liked, saved);
    }

    @Transactional
    public PostResponse update(AuthUser user, UUID postId, PostUpdateRequest request) {
        Post post = ownedPost(user.id(), postId);
        post.update(request.title().trim(), blankToNull(request.description()), request.visibility());
        replaceMedia(post, request.media());
        return toResponse(post, user.id(), false, false);
    }

    @Transactional
    public PostResponse changePrivacy(AuthUser user, UUID postId, PostVisibility visibility) {
        Post post = ownedPost(user.id(), postId);
        post.update(post.getTitle(), post.getDescription(), visibility);
        return toResponse(post, user.id(), false, false);
    }

    @Transactional
    public void delete(AuthUser user, UUID postId) {
        ownedPost(user.id(), postId).delete();
    }

    @Transactional(readOnly = true)
    public CursorPage<PostResponse> feed(AuthUser viewer, Set<UUID> followedIds, String cursor, int limit) {
        CursorSupport.Cursor decoded = cursors.decode(cursor);
        Set<UUID> effectiveFollowedIds = followedIds.isEmpty() ? Set.of(viewer.id()) : followedIds;
        PageRequest pageRequest = PageRequest.of(0, boundedLimit(limit) + 1);
        List<Post> found = decoded == null
                ? posts.feedFirstPage(viewer.id(), effectiveFollowedIds, pageRequest)
                : posts.feedAfterCursor(viewer.id(), effectiveFollowedIds, decoded.createdAt(), pageRequest);
        return page(found, viewer.id(), limit);
    }

    @Transactional(readOnly = true)
    public CursorPage<PostResponse> profilePosts(String username, AuthUser viewer, boolean includePrivate, String cursor, int limit) {
        CursorSupport.Cursor decoded = cursors.decode(cursor);
        PageRequest pageRequest = PageRequest.of(0, boundedLimit(limit) + 1);
        List<Post> found = decoded == null
                ? posts.profilePostsFirstPage(username, includePrivate, pageRequest)
                : posts.profilePostsAfterCursor(username, includePrivate, decoded.createdAt(), pageRequest);
        UUID viewerId = viewer == null ? null : viewer.id();
        return page(found, viewerId, limit);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> searchPublic(String term, AuthUser viewer, int limit) {
        return posts.searchPublic(term, PageRequest.of(0, boundedLimit(limit))).stream()
                .map(post -> toResponse(post, viewer == null ? null : viewer.id(), false, false))
                .toList();
    }

    public Post requirePost(UUID postId) {
        return posts.findById(postId).orElseThrow(() -> ApiException.notFound("Post not found."));
    }

    public PostResponse toResponse(Post post, UUID viewerId, boolean liked, boolean saved) {
        ProfileSummary author = profiles.summary(post.getAuthor().getId());
        List<MediaResponse> media = mediaAssets.findByPostIdOrderByCreatedAtAsc(post.getId()).stream()
                .map(asset -> new MediaResponse(asset.getId(), asset.getType(), asset.getUrl(), asset.getAltText()))
                .toList();
        return new PostResponse(
                post.getId(),
                new AuthorResponse(author.id(), author.username(), author.displayName(), author.avatarUrl()),
                post.getTitle(),
                post.getDescription(),
                post.getVisibility(),
                post.getModerationStatus(),
                media,
                new PostStats(post.getLikeCount(), post.getCommentCount(), post.getSaveCount()),
                new ViewerState(liked, saved, viewerId != null && post.ownedBy(viewerId)),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

    private Post ownedPost(UUID userId, UUID postId) {
        Post post = posts.findById(postId).orElseThrow(() -> ApiException.notFound("Post not found."));
        if (post.deleted()) {
            throw ApiException.notFound("Post not found.");
        }
        if (!post.ownedBy(userId)) {
            throw ApiException.forbidden("Only the post owner can perform this action.");
        }
        return post;
    }

    private void replaceMedia(Post post, List<MediaRequest> media) {
        mediaAssets.deleteByPostId(post.getId());
        if (media == null) {
            return;
        }
        for (MediaRequest item : media) {
            validateHttpsUrl(item.url());
            mediaAssets.save(new MediaAsset(post.getId(), item.type(), item.url().trim(), blankToNull(item.altText())));
        }
    }

    private CursorPage<PostResponse> page(List<Post> found, UUID viewerId, int limit) {
        int bounded = boundedLimit(limit);
        boolean hasNext = found.size() > bounded;
        List<Post> visible = hasNext ? found.subList(0, bounded) : found;
        List<PostResponse> items = new ArrayList<>();
        for (Post post : visible) {
            items.add(toResponse(post, viewerId, false, false));
        }
        String next = hasNext && !visible.isEmpty()
                ? cursors.encode(visible.get(visible.size() - 1).getCreatedAt(), visible.get(visible.size() - 1).getId())
                : null;
        return new CursorPage<>(items, next, hasNext);
    }

    private int boundedLimit(int limit) {
        if (limit <= 0) {
            return 20;
        }
        return Math.min(50, limit);
    }

    private void validateHttpsUrl(String value) {
        try {
            URI uri = URI.create(value);
            if (!"https".equalsIgnoreCase(uri.getScheme())) {
                throw ApiException.badRequest("Media URL must use https.");
            }
        } catch (IllegalArgumentException exception) {
            throw ApiException.badRequest("Invalid media URL.");
        }
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
