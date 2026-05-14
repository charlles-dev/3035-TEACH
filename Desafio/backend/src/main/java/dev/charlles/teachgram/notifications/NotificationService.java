package dev.charlles.teachgram.notifications;

import dev.charlles.teachgram.engagement.PostCommentedEvent;
import dev.charlles.teachgram.engagement.PostLikedEvent;
import dev.charlles.teachgram.identity.UserAccount;
import dev.charlles.teachgram.identity.UserAccountRepository;
import dev.charlles.teachgram.notifications.NotificationDtos.NotificationPage;
import dev.charlles.teachgram.notifications.NotificationDtos.NotificationResponse;
import dev.charlles.teachgram.profiles.ProfileDtos.ProfileSummary;
import dev.charlles.teachgram.profiles.ProfileService;
import dev.charlles.teachgram.shared.error.ApiException;
import dev.charlles.teachgram.shared.security.AuthUser;
import dev.charlles.teachgram.socialgraph.UserFollowedEvent;
import java.util.List;
import java.util.UUID;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

    private final NotificationRepository notifications;
    private final UserAccountRepository users;
    private final ProfileService profiles;

    public NotificationService(NotificationRepository notifications, UserAccountRepository users, ProfileService profiles) {
        this.notifications = notifications;
        this.users = users;
        this.profiles = profiles;
    }

    @EventListener
    @Transactional
    public void onFollow(UserFollowedEvent event) {
        if (event.followerId().equals(event.followedId())) {
            return;
        }
        UserAccount recipient = users.findById(event.followedId()).orElseThrow();
        UserAccount actor = users.findById(event.followerId()).orElseThrow();
        notifications.save(new Notification(
                recipient,
                actor,
                NotificationType.FOLLOW,
                "PROFILE",
                actor.getId(),
                "@" + actor.getUsername() + " comecou a seguir voce."
        ));
    }

    @EventListener
    @Transactional
    public void onLike(PostLikedEvent event) {
        if (event.actorId().equals(event.postAuthorId())) {
            return;
        }
        UserAccount recipient = users.findById(event.postAuthorId()).orElseThrow();
        UserAccount actor = users.findById(event.actorId()).orElseThrow();
        notifications.save(new Notification(
                recipient,
                actor,
                NotificationType.LIKE,
                "POST",
                event.postId(),
                "@" + actor.getUsername() + " curtiu sua publicacao."
        ));
    }

    @EventListener
    @Transactional
    public void onComment(PostCommentedEvent event) {
        if (event.actorId().equals(event.postAuthorId())) {
            return;
        }
        UserAccount recipient = users.findById(event.postAuthorId()).orElseThrow();
        UserAccount actor = users.findById(event.actorId()).orElseThrow();
        notifications.save(new Notification(
                recipient,
                actor,
                NotificationType.COMMENT,
                "POST",
                event.postId(),
                "@" + actor.getUsername() + " comentou no seu post."
        ));
    }

    @Transactional(readOnly = true)
    public NotificationPage list(AuthUser user, int limit) {
        int bounded = Math.min(Math.max(limit, 1), 50);
        List<NotificationResponse> items = notifications
                .findByRecipientIdOrderByCreatedAtDesc(user.id(), PageRequest.of(0, bounded))
                .stream()
                .map(this::toResponse)
                .toList();
        return new NotificationPage(items, null, false);
    }

    @Transactional
    public void markRead(AuthUser user, UUID notificationId) {
        Notification notification = notifications.findById(notificationId)
                .orElseThrow(() -> ApiException.notFound("Notification not found."));
        notification.markRead();
    }

    private NotificationResponse toResponse(Notification notification) {
        ProfileSummary actor = notification.getActor() == null ? null : profiles.summary(notification.getActor().getId());
        return new NotificationResponse(
                notification.getId(),
                notification.getType(),
                notification.getMessage(),
                notification.isRead(),
                notification.getResourceType(),
                notification.getResourceId(),
                actor,
                notification.getCreatedAt()
        );
    }
}

