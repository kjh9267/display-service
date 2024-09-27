package me.jun.displayservice.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@Profile("test")
@Configuration
public class BlogWebClientConfig {

    @Bean
    public WebClient.Builder blogWebClientBuilder() {
        return WebClient.builder();
    }
}
