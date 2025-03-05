package me.jun.displayservice.core.application;

import me.jun.displayservice.core.application.dto.ArticleListResponse;
import me.jun.displayservice.core.application.dto.ArticleRequest;
import reactor.core.publisher.Mono;

public interface BlogService {

    Mono<ArticleListResponse> retrieveArticleList(ArticleRequest request);
}
