package me.jun.displayservice.core.application.dto;

import lombok.*;

import java.util.Date;

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

    private Date createdAt;

    private Date updatedAt;
}
