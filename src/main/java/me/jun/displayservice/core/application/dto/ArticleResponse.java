package me.jun.displayservice.core.application.dto;

import lombok.*;

import java.time.Instant;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
@Getter
public class ArticleResponse {

    private Long articleId;

    private String title;

    private String content;

    private Instant createdAt;

    private Instant updatedAt;
}
