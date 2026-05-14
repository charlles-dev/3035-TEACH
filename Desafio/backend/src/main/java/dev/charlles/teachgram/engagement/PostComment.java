package dev.charlles.teachgram.engagement;

import dev.charlles.teachgram.identity.UserAccount;
import dev.charlles.teachgram.posts.Post;
import dev.charlles.teachgram.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "post_comments")
public class PostComment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private UserAccount author;

    @Column(nullable = false, length = 500)
    private String content;

    private Instant deletedAt;

    protected PostComment() {
    }

    public PostComment(Post post, UserAccount author, String content) {
        this.post = post;
        this.author = author;
        this.content = content;
    }

    public UserAccount getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}

