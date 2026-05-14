package dev.charlles.teachgram.shared.pagination;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CursorSupport {

    private final ObjectMapper objectMapper;

    public CursorSupport(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Cursor decode(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            String json = new String(Base64.getUrlDecoder().decode(value), StandardCharsets.UTF_8);
            return objectMapper.readValue(json, Cursor.class);
        } catch (Exception exception) {
            return null;
        }
    }

    public String encode(Instant createdAt, UUID id) {
        try {
            String json = objectMapper.writeValueAsString(new Cursor(createdAt, id));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception exception) {
            throw new IllegalStateException("Could not encode cursor", exception);
        }
    }

    public record Cursor(Instant createdAt, UUID id) {
    }
}

