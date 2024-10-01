package me.jun.displayservice.core.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jun.displayservice.core.application.GuestbookService;
import me.jun.displayservice.core.application.dto.PostListResponse;
import me.jun.displayservice.core.application.dto.PostRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Component
@RequiredArgsConstructor
public class GuestbookServiceImpl implements GuestbookService {

    private final WebClient.Builder guestbookWebClientBuilder;

    @Value("#{${guestbook-post-uri}}")
    private String guestbookPostUri;

    @Cacheable(
            cacheNames = "guestbookPosts",
            cacheManager = "redisCacheManager",
            key = "#request.toString()"
    )
    @Override
    public PostListResponse retrievePostList(PostRequest request) {
        return guestbookWebClientBuilder.build()
                .get()
                .uri(String.format(guestbookPostUri, request.getPage(), request.getSize()))
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(PostListResponse.class)
                .block();
    }
}
