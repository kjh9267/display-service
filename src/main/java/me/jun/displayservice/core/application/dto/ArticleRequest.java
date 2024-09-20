package me.jun.displayservice.core.application.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class ArticleRequest {

    int page;

    int size;

    public static ArticleRequest of(int page, int size) {
        return ArticleRequest.builder()
                .page(page)
                .size(size)
                .build();
    }
}
