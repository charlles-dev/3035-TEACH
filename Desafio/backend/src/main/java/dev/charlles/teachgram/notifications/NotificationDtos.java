package dev.charlles.teachgram.notifications;

import dev.charlles.teachgram.profiles.ProfileDtos.ProfileSummary;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class NotificationDtos {
    private NotificationDtos() {
    }

    public record NotificationResponse(
            UUID id,
            NotificationType type,
            String message,
            boolean read,
            String resourceType,
            UUID resourceId,
            ProfileSummary actor,
            Instant createdAt
    ) {
    }

    public record NotificationPage(List<NotificationResponse> items, String nextCursor, boolean hasNext) {
    }
}

