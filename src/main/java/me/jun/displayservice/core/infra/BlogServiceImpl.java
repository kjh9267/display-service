package me.jun.displayservice.core.infra;

import lombok.extern.slf4j.Slf4j;
import me.jun.displayservice.core.application.BlogService;
import me.jun.displayservice.core.application.dto.ArticleListResponse;
import me.jun.displayservice.core.application.dto.ArticleRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Component
public class BlogServiceImpl implements BlogService {

    private final WebClient.Builder blogWebClientBuilder;

    private final String blogArticleUri;

    public BlogServiceImpl(
            WebClient.Builder blogWebClientBuilder,
            @Value("#{${blog-article-uri}}") String blogArticleUri
    ) {
        this.blogWebClientBuilder = blogWebClientBuilder;
        this.blogArticleUri = blogArticleUri;
    }

    @Override
    public Mono<ArticleListResponse> retrieveArticleList(Mono<ArticleRequest> requestMono) {
        return requestMono.flatMap(
                request -> blogWebClientBuilder.build()
                        .get()
                        .uri(String.format("%s?page=%s&size=%s", blogArticleUri, request.getPage(), request.getSize()))
                        .accept(APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(ArticleListResponse.class)
                );
    }
}
