package dev.charlles.teachgram.demo;

import dev.charlles.teachgram.identity.AuthDtos.SignupRequest;
import dev.charlles.teachgram.identity.AuthService;
import dev.charlles.teachgram.identity.UserAccountRepository;
import dev.charlles.teachgram.media.MediaType;
import dev.charlles.teachgram.posts.PostDtos.MediaRequest;
import dev.charlles.teachgram.posts.PostDtos.PostCreateRequest;
import dev.charlles.teachgram.posts.PostService;
import dev.charlles.teachgram.posts.PostVisibility;
import dev.charlles.teachgram.shared.security.AuthUser;
import java.util.List;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"local", "demo"})
public class DemoDataInitializer implements ApplicationRunner {

    private final UserAccountRepository users;
    private final AuthService authService;
    private final PostService postService;

    public DemoDataInitializer(UserAccountRepository users, AuthService authService, PostService postService) {
        this.users = users;
        this.authService = authService;
        this.postService = postService;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (users.existsByEmailIgnoreCase("demo@teachgram.pro")) {
            return;
        }
        authService.signup(new SignupRequest(
                "Demo TeachGram",
                "demo",
                "demo@teachgram.pro",
                "+5511999990000",
                "Demo@123456",
                "https://images.unsplash.com/photo-1494790108377-be9c29b29330",
                "Perfil demo para recrutadores testarem o TeachGram Pro."
        ));
        var user = users.findByEmailIgnoreCase("demo@teachgram.pro").orElseThrow();
        AuthUser authUser = new AuthUser(user.getId(), user.getUsername(), user.getEmail(), user.getRole().name());
        postService.create(authUser, new PostCreateRequest(
                "Bem-vindo ao TeachGram Pro",
                "Uma rede social fullstack com Spring Boot, React, IA e arquitetura modular.",
                PostVisibility.PUBLIC,
                List.of(new MediaRequest(MediaType.IMAGE, "https://images.unsplash.com/photo-1516321318423-f06f85e504b3", "Notebook com codigo"))
        ));
        postService.create(authUser, new PostCreateRequest(
                "Arquitetura custo zero",
                "Render, Vercel, Supabase, Upstash e fallback de IA para manter a demo viva.",
                PostVisibility.PUBLIC,
                List.of()
        ));
    }
}
