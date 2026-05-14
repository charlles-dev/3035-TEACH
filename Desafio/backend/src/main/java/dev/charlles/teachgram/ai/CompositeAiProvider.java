package dev.charlles.teachgram.ai;

import dev.charlles.teachgram.ai.AiDtos.BioRequest;
import dev.charlles.teachgram.ai.AiDtos.BioResponse;
import dev.charlles.teachgram.ai.AiDtos.CaptionRequest;
import dev.charlles.teachgram.ai.AiDtos.CaptionResponse;
import dev.charlles.teachgram.ai.AiDtos.HashtagRequest;
import dev.charlles.teachgram.ai.AiDtos.HashtagResponse;
import dev.charlles.teachgram.ai.AiDtos.ModerationRequest;
import dev.charlles.teachgram.ai.AiDtos.ModerationResponse;
import org.springframework.stereotype.Component;

@Component
public class CompositeAiProvider implements AiProvider {

    private final GroqCompoundAiProvider groq;
    private final FallbackAiProvider fallback;

    public CompositeAiProvider(GroqCompoundAiProvider groq, FallbackAiProvider fallback) {
        this.groq = groq;
        this.fallback = fallback;
    }

    @Override
    public CaptionResponse caption(CaptionRequest request) {
        try {
            return groq.caption(request);
        } catch (Exception exception) {
            return fallback.caption(request);
        }
    }

    @Override
    public HashtagResponse hashtags(HashtagRequest request) {
        try {
            return groq.hashtags(request);
        } catch (Exception exception) {
            return fallback.hashtags(request);
        }
    }

    @Override
    public BioResponse bio(BioRequest request) {
        try {
            return groq.bio(request);
        } catch (Exception exception) {
            return fallback.bio(request);
        }
    }

    @Override
    public ModerationResponse moderate(ModerationRequest request) {
        try {
            return groq.moderate(request);
        } catch (Exception exception) {
            return fallback.moderate(request);
        }
    }
}
