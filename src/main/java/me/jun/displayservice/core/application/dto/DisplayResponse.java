package me.jun.displayservice.core.application.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class DisplayResponse {

    private ArticleListResponse articleListResponse;

    private PostListResponse postListResponse;

    public static DisplayResponse of(
            ArticleListResponse articleListResponse,
            PostListResponse postListResponse
    ) {
        return new DisplayResponse(articleListResponse, postListResponse);
    }
}
