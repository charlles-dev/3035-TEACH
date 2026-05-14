package dev.charlles.teachgram.identity;

import dev.charlles.teachgram.identity.AuthDtos.AuthResponse;
import dev.charlles.teachgram.identity.AuthDtos.AuthUserResponse;
import dev.charlles.teachgram.identity.AuthDtos.LoginRequest;
import dev.charlles.teachgram.identity.AuthDtos.SignupRequest;
import dev.charlles.teachgram.identity.AuthDtos.TokenPairResponse;
import dev.charlles.teachgram.shared.config.SecurityProperties;
import dev.charlles.teachgram.shared.error.ApiException;
import dev.charlles.teachgram.shared.security.JwtService;
import dev.charlles.teachgram.shared.security.RateLimiter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserAccountRepository users;
    private final RefreshTokenRepository refreshTokens;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SecurityProperties securityProperties;
    private final RateLimiter rateLimiter;
    private final ApplicationEventPublisher events;
    private final SecureRandom secureRandom = new SecureRandom();

    public AuthService(
            UserAccountRepository users,
            RefreshTokenRepository refreshTokens,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            SecurityProperties securityProperties,
            RateLimiter rateLimiter,
            ApplicationEventPublisher events
    ) {
        this.users = users;
        this.refreshTokens = refreshTokens;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.securityProperties = securityProperties;
        this.rateLimiter = rateLimiter;
        this.events = events;
    }

    @Transactional
    public IssuedAuth signup(SignupRequest request) {
        String email = normalizeEmail(request.email());
        String username = normalizeUsername(request.username());
        if (users.existsByEmailIgnoreCase(email)) {
            throw ApiException.conflict("Email already registered.");
        }
        if (users.existsByUsernameIgnoreCase(username)) {
            throw ApiException.conflict("Username already registered.");
        }
        UserAccount user = users.save(new UserAccount(email, username, passwordEncoder.encode(request.password())));
        events.publishEvent(new UserRegisteredEvent(
                user.getId(),
                request.name().trim(),
                blankToNull(request.phone()),
                blankToNull(request.bio()),
                blankToNull(request.avatarUrl()),
                Instant.now()
        ));
        return issue(user, request.name(), request.avatarUrl());
    }

    @Transactional
    public IssuedAuth login(LoginRequest request, String remoteAddress) {
        rateLimiter.checkLogin((remoteAddress == null ? "unknown" : remoteAddress) + ":" + request.identifier().toLowerCase());
        UserAccount user = findByIdentifier(request.identifier());
        if (!user.isActive() || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw ApiException.unauthorized("Invalid credentials.");
        }
        user.markLogin();
        return issue(user, null, null);
    }

    @Transactional
    public IssuedAuth refresh(String rawRefreshToken) {
        if (rawRefreshToken == null || rawRefreshToken.isBlank()) {
            throw ApiException.unauthorized("Refresh token required.");
        }
        RefreshToken refreshToken = refreshTokens.findByTokenHash(hash(rawRefreshToken))
                .orElseThrow(() -> ApiException.unauthorized("Invalid refresh token."));
        if (!refreshToken.validAt(Instant.now()) || !refreshToken.getUser().isActive()) {
            throw ApiException.unauthorized("Invalid refresh token.");
        }
        refreshToken.revoke();
        return issue(refreshToken.getUser(), null, null);
    }

    @Transactional
    public void logout(String rawRefreshToken) {
        if (rawRefreshToken == null || rawRefreshToken.isBlank()) {
            return;
        }
        refreshTokens.findByTokenHash(hash(rawRefreshToken)).ifPresent(RefreshToken::revoke);
    }

    private IssuedAuth issue(UserAccount user, String displayName, String avatarUrl) {
        JwtService.TokenIssue access = jwtService.issue(user.getId(), user.getUsername(), user.getEmail(), user.getRole().name());
        String refresh = randomToken();
        refreshTokens.save(new RefreshToken(
                user,
                hash(refresh),
                Instant.now().plus(securityProperties.refreshTokenTtl())
        ));
        AuthResponse response = new AuthResponse(
                new AuthUserResponse(user.getId(), user.getUsername(), user.getEmail(), displayName, avatarUrl, user.getRole().name()),
                new TokenPairResponse(access.accessToken(), "Bearer", access.expiresIn())
        );
        return new IssuedAuth(response, refresh);
    }

    private UserAccount findByIdentifier(String identifier) {
        String normalized = identifier.trim().toLowerCase();
        return (normalized.contains("@")
                ? users.findByEmailIgnoreCase(normalized)
                : users.findByUsernameIgnoreCase(normalized))
                .orElseThrow(() -> ApiException.unauthorized("Invalid credentials."));
    }

    private String randomToken() {
        byte[] bytes = new byte[48];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hash(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashed);
        } catch (Exception exception) {
            throw new IllegalStateException("Could not hash token", exception);
        }
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private String normalizeUsername(String username) {
        return username.trim().replace("@", "").toLowerCase();
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    public record IssuedAuth(AuthResponse response, String refreshToken) {
    }
}

