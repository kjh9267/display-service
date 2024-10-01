package me.jun.displayservice.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.jun.displayservice.core.application.dto.DisplayRequest;
import me.jun.displayservice.core.application.dto.DisplayResponse;

import static me.jun.displayservice.support.BlogFixture.*;
import static me.jun.displayservice.support.GuestbookFixture.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
abstract public class DisplayFixture {

    public static final int REDIS_PORT = 6379;

    public static DisplayRequest displayRequest() {
        return DisplayRequest.builder()
                .blogArticlePage(BLOG_ARTICLE_PAGE)
                .blogArticleSize(BLOG_ARTICLE_SIZE)
                .guestbookPostPage(GUESTBOOK_POST_PAGE)
                .guestbookPostSize(GUESTBOOK_POST_SIZE)
                .build();
    }

    public static DisplayResponse displayResponse() {
        return DisplayResponse.of(
                articleListResponse(),
                postListResponse()
        );
    }
}
