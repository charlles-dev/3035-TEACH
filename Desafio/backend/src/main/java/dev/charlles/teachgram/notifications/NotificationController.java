package dev.charlles.teachgram.notifications;

import dev.charlles.teachgram.notifications.NotificationDtos.NotificationPage;
import dev.charlles.teachgram.shared.security.CurrentUser;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final CurrentUser currentUser;

    public NotificationController(NotificationService notificationService, CurrentUser currentUser) {
        this.notificationService = notificationService;
        this.currentUser = currentUser;
    }

    @GetMapping
    NotificationPage list(@RequestParam(defaultValue = "20") int limit) {
        return notificationService.list(currentUser.require(), limit);
    }

    @PatchMapping("/{notificationId}/read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void markRead(@PathVariable UUID notificationId) {
        notificationService.markRead(currentUser.require(), notificationId);
    }
}

