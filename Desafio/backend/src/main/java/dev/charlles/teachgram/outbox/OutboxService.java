package dev.charlles.teachgram.outbox;

import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OutboxService {

    private final OutboxEventRepository outboxEvents;

    public OutboxService(OutboxEventRepository outboxEvents) {
        this.outboxEvents = outboxEvents;
    }

    @Transactional
    public void record(String aggregateType, UUID aggregateId, String eventType, String payload) {
        outboxEvents.save(new OutboxEvent(aggregateType, aggregateId, eventType, payload));
    }

    @Scheduled(fixedDelayString = "${OUTBOX_PROCESS_DELAY_MS:30000}")
    @Transactional
    public void markPendingAsProcessedForDemo() {
        outboxEvents.findByStatusOrderByCreatedAtAsc("PENDING", PageRequest.of(0, 25))
                .forEach(OutboxEvent::processed);
    }
}

