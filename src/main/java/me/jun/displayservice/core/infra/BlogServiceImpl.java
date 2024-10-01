package me.jun.displayservice.core.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jun.displayservice.core.application.BlogService;
import me.jun.displayservice.core.application.dto.ArticleListResponse;
import me.jun.displayservice.core.application.dto.ArticleRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final WebClient.Builder blogWebClientBuilder;

    @Value("#{${blog-article-uri}}")
    private String blogArticleUri;

    @Cacheable(
            cacheNames = "blogArticles",
            cacheManager = "redisCacheManager",
            key = "#request.toString()"
    )
    @Override
    public ArticleListResponse retrieveArticleList(ArticleRequest request) {
        return blogWebClientBuilder.build()
                .get()
                .uri(String.format(blogArticleUri, request.getPage(), request.getSize()))
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ArticleListResponse.class)
                .block();
    }
}
