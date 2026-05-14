package dev.charlles.teachgram.engagement;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedPostRepository extends JpaRepository<SavedPost, UUID> {
    boolean existsByPostIdAndUserId(UUID postId, UUID userId);
    Optional<SavedPost> findByPostIdAndUserId(UUID postId, UUID userId);
    List<SavedPost> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);
}

