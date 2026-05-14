package dev.charlles.teachgram.ai;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiJobRepository extends JpaRepository<AiJob, UUID> {
}

