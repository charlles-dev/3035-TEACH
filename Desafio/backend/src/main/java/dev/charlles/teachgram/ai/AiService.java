package dev.charlles.teachgram.ai;

import dev.charlles.teachgram.ai.AiDtos.BioRequest;
import dev.charlles.teachgram.ai.AiDtos.BioResponse;
import dev.charlles.teachgram.ai.AiDtos.CaptionRequest;
import dev.charlles.teachgram.ai.AiDtos.CaptionResponse;
import dev.charlles.teachgram.ai.AiDtos.HashtagRequest;
import dev.charlles.teachgram.ai.AiDtos.HashtagResponse;
import dev.charlles.teachgram.ai.AiDtos.ModerationRequest;
import dev.charlles.teachgram.ai.AiDtos.ModerationResponse;
import dev.charlles.teachgram.identity.UserAccount;
import dev.charlles.teachgram.identity.UserAccountRepository;
import dev.charlles.teachgram.shared.security.AuthUser;
import dev.charlles.teachgram.shared.security.RateLimiter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AiService {

    private final CompositeAiProvider provider;
    private final AiJobRepository jobs;
    private final ModerationEventRepository moderationEvents;
    private final UserAccountRepository users;
    private final RateLimiter rateLimiter;

    public AiService(
            CompositeAiProvider provider,
            AiJobRepository jobs,
            ModerationEventRepository moderationEvents,
            UserAccountRepository users,
            RateLimiter rateLimiter
    ) {
        this.provider = provider;
        this.jobs = jobs;
        this.moderationEvents = moderationEvents;
        this.users = users;
        this.rateLimiter = rateLimiter;
    }

    @Transactional
    public CaptionResponse caption(AuthUser user, CaptionRequest request) {
        rateLimiter.checkAi(user.id().toString());
        long start = System.currentTimeMillis();
        CaptionResponse response = provider.caption(request);
        saveJob(user, response.provider(), response.model(), "CAPTION",
                response.fallbackUsed() ? "FALLBACK_USED" : "SUCCEEDED",
                request.topic(), String.join(" | ", response.suggestions()), null, start);
        return response;
    }

    @Transactional
    public HashtagResponse hashtags(AuthUser user, HashtagRequest request) {
        rateLimiter.checkAi(user.id().toString());
        long start = System.currentTimeMillis();
        HashtagResponse response = provider.hashtags(request);
        saveJob(user, response.provider(), null, "HASHTAGS",
                response.fallbackUsed() ? "FALLBACK_USED" : "SUCCEEDED",
                request.title(), String.join(" ", response.hashtags()), null, start);
        return response;
    }

    @Transactional
    public BioResponse bio(AuthUser user, BioRequest request) {
        rateLimiter.checkAi(user.id().toString());
        long start = System.currentTimeMillis();
        BioResponse response = provider.bio(request);
        saveJob(user, response.provider(), null, "BIO",
                response.fallbackUsed() ? "FALLBACK_USED" : "SUCCEEDED",
                String.valueOf(request.keywords()), String.join(" | ", response.suggestions()), null, start);
        return response;
    }

    @Transactional
    public ModerationResponse moderate(AuthUser user, ModerationRequest request) {
        long start = System.currentTimeMillis();
        ModerationResponse response = provider.moderate(request);
        saveJob(user, response.provider(), response.model(), "MODERATION", "SUCCEEDED",
                request.text(), response.status(), null, start);
        if (request.resourceId() != null) {
            moderationEvents.save(new ModerationEvent(
                    request.resourceType(),
                    request.resourceId(),
                    response.provider(),
                    response.model(),
                    response.status(),
                    "[\"" + String.join("\",\"", response.categories()) + "\"]"
            ));
        }
        return response;
    }

    private void saveJob(AuthUser authUser, String provider, String model, String type, String status,
                         String input, String output, String error, long start) {
        UserAccount user = users.findById(authUser.id()).orElse(null);
        jobs.save(new AiJob(
                user,
                provider,
                model,
                type,
                status,
                hash(input),
                output == null ? null : output.substring(0, Math.min(500, output.length())),
                error,
                (int) (System.currentTimeMillis() - start)
        ));
    }

    private String hash(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return Base64.getEncoder().encodeToString(digest.digest(String.valueOf(value).getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("Could not hash AI input", exception);
        }
    }
}

