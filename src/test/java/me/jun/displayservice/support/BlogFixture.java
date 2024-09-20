package me.jun.displayservice.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.jun.displayservice.core.application.dto.ArticleListResponse;
import me.jun.displayservice.core.application.dto.ArticleResponse;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static java.time.Instant.now;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
abstract public class BlogFixture {

    public static final String BLOG_BASE_URL = "http://127.0.0.1";

    public static final int BLOG_PORT = 8082;

    public static final Long ARTICLE_ID = 1L;

    public static final String TITLE = "title string";

    public static final String CONTENT = "content string";

    public static final Instant CREATED_AT = now();

    public static final Instant UPDATED_AT = now();

    public static ArticleResponse articleResponse() {
        return ArticleResponse.builder()
                .articleId(ARTICLE_ID)
                .title(TITLE)
                .content(CONTENT)
                .createdAt(CREATED_AT)
                .updatedAt(UPDATED_AT)
                .build();
    }

    public static List<ArticleResponse> articleResponseList() {
        return LongStream.rangeClosed(1, 10)
                .mapToObj(
                        id -> articleResponse().toBuilder()
                                .articleId(id)
                                .build()
                )
                .collect(Collectors.toList());
    }

    public static ArticleListResponse articleListResponse() {
        return ArticleListResponse.of(articleResponseList());
    }
}
