package dev.charlles.teachgram.identity;

import dev.charlles.teachgram.identity.AuthDtos.AuthResponse;
import dev.charlles.teachgram.identity.AuthDtos.LoginRequest;
import dev.charlles.teachgram.identity.AuthDtos.LogoutRequest;
import dev.charlles.teachgram.identity.AuthDtos.RefreshRequest;
import dev.charlles.teachgram.identity.AuthDtos.SignupRequest;
import dev.charlles.teachgram.shared.config.SecurityProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.time.Duration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final SecurityProperties securityProperties;

    public AuthController(AuthService authService, SecurityProperties securityProperties) {
        this.authService = authService;
        this.securityProperties = securityProperties;
    }

    @PostMapping("/signup")
    ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        AuthService.IssuedAuth issued = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, refreshCookie(issued.refreshToken()).toString())
                .body(issued.response());
    }

    @PostMapping("/login")
    ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest servletRequest) {
        AuthService.IssuedAuth issued = authService.login(request, servletRequest.getRemoteAddr());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie(issued.refreshToken()).toString())
                .body(issued.response());
    }

    @PostMapping("/refresh")
    ResponseEntity<AuthResponse> refresh(
            @RequestBody(required = false) RefreshRequest request,
            @CookieValue(name = "${app.security.refresh-cookie-name}", required = false) String cookieRefreshToken
    ) {
        String refreshToken = request != null && request.refreshToken() != null ? request.refreshToken() : cookieRefreshToken;
        AuthService.IssuedAuth issued = authService.refresh(refreshToken);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie(issued.refreshToken()).toString())
                .body(issued.response());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void logout(
            @RequestBody(required = false) LogoutRequest request,
            @CookieValue(name = "${app.security.refresh-cookie-name}", required = false) String cookieRefreshToken,
            HttpServletResponse response
    ) {
        String refreshToken = request != null && request.refreshToken() != null ? request.refreshToken() : cookieRefreshToken;
        authService.logout(refreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE, clearRefreshCookie().toString());
    }

    private ResponseCookie refreshCookie(String value) {
        return ResponseCookie.from(securityProperties.refreshCookieName(), value)
                .httpOnly(true)
                .secure(securityProperties.secureCookies())
                .sameSite(securityProperties.secureCookies() ? "None" : "Lax")
                .path("/api/v1/auth")
                .maxAge(securityProperties.refreshTokenTtl())
                .build();
    }

    private ResponseCookie clearRefreshCookie() {
        return ResponseCookie.from(securityProperties.refreshCookieName(), "")
                .httpOnly(true)
                .secure(securityProperties.secureCookies())
                .sameSite(securityProperties.secureCookies() ? "None" : "Lax")
                .path("/api/v1/auth")
                .maxAge(Duration.ZERO)
                .build();
    }
}

