package dev.charlles.teachgram.shared.security;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;
import com.nimbusds.jose.jwk.source.ImmutableSecret;

@Service
public class JwtService {

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;
    private final Duration accessTokenTtl;

    public JwtService(
            @Value("${app.security.jwt-secret}") String secret,
            @Value("${app.security.access-token-ttl}") Duration accessTokenTtl
    ) {
        byte[] keyBytes = secret.getBytes();
        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA256");
        this.encoder = new NimbusJwtEncoder(new ImmutableSecret<>(key));
        this.decoder = NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
        this.accessTokenTtl = accessTokenTtl;
    }

    public TokenIssue issue(UUID userId, String username, String email, String role) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(accessTokenTtl);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("teachgram-pro")
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(userId.toString())
                .claim("username", username)
                .claim("email", email)
                .claim("role", role)
                .build();
        String token = encoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims))
                .getTokenValue();
        return new TokenIssue(token, accessTokenTtl.toSeconds());
    }

    public AuthUser parse(String token) {
        var jwt = decoder.decode(token);
        return new AuthUser(
                UUID.fromString(jwt.getSubject()),
                jwt.getClaimAsString("username"),
                jwt.getClaimAsString("email"),
                jwt.getClaimAsString("role")
        );
    }

    public record TokenIssue(String accessToken, long expiresIn) {
    }
}

