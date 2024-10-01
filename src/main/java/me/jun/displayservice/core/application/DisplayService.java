package me.jun.displayservice.core.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jun.displayservice.core.application.dto.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static reactor.core.scheduler.Schedulers.boundedElastic;

@Slf4j
@Service
@RequiredArgsConstructor
public class DisplayService {

    private final BlogService blogServiceImpl;

    private final GuestbookService guestbookServiceImpl;

    public Mono<DisplayResponse> retrieveDisplay(Mono<DisplayRequest> requestMono) {
        return requestMono.map(
                request -> {
                    ArticleRequest articleRequest = ArticleRequest.of(request.getBlogArticlePage(), request.getBlogArticleSize());
                    Mono<PostRequest> postRequestMono = createPostRequestMono(request);
                    Mono<PostListResponse> postListResponseMono = guestbookServiceImpl.retrievePostList(postRequestMono).log();

                    CompletableFuture<ArticleListResponse> articleListResponseFuture = CompletableFuture.supplyAsync(
                            () -> blogServiceImpl.retrieveArticleList(articleRequest)
                    );

                    CompletableFuture<PostListResponse> postListResponseFuture = CompletableFuture.supplyAsync(
                            () -> postListResponseMono.block()
                    );

                    ArticleListResponse articleListResponse;
                    PostListResponse postListResponse;

                    try {
                        articleListResponse = articleListResponseFuture.get();
                        postListResponse = postListResponseFuture.get();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }

                    return DisplayResponse.of(articleListResponse, postListResponse);
                }
                );
    }

    private static Mono<PostRequest> createPostRequestMono(DisplayRequest request) {
        return Mono.fromSupplier(
                        () -> PostRequest.of(request.getGuestbookPostPage(), request.getGuestbookPostSize())
                ).log()
                .publishOn(boundedElastic()).log();
    }
}
