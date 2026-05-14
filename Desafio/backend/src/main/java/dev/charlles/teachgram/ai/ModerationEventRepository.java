package dev.charlles.teachgram.ai;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModerationEventRepository extends JpaRepository<ModerationEvent, UUID> {
}

