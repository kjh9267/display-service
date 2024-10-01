package me.jun.displayservice.core.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jun.displayservice.core.application.dto.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
                    PostRequest postRequest = PostRequest.of(request.getGuestbookPostPage(), request.getGuestbookPostSize());

                    CompletableFuture<ArticleListResponse> articleListResponseFuture = CompletableFuture.supplyAsync(
                            () -> blogServiceImpl.retrieveArticleList(articleRequest)
                    );

                    CompletableFuture<PostListResponse> postListResponseFuture = CompletableFuture.supplyAsync(
                            () -> guestbookServiceImpl.retrievePostList(postRequest)
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
}
