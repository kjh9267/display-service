package me.jun.displayservice.core.infra;

import lombok.extern.slf4j.Slf4j;
import me.jun.displayservice.core.application.GuestbookService;
import me.jun.displayservice.core.application.dto.PostListResponse;
import me.jun.displayservice.core.application.dto.PostRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Component
public class GuestbookServiceImpl implements GuestbookService {

    private final WebClient.Builder guestbookWebClientBuilder;

    private final String guestbookPostUri;

    public GuestbookServiceImpl(
            WebClient.Builder guestbookWebClientBuilder,
            @Value("#{${guestbook-post-uri}}") String guestbookPostUri) {
        this.guestbookWebClientBuilder = guestbookWebClientBuilder;
        this.guestbookPostUri = guestbookPostUri;
    }

    @Override
    public Mono<PostListResponse> retrievePostList(Mono<PostRequest> requestMono) {
        return requestMono.flatMap(
                request -> guestbookWebClientBuilder.build()
                        .get()
                        .uri(String.format("%s?page=%s&size=%s", guestbookPostUri, request.getPage(), request.getSize()))
                        .accept(APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(PostListResponse.class)
                );
    }
}
