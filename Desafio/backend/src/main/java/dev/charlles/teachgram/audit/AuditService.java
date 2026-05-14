package dev.charlles.teachgram.audit;

import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {

    private final AuditEventRepository auditEvents;

    public AuditService(AuditEventRepository auditEvents) {
        this.auditEvents = auditEvents;
    }

    @Transactional
    public void record(UUID actorId, String action, String resourceType, UUID resourceId) {
        auditEvents.save(new AuditEvent(actorId, action, resourceType, resourceId, "{}"));
    }
}

