package me.jun.displayservice.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.jun.displayservice.core.application.dto.PostListResponse;
import me.jun.displayservice.core.application.dto.PostResponse;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static java.time.Instant.now;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
abstract public class GuestbookFixture {

    public static final String GUESTBOOK_BASE_URL = "http://127.0.0.1";

    public static final int GUESTBOOK_PORT = 8081;

    public static final Long POST_ID = 1L;

    public static final String POST_TITLE = "title string";

    public static final String POST_CONTENT = "content string";

    public static final Long POST_WRITER_ID = 1L;

    public static final Instant CREATED_AT = now();

    public static final Instant UPDATED_AT = now();

    public static PostResponse postResponse() {
        return PostResponse.builder()
                .id(POST_ID)
                .title(POST_TITLE)
                .content(POST_CONTENT)
                .writerId(POST_WRITER_ID)
                .createdAt(CREATED_AT)
                .updatedAt(UPDATED_AT)
                .build();
    }

    public static List<PostResponse> postResponseList() {
        return LongStream.rangeClosed(1, 10)
                .mapToObj(
                        id -> postResponse().toBuilder()
                                .id(id)
                                .build()
                )
                .collect(Collectors.toList());
    }

    public static PostListResponse postListResponse() {
        return PostListResponse.of(postResponseList());
    }
}
