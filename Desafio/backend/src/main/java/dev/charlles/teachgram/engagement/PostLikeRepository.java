package dev.charlles.teachgram.engagement;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, UUID> {
    boolean existsByPostIdAndUserId(UUID postId, UUID userId);
    Optional<PostLike> findByPostIdAndUserId(UUID postId, UUID userId);
}

