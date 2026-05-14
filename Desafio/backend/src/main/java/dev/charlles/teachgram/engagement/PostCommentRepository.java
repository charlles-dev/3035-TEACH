package dev.charlles.teachgram.engagement;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, UUID> {
    List<PostComment> findByPostIdAndDeletedAtIsNullOrderByCreatedAtAsc(UUID postId, Pageable pageable);
}

