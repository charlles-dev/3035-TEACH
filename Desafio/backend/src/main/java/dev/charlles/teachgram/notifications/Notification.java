package dev.charlles.teachgram.notifications;

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
@Table(name = "notifications")
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_id", nullable = false)
    private UserAccount recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id")
    private UserAccount actor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private NotificationType type;

    @Column(length = 40)
    private String resourceType;

    private UUID resourceId;

    @Column(nullable = false, length = 255)
    private String message;

    private Instant readAt;

    protected Notification() {
    }

    public Notification(UserAccount recipient, UserAccount actor, NotificationType type,
                        String resourceType, UUID resourceId, String message) {
        this.recipient = recipient;
        this.actor = actor;
        this.type = type;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.message = message;
    }

    public void markRead() {
        this.readAt = Instant.now();
    }

    public UserAccount getActor() {
        return actor;
    }

    public NotificationType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getResourceType() {
        return resourceType;
    }

    public UUID getResourceId() {
        return resourceId;
    }

    public boolean isRead() {
        return readAt != null;
    }
}

