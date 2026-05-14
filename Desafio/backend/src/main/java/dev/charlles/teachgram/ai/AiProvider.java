package dev.charlles.teachgram.ai;

import dev.charlles.teachgram.ai.AiDtos.BioRequest;
import dev.charlles.teachgram.ai.AiDtos.BioResponse;
import dev.charlles.teachgram.ai.AiDtos.CaptionRequest;
import dev.charlles.teachgram.ai.AiDtos.CaptionResponse;
import dev.charlles.teachgram.ai.AiDtos.HashtagRequest;
import dev.charlles.teachgram.ai.AiDtos.HashtagResponse;
import dev.charlles.teachgram.ai.AiDtos.ModerationRequest;
import dev.charlles.teachgram.ai.AiDtos.ModerationResponse;

public interface AiProvider {
    CaptionResponse caption(CaptionRequest request);
    HashtagResponse hashtags(HashtagRequest request);
    BioResponse bio(BioRequest request);
    ModerationResponse moderate(ModerationRequest request);
}

