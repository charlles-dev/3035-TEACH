package dev.charlles.teachgram.socialgraph;

import dev.charlles.teachgram.identity.UserAccount;
import dev.charlles.teachgram.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "follows")
public class Follow extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "follower_id", nullable = false)
    private UserAccount follower;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "followed_id", nullable = false)
    private UserAccount followed;

    @Column(nullable = false, length = 30)
    private String status = "ACTIVE";

    protected Follow() {
    }

    public Follow(UserAccount follower, UserAccount followed) {
        this.follower = follower;
        this.followed = followed;
    }

    public UserAccount getFollower() {
        return follower;
    }

    public UserAccount getFollowed() {
        return followed;
    }
}

