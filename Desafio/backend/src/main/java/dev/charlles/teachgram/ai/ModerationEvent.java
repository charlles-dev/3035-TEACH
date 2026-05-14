package dev.charlles.teachgram.ai;

import dev.charlles.teachgram.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "moderation_events")
public class ModerationEvent extends BaseEntity {

    @Column(nullable = false, length = 40)
    private String resourceType;

    @Column(nullable = false)
    private UUID resourceId;

    @Column(nullable = false, length = 50)
    private String provider;

    @Column(length = 120)
    private String model;

    @Column(nullable = false, length = 40)
    private String status;

    @Column(columnDefinition = "jsonb")
    private String categories;

    protected ModerationEvent() {
    }

    public ModerationEvent(String resourceType, UUID resourceId, String provider, String model, String status, String categories) {
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.provider = provider;
        this.model = model;
        this.status = status;
        this.categories = categories;
    }
}

