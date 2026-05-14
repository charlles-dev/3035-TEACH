package dev.charlles.teachgram.profiles;

import dev.charlles.teachgram.identity.UserAccount;
import dev.charlles.teachgram.identity.UserAccountRepository;
import dev.charlles.teachgram.identity.UserRegisteredEvent;
import dev.charlles.teachgram.profiles.ProfileDtos.MeResponse;
import dev.charlles.teachgram.profiles.ProfileDtos.ProfileStats;
import dev.charlles.teachgram.profiles.ProfileDtos.ProfileSummary;
import dev.charlles.teachgram.profiles.ProfileDtos.ProfileUpdateRequest;
import dev.charlles.teachgram.profiles.ProfileDtos.PublicProfileResponse;
import dev.charlles.teachgram.profiles.ProfileDtos.ViewerState;
import dev.charlles.teachgram.shared.error.ApiException;
import dev.charlles.teachgram.shared.security.AuthUser;
import java.util.UUID;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    private final UserAccountRepository users;
    private final UserProfileRepository profiles;

    public ProfileService(UserAccountRepository users, UserProfileRepository profiles) {
        this.users = users;
        this.profiles = profiles;
    }

    @EventListener
    @Transactional
    public void createProfile(UserRegisteredEvent event) {
        if (profiles.findByUserId(event.userId()).isPresent()) {
            return;
        }
        UserAccount user = users.findById(event.userId()).orElseThrow();
        profiles.save(new UserProfile(user, event.displayName(), event.phone(), event.bio(), event.avatarUrl()));
    }

    @Transactional(readOnly = true)
    public MeResponse me(AuthUser authUser) {
        UserProfile profile = profileByUserId(authUser.id());
        UserAccount user = profile.getUser();
        return new MeResponse(user.getId(), user.getUsername(), user.getEmail(), profile.getDisplayName(),
                profile.getPhone(), profile.getBio(), profile.getAvatarUrl());
    }

    @Transactional
    public MeResponse updateMe(AuthUser authUser, ProfileUpdateRequest request) {
        UserProfile profile = profileByUserId(authUser.id());
        UserAccount user = profile.getUser();
        if (request.email() != null && !request.email().isBlank()
                && !request.email().equalsIgnoreCase(user.getEmail())
                && users.existsByEmailIgnoreCase(request.email())) {
            throw ApiException.conflict("Email already registered.");
        }
        if (request.username() != null && !request.username().isBlank()
                && !request.username().equalsIgnoreCase(user.getUsername())
                && users.existsByUsernameIgnoreCase(request.username())) {
            throw ApiException.conflict("Username already registered.");
        }
        if (request.phone() != null && !request.phone().isBlank()
                && profiles.existsByPhoneAndUserIdNot(request.phone(), authUser.id())) {
            throw ApiException.conflict("Phone already registered.");
        }
        if (request.email() != null && !request.email().isBlank()) {
            user.setEmail(request.email().trim().toLowerCase());
        }
        if (request.username() != null && !request.username().isBlank()) {
            user.setUsername(request.username().trim().replace("@", "").toLowerCase());
        }
        profile.update(request.displayName(), request.phone(), request.bio(), request.avatarUrl());
        return me(new AuthUser(user.getId(), user.getUsername(), user.getEmail(), user.getRole().name()));
    }

    @Transactional
    public void deleteMe(AuthUser authUser) {
        UserAccount user = users.findById(authUser.id()).orElseThrow(() -> ApiException.notFound("User not found."));
        user.delete();
    }

    @Transactional(readOnly = true)
    public PublicProfileResponse publicProfile(String username, AuthUser viewer) {
        UserProfile profile = profiles.findByUserUsernameIgnoreCase(username)
                .filter(candidate -> candidate.getUser().isActive())
                .orElseThrow(() -> ApiException.notFound("Profile not found."));
        boolean own = viewer != null && viewer.id().equals(profile.getUser().getId());
        return new PublicProfileResponse(
                profile.getUser().getId(),
                profile.getUser().getUsername(),
                profile.getDisplayName(),
                profile.getBio(),
                profile.getAvatarUrl(),
                new ProfileStats(0, 0, 0),
                new ViewerState(false, own)
        );
    }

    @Transactional(readOnly = true)
    public ProfileSummary summary(UUID userId) {
        UserProfile profile = profileByUserId(userId);
        return new ProfileSummary(userId, profile.getUser().getUsername(), profile.getDisplayName(),
                profile.getAvatarUrl(), profile.getBio());
    }

    private UserProfile profileByUserId(UUID userId) {
        return profiles.findByUserId(userId).orElseThrow(() -> ApiException.notFound("Profile not found."));
    }
}

