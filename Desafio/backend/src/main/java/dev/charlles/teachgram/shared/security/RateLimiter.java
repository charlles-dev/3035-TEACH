package dev.charlles.teachgram.shared.security;

import dev.charlles.teachgram.shared.config.RateLimitProperties;
import dev.charlles.teachgram.shared.error.ApiException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class RateLimiter {

    private final RateLimitProperties properties;
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public RateLimiter(RateLimitProperties properties) {
        this.properties = properties;
    }

    public void checkLogin(String key) {
        check("login:" + key, properties.loginPerMinute(), Duration.ofMinutes(1));
    }

    public void checkAi(String key) {
        check("ai:" + key, properties.aiPerHour(), Duration.ofHours(1));
    }

    private void check(String key, int limit, Duration window) {
        if (!properties.enabled()) {
            return;
        }
        Instant now = Instant.now();
        Bucket bucket = buckets.compute(key, (ignored, current) -> {
            if (current == null || now.isAfter(current.resetAt())) {
                return new Bucket(1, now.plus(window));
            }
            return new Bucket(current.count() + 1, current.resetAt());
        });
        if (bucket.count() > limit) {
            throw ApiException.tooManyRequests("Rate limit exceeded. Try again later.");
        }
    }

    private record Bucket(int count, Instant resetAt) {
    }
}

