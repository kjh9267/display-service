package me.jun.displayservice.core.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jun.displayservice.core.application.dto.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DisplayService {

    private final BlogService blogServiceImpl;

    private final GuestbookService guestbookServiceImpl;

    public Mono<DisplayResponse> retrieveDisplay(Mono<DisplayRequest> requestMono) {
        return requestMono.log()
                .map(
                        request -> {
                            Mono<ArticleRequest> articleRequestMono = Mono.fromSupplier(
                                            () -> ArticleRequest.of(request.getBlogArticlePage(), request.getBlogArticleSize())
                                    )
                                    .log()
                                    .doOnError(throwable -> log.error("{}", throwable));

                            ArticleListResponse articleListResponse = blogServiceImpl.retrieveArticleList(
                                            articleRequestMono
                                    )
                                    .log()
                                    .block();

                            Mono<PostRequest> postRequestMono = Mono.fromSupplier(
                                            () -> PostRequest.of(request.getGuestbookPostPage(), request.getGuestbookPostSize())
                                    )
                                    .log()
                                    .doOnError(throwable -> log.error("{}", throwable));

                            PostListResponse postListResponse = guestbookServiceImpl.retrievePostList(postRequestMono)
                                    .log()
                                    .block();

                            return DisplayResponse.of(articleListResponse, postListResponse);
                        }
                )
                .log()
                .doOnError(throwable -> log.error("{}", throwable));
    }
}
