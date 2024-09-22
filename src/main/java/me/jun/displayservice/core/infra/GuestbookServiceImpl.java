package me.jun.displayservice.core.infra;

import lombok.extern.slf4j.Slf4j;
import me.jun.displayservice.core.application.GuestbookService;
import me.jun.displayservice.core.application.dto.PostListResponse;
import me.jun.displayservice.core.application.dto.PostRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GuestbookServiceImpl implements GuestbookService {

    private final WebClient guestbookWebClient;

    private final String guestbookPostUri;

    public GuestbookServiceImpl(
            WebClient guestbookWebClient,
            @Value("#{${guestbook-post-uri}}") String guestbookPostUri) {
        this.guestbookWebClient = guestbookWebClient;
        this.guestbookPostUri = guestbookPostUri;
    }

    @Override
    public Mono<PostListResponse> retrievePostList(Mono<PostRequest> requestMono) {
        return requestMono.flatMap(
                request -> guestbookWebClient.get()
                        .uri(String.format("%s?page=%s&size=%s", guestbookPostUri, request.getPage(), request.getSize()))
                        .retrieve()
                        .bodyToMono(PostListResponse.class).log()
                        .doOnError(throwable -> log.error(throwable.getMessage()))
                ).log()
                .doOnError(throwable -> log.error(throwable.getMessage()));
    }
}
