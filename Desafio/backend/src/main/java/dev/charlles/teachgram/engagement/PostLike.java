package dev.charlles.teachgram.engagement;

import dev.charlles.teachgram.identity.UserAccount;
import dev.charlles.teachgram.posts.Post;
import dev.charlles.teachgram.shared.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "post_likes")
public class PostLike extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount user;

    protected PostLike() {
    }

    public PostLike(Post post, UserAccount user) {
        this.post = post;
        this.user = user;
    }
}

