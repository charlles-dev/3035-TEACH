package dev.charlles.teachgram.socialgraph;

import dev.charlles.teachgram.identity.UserAccount;
import dev.charlles.teachgram.identity.UserAccountRepository;
import dev.charlles.teachgram.profiles.ProfileDtos.ProfileSummary;
import dev.charlles.teachgram.profiles.ProfileService;
import dev.charlles.teachgram.shared.error.ApiException;
import dev.charlles.teachgram.shared.security.AuthUser;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RelationshipService {

    private final FollowRepository follows;
    private final UserAccountRepository users;
    private final ProfileService profiles;
    private final ApplicationEventPublisher events;

    public RelationshipService(
            FollowRepository follows,
            UserAccountRepository users,
            ProfileService profiles,
            ApplicationEventPublisher events
    ) {
        this.follows = follows;
        this.users = users;
        this.profiles = profiles;
        this.events = events;
    }

    @Transactional
    public RelationshipDtos.FollowResponse follow(AuthUser actor, UUID targetId) {
        if (actor.id().equals(targetId)) {
            throw ApiException.badRequest("You cannot follow yourself.");
        }
        UserAccount follower = users.findById(actor.id()).orElseThrow(() -> ApiException.notFound("User not found."));
        UserAccount followed = users.findById(targetId)
                .filter(UserAccount::isActive)
                .orElseThrow(() -> ApiException.notFound("User not found."));
        if (follows.existsByFollowerIdAndFollowedId(follower.getId(), followed.getId())) {
            return new RelationshipDtos.FollowResponse(true, Instant.now());
        }
        Follow follow = follows.save(new Follow(follower, followed));
        events.publishEvent(new UserFollowedEvent(follower.getId(), followed.getId(), Instant.now()));
        return new RelationshipDtos.FollowResponse(true, follow.getCreatedAt());
    }

    @Transactional
    public void unfollow(AuthUser actor, UUID targetId) {
        follows.deleteByFollowerIdAndFollowedId(actor.id(), targetId);
    }

    @Transactional(readOnly = true)
    public RelationshipDtos.ConnectionsResponse following(AuthUser actor) {
        List<ProfileSummary> items = follows.findByFollowerIdOrderByCreatedAtDesc(actor.id()).stream()
                .filter(follow -> follow.getFollowed().isActive())
                .map(follow -> profiles.summary(follow.getFollowed().getId()))
                .toList();
        return new RelationshipDtos.ConnectionsResponse(items);
    }

    @Transactional(readOnly = true)
    public RelationshipDtos.ConnectionsResponse followers(AuthUser actor) {
        List<ProfileSummary> items = follows.findByFollowedIdOrderByCreatedAtDesc(actor.id()).stream()
                .filter(follow -> follow.getFollower().isActive())
                .map(follow -> profiles.summary(follow.getFollower().getId()))
                .toList();
        return new RelationshipDtos.ConnectionsResponse(items);
    }

    @Transactional(readOnly = true)
    public boolean follows(UUID followerId, UUID followedId) {
        return follows.existsByFollowerIdAndFollowedId(followerId, followedId);
    }

    @Transactional(readOnly = true)
    public Set<UUID> followedIds(UUID followerId) {
        return follows.followedIds(followerId).stream().collect(Collectors.toSet());
    }
}

