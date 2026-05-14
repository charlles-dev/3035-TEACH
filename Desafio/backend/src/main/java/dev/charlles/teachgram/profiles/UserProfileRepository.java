package dev.charlles.teachgram.profiles;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    Optional<UserProfile> findByUserId(UUID userId);
    Optional<UserProfile> findByUserUsernameIgnoreCase(String username);
    boolean existsByPhoneAndUserIdNot(String phone, UUID userId);

    @Query("""
            select p from UserProfile p
            where p.user.status = dev.charlles.teachgram.identity.UserStatus.ACTIVE
              and (lower(p.displayName) like lower(concat('%', :term, '%'))
                or lower(p.user.username) like lower(concat('%', :term, '%')))
            order by p.displayName asc
            """)
    List<UserProfile> search(@Param("term") String term, Pageable pageable);
}
