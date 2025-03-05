package me.jun.displayservice.core.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jun.displayservice.core.application.dto.ArticleRequest;
import me.jun.displayservice.core.application.dto.DisplayRequest;
import me.jun.displayservice.core.application.dto.DisplayResponse;
import me.jun.displayservice.core.application.dto.PostRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DisplayService {

    private final BlogService blogServiceImpl;

    private final GuestbookService guestbookServiceImpl;

    public Mono<DisplayResponse> retrieveDisplay(Mono<DisplayRequest> requestMono) {
        return requestMono.flatMap(
                request -> {
                    ArticleRequest articleRequest = ArticleRequest.of(request.getBlogArticlePage(), request.getBlogArticleSize());
                    PostRequest postRequest = PostRequest.of(request.getGuestbookPostPage(), request.getGuestbookPostSize());

                    return blogServiceImpl.retrieveArticleList(articleRequest)
                            .flatMap(articleListResponse ->
                                guestbookServiceImpl.retrievePostList(postRequest)
                                        .map(postListResponse ->
                                                DisplayResponse.of(articleListResponse, postListResponse)
                                        )
                            );
                });
    }
}
