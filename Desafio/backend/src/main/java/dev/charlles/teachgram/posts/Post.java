package dev.charlles.teachgram.posts;

import dev.charlles.teachgram.identity.UserAccount;
import dev.charlles.teachgram.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "posts")
public class Post extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private UserAccount author;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(length = 200)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PostVisibility visibility;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ModerationStatus moderationStatus = ModerationStatus.APPROVED;

    @Column(nullable = false)
    private int likeCount;

    @Column(nullable = false)
    private int commentCount;

    @Column(nullable = false)
    private int saveCount;

    private Instant deletedAt;

    protected Post() {
    }

    public Post(UserAccount author, String title, String description, PostVisibility visibility) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.visibility = visibility;
    }

    public void update(String title, String description, PostVisibility visibility) {
        this.title = title;
        this.description = description;
        this.visibility = visibility;
    }

    public void delete() {
        this.deletedAt = Instant.now();
    }

    public boolean deleted() {
        return deletedAt != null;
    }

    public boolean ownedBy(UUID userId) {
        return author.getId().equals(userId);
    }

    public boolean visibleTo(UUID viewerId, boolean viewerFollowsAuthor) {
        if (deleted() || moderationStatus == ModerationStatus.HIDDEN) {
            return false;
        }
        if (author.getId().equals(viewerId)) {
            return true;
        }
        return visibility == PostVisibility.PUBLIC || (visibility == PostVisibility.FOLLOWERS && viewerFollowsAuthor);
    }

    public void incrementLikes() {
        likeCount++;
    }

    public void decrementLikes() {
        likeCount = Math.max(0, likeCount - 1);
    }

    public void incrementComments() {
        commentCount++;
    }

    public void incrementSaves() {
        saveCount++;
    }

    public void decrementSaves() {
        saveCount = Math.max(0, saveCount - 1);
    }

    public UserAccount getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public PostVisibility getVisibility() {
        return visibility;
    }

    public ModerationStatus getModerationStatus() {
        return moderationStatus;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public int getSaveCount() {
        return saveCount;
    }
}

