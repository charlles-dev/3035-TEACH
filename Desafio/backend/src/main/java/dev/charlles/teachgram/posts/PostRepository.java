package dev.charlles.teachgram.posts;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, UUID> {

    @Query("""
            select p from Post p
            where p.deletedAt is null
              and p.moderationStatus <> dev.charlles.teachgram.posts.ModerationStatus.HIDDEN
              and (
                p.visibility = dev.charlles.teachgram.posts.PostVisibility.PUBLIC
                or p.author.id = :viewerId
                or (p.visibility = dev.charlles.teachgram.posts.PostVisibility.FOLLOWERS and p.author.id in :followedIds)
              )
            order by p.createdAt desc
            """)
    List<Post> feedFirstPage(
            @Param("viewerId") UUID viewerId,
            @Param("followedIds") Collection<UUID> followedIds,
            Pageable pageable
    );

    @Query("""
            select p from Post p
            where p.deletedAt is null
              and p.moderationStatus <> dev.charlles.teachgram.posts.ModerationStatus.HIDDEN
              and p.createdAt < :cursorCreatedAt
              and (
                p.visibility = dev.charlles.teachgram.posts.PostVisibility.PUBLIC
                or p.author.id = :viewerId
                or (p.visibility = dev.charlles.teachgram.posts.PostVisibility.FOLLOWERS and p.author.id in :followedIds)
              )
            order by p.createdAt desc
            """)
    List<Post> feedAfterCursor(
            @Param("viewerId") UUID viewerId,
            @Param("followedIds") Collection<UUID> followedIds,
            @Param("cursorCreatedAt") Instant cursorCreatedAt,
            Pageable pageable
    );

    @Query("""
            select p from Post p
            where p.deletedAt is null
              and p.author.username = :username
              and (:includePrivate = true or p.visibility = dev.charlles.teachgram.posts.PostVisibility.PUBLIC)
            order by p.createdAt desc
            """)
    List<Post> profilePostsFirstPage(
            @Param("username") String username,
            @Param("includePrivate") boolean includePrivate,
            Pageable pageable
    );

    @Query("""
            select p from Post p
            where p.deletedAt is null
              and p.author.username = :username
              and (:includePrivate = true or p.visibility = dev.charlles.teachgram.posts.PostVisibility.PUBLIC)
              and p.createdAt < :cursorCreatedAt
            order by p.createdAt desc
            """)
    List<Post> profilePostsAfterCursor(
            @Param("username") String username,
            @Param("includePrivate") boolean includePrivate,
            @Param("cursorCreatedAt") Instant cursorCreatedAt,
            Pageable pageable
    );

    @Query("""
            select p from Post p
            where p.deletedAt is null
              and p.moderationStatus <> dev.charlles.teachgram.posts.ModerationStatus.HIDDEN
              and p.visibility = dev.charlles.teachgram.posts.PostVisibility.PUBLIC
              and (lower(p.title) like lower(concat('%', :term, '%'))
                or lower(coalesce(p.description, '')) like lower(concat('%', :term, '%')))
            order by p.createdAt desc
            """)
    List<Post> searchPublic(@Param("term") String term, Pageable pageable);

    long countByAuthorIdAndDeletedAtIsNull(UUID authorId);
}
