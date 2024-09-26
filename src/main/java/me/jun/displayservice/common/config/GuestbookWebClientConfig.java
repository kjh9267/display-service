package me.jun.displayservice.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GuestbookWebClientConfig {

    @Bean
    public WebClient.Builder guestbookWebClientBuilder() {
        return WebClient.builder();
    }
}
