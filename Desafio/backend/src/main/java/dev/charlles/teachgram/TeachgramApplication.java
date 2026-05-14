package dev.charlles.teachgram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulithic;

@Modulithic
@SpringBootApplication
public class TeachgramApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeachgramApplication.class, args);
    }
}

