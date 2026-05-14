package dev.charlles.teachgram.audit;

import dev.charlles.teachgram.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "audit_events")
public class AuditEvent extends BaseEntity {

    private UUID actorId;

    @Column(nullable = false, length = 80)
    private String action;

    @Column(length = 40)
    private String resourceType;

    private UUID resourceId;

    @Column(length = 80)
    private String ipAddress;

    @Column(columnDefinition = "text")
    private String userAgent;

    @Column(columnDefinition = "jsonb")
    private String metadata;

    protected AuditEvent() {
    }

    public AuditEvent(UUID actorId, String action, String resourceType, UUID resourceId, String metadata) {
        this.actorId = actorId;
        this.action = action;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.metadata = metadata;
    }
}

