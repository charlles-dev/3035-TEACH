package dev.charlles.teachgram.identity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public final class AuthDtos {
    private AuthDtos() {
    }

    public record SignupRequest(
            @NotBlank @Size(max = 120) String name,
            @NotBlank @Pattern(regexp = "^[a-zA-Z0-9._]{3,40}$") String username,
            @NotBlank @Email String email,
            @Size(max = 30) String phone,
            @NotBlank @Size(min = 8, max = 120) String password,
            String avatarUrl,
            @Size(max = 500) String bio
    ) {
    }

    public record LoginRequest(
            @NotBlank String identifier,
            @NotBlank String password
    ) {
    }

    public record RefreshRequest(String refreshToken) {
    }

    public record LogoutRequest(String refreshToken) {
    }

    public record AuthUserResponse(
            UUID id,
            String username,
            String email,
            String displayName,
            String avatarUrl,
            String role
    ) {
    }

    public record TokenPairResponse(
            String accessToken,
            String tokenType,
            long expiresIn
    ) {
    }

    public record AuthResponse(
            AuthUserResponse user,
            TokenPairResponse tokens
    ) {
    }
}

