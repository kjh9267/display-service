package me.jun.displayservice.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.jun.displayservice.core.application.dto.ArticleListResponse;
import me.jun.displayservice.core.application.dto.ArticleRequest;
import me.jun.displayservice.core.application.dto.ArticleResponse;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static java.time.Instant.now;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
abstract public class BlogFixture {

    public static final String BLOG_BASE_URL = "http://127.0.0.1";

    public static final int BLOG_PORT = 8082;

    public static final int BLOG_ARTICLE_PAGE = 0;

    public static final int BLOG_ARTICLE_SIZE = 10;

    public static final Long ARTICLE_ID = 1L;

    public static final String ARTICLE_TITLE = "title string";

    public static final String ARTICLE_CONTENT = "content string";

    public static final Date CREATED_AT = Date.from(now());

    public static final Date UPDATED_AT = Date.from(now());

    public static ArticleResponse articleResponse() {
        return ArticleResponse.builder()
                .id(ARTICLE_ID)
                .title(ARTICLE_TITLE)
                .content(ARTICLE_CONTENT)
                .createdAt(CREATED_AT)
                .updatedAt(UPDATED_AT)
                .build();
    }

    public static List<ArticleResponse> articleResponseList() {
        return LongStream.rangeClosed(1, 10)
                .mapToObj(
                        id -> articleResponse().toBuilder()
                                .id(id)
                                .build()
                )
                .collect(Collectors.toList());
    }

    public static ArticleListResponse articleListResponse() {
        return ArticleListResponse.of(articleResponseList());
    }

    public static ArticleRequest articleRequest() {
        return ArticleRequest.builder()
                .page(BLOG_ARTICLE_PAGE)
                .size(BLOG_ARTICLE_SIZE)
                .build();
    }
}
