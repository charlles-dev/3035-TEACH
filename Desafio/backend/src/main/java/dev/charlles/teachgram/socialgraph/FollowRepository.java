package dev.charlles.teachgram.socialgraph;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FollowRepository extends JpaRepository<Follow, UUID> {
    boolean existsByFollowerIdAndFollowedId(UUID followerId, UUID followedId);
    Optional<Follow> findByFollowerIdAndFollowedId(UUID followerId, UUID followedId);
    void deleteByFollowerIdAndFollowedId(UUID followerId, UUID followedId);
    long countByFollowedId(UUID followedId);
    long countByFollowerId(UUID followerId);

    @Query("select f.followed.id from Follow f where f.follower.id = :followerId")
    List<UUID> followedIds(UUID followerId);

    List<Follow> findByFollowerIdOrderByCreatedAtDesc(UUID followerId);
    List<Follow> findByFollowedIdOrderByCreatedAtDesc(UUID followedId);
}

