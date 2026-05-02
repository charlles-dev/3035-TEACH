package com.teach3035.desafio_todo_jwt.infrastructure.web;

import com.teach3035.desafio_todo_jwt.application.usecase.AuthUseCase;
import com.teach3035.desafio_todo_jwt.domain.model.User;
import com.teach3035.desafio_todo_jwt.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthUseCase authUseCase;

    public AuthController(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthUseCase.AuthResponse>> register(
            @RequestParam String username, 
            @RequestParam String password) {
        log.info("Register request: username={}", username);
        
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Username e password sao obrigatorios"));
        }
        
        User user = authUseCase.register(new AuthUseCase.RegisterRequest(username, password));
        AuthUseCase.AuthResponse response = authUseCase.login(new AuthUseCase.LoginRequest(username, password));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Usuario registrado com sucesso"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthUseCase.AuthResponse>> login(
            @RequestParam String username, 
            @RequestParam String password) {
        log.info("Login request: username={}", username);
        
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Username e password sao obrigatorios"));
        }
        
        AuthUseCase.AuthResponse response = authUseCase.login(
                new AuthUseCase.LoginRequest(username, password)
        );
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}