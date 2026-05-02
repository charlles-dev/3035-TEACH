package com.teach3035.desafio_todo_jwt.application.usecase;

import com.teach3035.desafio_todo_jwt.domain.model.User;
import com.teach3035.desafio_todo_jwt.domain.model.UserRole;
import com.teach3035.desafio_todo_jwt.domain.repository.UserRepository;
import com.teach3035.desafio_todo_jwt.exception.UserAlreadyExistsException;
import com.teach3035.desafio_todo_jwt.infrastructure.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthUseCase {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthUseCase(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public record RegisterRequest(String username, String password) {}
    public record LoginRequest(String username, String password) {}
    public record AuthResponse(String accessToken, String refreshToken, long expiresIn) {}

public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException("Usuário já existe");
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = new User(request.username(), encodedPassword, UserRole.USER);
        return userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("Credenciais inválidas"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            user.incrementFailedAttempts();
            userRepository.save(user);

            if (user.getFailedLoginAttempts() >= 5) {
                user.lock();
                userRepository.save(user);
                throw new IllegalStateException("Conta bloqueada após 5 tentativas falhas");
            }
            throw new IllegalArgumentException("Credenciais inválidas");
        }

        user.resetFailedAttempts();
        userRepository.save(user);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        claims.put("userId", user.getId());

        String accessToken = jwtService.generateToken(user.getUsername(), claims);
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());

        return new AuthResponse(accessToken, refreshToken, 900);
    }

    public AuthResponse refresh(String refreshToken) {
        if (!jwtService.isTokenValid(refreshToken)) {
            throw new IllegalArgumentException("Refresh token inválido ou expirado");
        }

        String username = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        claims.put("userId", user.getId());

        String newAccessToken = jwtService.generateToken(user.getUsername(), claims);
        String newRefreshToken = jwtService.generateRefreshToken(user.getUsername());

        return new AuthResponse(newAccessToken, newRefreshToken, 900);
    }
}
