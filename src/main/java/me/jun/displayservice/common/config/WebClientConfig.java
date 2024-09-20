package me.jun.displayservice.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class WebClientConfig {

    private final String blogUrl;

    private final String guestbookUrl;

    WebClientConfig(
            @Value("#{${blog-base-url}}") String blogUrl,
            @Value("#{${guestbook-base-url}}") String guestbookUrl
    ) {
        this.blogUrl = blogUrl;
        this.guestbookUrl = guestbookUrl;
    }

    @Bean
    public WebClient blogWebClient() {
        return WebClient.builder()
                .baseUrl(blogUrl)
                .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public WebClient guestbookWebClient() {
        return WebClient.builder()
                .baseUrl(guestbookUrl)
                .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .build();
    }
}
