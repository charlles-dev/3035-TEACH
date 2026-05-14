package dev.charlles.teachgram.shared.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableConfigurationProperties({SecurityProperties.class, AiProperties.class, RateLimitProperties.class})
public class AppConfig {
}

