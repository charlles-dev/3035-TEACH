package com.teach3035;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ApplicationLauncher {

    public static void main(String[] args) {
        String profile = System.getenv("SPRING_PROFILES_ACTIVE");
        if (profile == null) {
            profile = System.getProperty("spring.profiles.active", "ex1");
        }
        
        System.out.println("Iniciando com perfil: " + profile);
        
        Class<?> primarySource = switch (profile) {
            case "ex1" -> com.teach3035.exercicio1_produtos_inmemory.Application.class;
            case "ex2" -> com.teach3035.exercicio2_produtos_jpa.Application.class;
            case "desafio" -> com.teach3035.desafio_todo_jwt.Application.class;
            default -> com.teach3035.exercicio1_produtos_inmemory.Application.class;
        };
        
        SpringApplication app = new SpringApplication(primarySource);
        ConfigurableApplicationContext context = app.run(args);
    }
}