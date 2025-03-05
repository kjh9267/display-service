package me.jun.displayservice.core.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jun.displayservice.core.application.BlogService;
import me.jun.displayservice.core.application.dto.ArticleListResponse;
import me.jun.displayservice.core.application.dto.ArticleRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final WebClient.Builder blogWebClientBuilder;

    private final ReactiveRedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper;

    @Value("#{${blog-article-uri}}")
    private String blogArticleUri;

    @CircuitBreaker(name = "blogServiceCircuitBreaker")
    @Override
    public Mono<ArticleListResponse> retrieveArticleList(ArticleRequest request) {
        ReactiveValueOperations<String, String> operations = redisTemplate.opsForValue();

        Mono<String> responseMono = blogWebClientBuilder.build()
                .get()
                .uri(String.format(blogArticleUri, request.getPage(), request.getSize()))
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(response ->
                        operations.set(
                                request.toString(),
                                response,
                                Duration.of(10L, ChronoUnit.MINUTES)).block()
                );

        return operations.get(request.toString()).log()
                .switchIfEmpty(responseMono).log()
                .map(response -> {
                    try {
                        return objectMapper.readValue(response, ArticleListResponse.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
