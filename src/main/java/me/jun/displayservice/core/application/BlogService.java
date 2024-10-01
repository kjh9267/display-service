package me.jun.displayservice.core.application;

import me.jun.displayservice.core.application.dto.ArticleListResponse;
import me.jun.displayservice.core.application.dto.ArticleRequest;

public interface BlogService {

    ArticleListResponse retrieveArticleList(ArticleRequest request);
}
