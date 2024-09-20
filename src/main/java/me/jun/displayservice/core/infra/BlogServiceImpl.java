package me.jun.displayservice.core.infra;

import lombok.extern.slf4j.Slf4j;
import me.jun.displayservice.core.application.BlogService;
import me.jun.displayservice.core.application.dto.ArticleListResponse;
import me.jun.displayservice.core.application.dto.ArticleRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class BlogServiceImpl implements BlogService {

    private final WebClient blogWebClient;

    private final String blogArticleUri;

    public BlogServiceImpl(
            WebClient blogWebClient,
            @Value("#{${blog-article-uri}}") String blogArticleUri
    ) {
        this.blogWebClient = blogWebClient;
        this.blogArticleUri = blogArticleUri;
    }

    @Override
    public Mono<ArticleListResponse> retrieveArticleList(Mono<ArticleRequest> requestMono) {
        return requestMono.flatMap(
                request -> blogWebClient.get()
                        .uri(String.format("%s?page=%s&size=%s", blogArticleUri, request.getPage(), request.getSize()))
                        .retrieve()
                        .bodyToMono(ArticleListResponse.class)
                        .log()
                        .doOnError(throwable -> log.error("{}", throwable))
        );
    }
}
