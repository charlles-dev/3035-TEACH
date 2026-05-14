package dev.charlles.teachgram.ai;

import dev.charlles.teachgram.ai.AiDtos.BioRequest;
import dev.charlles.teachgram.ai.AiDtos.BioResponse;
import dev.charlles.teachgram.ai.AiDtos.CaptionRequest;
import dev.charlles.teachgram.ai.AiDtos.CaptionResponse;
import dev.charlles.teachgram.ai.AiDtos.HashtagRequest;
import dev.charlles.teachgram.ai.AiDtos.HashtagResponse;
import dev.charlles.teachgram.ai.AiDtos.ModerationRequest;
import dev.charlles.teachgram.ai.AiDtos.ModerationResponse;
import dev.charlles.teachgram.shared.security.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
public class AiController {

    private final AiService ai;
    private final CurrentUser currentUser;

    public AiController(AiService ai, CurrentUser currentUser) {
        this.ai = ai;
        this.currentUser = currentUser;
    }

    @PostMapping("/caption")
    CaptionResponse caption(@Valid @RequestBody CaptionRequest request) {
        return ai.caption(currentUser.require(), request);
    }

    @PostMapping("/hashtags")
    HashtagResponse hashtags(@Valid @RequestBody HashtagRequest request) {
        return ai.hashtags(currentUser.require(), request);
    }

    @PostMapping("/profile-bio")
    BioResponse bio(@Valid @RequestBody BioRequest request) {
        return ai.bio(currentUser.require(), request);
    }

    @PostMapping("/moderate")
    ModerationResponse moderate(@Valid @RequestBody ModerationRequest request) {
        return ai.moderate(currentUser.require(), request);
    }
}

