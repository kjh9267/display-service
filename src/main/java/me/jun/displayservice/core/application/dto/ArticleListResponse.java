package me.jun.displayservice.core.application.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class ArticleListResponse {

    private List<ArticleResponse> articleResponses;

    public static ArticleListResponse of(List<ArticleResponse> articleResponses) {
        return new ArticleListResponse(articleResponses);
    }
}
