package me.jun.displayservice.core.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jun.displayservice.core.application.dto.ArticleRequest;
import me.jun.displayservice.core.application.dto.DisplayRequest;
import me.jun.displayservice.core.application.dto.DisplayResponse;
import me.jun.displayservice.core.application.dto.PostRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class DisplayService {

    private final BlogService blogServiceImpl;

    private final GuestbookService guestbookServiceImpl;

    public Mono<DisplayResponse> retrieveDisplay(Mono<DisplayRequest> requestMono) {
        return requestMono.flatMap(
                request -> blogServiceImpl.retrieveArticleList(
                        ArticleRequest.of(request.getBlogArticlePage(), request.getBlogArticleSize())
                        ).log()
                        .subscribeOn(Schedulers.boundedElastic())
                        .flatMap(articleListResponse ->
                                guestbookServiceImpl.retrievePostList(
                                        PostRequest.of(request.getGuestbookPostPage(), request.getGuestbookPostSize())
                                ).log()
                                        .subscribeOn(Schedulers.boundedElastic())
                                        .map(postListResponse ->
                                                DisplayResponse.of(articleListResponse, postListResponse)
                                        ).log()
                        )
                );
    }
}
