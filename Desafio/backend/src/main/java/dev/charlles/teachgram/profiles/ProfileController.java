package dev.charlles.teachgram.profiles;

import dev.charlles.teachgram.profiles.ProfileDtos.MeResponse;
import dev.charlles.teachgram.profiles.ProfileDtos.ProfileUpdateRequest;
import dev.charlles.teachgram.profiles.ProfileDtos.PublicProfileResponse;
import dev.charlles.teachgram.shared.security.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class ProfileController {

    private final ProfileService profileService;
    private final CurrentUser currentUser;

    public ProfileController(ProfileService profileService, CurrentUser currentUser) {
        this.profileService = profileService;
        this.currentUser = currentUser;
    }

    @GetMapping("/me")
    MeResponse me() {
        return profileService.me(currentUser.require());
    }

    @PatchMapping("/me")
    MeResponse updateMe(@Valid @RequestBody ProfileUpdateRequest request) {
        return profileService.updateMe(currentUser.require(), request);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteMe() {
        profileService.deleteMe(currentUser.require());
    }

    @GetMapping("/{username}")
    PublicProfileResponse publicProfile(@PathVariable String username) {
        return profileService.publicProfile(username, currentUser.optional());
    }
}

