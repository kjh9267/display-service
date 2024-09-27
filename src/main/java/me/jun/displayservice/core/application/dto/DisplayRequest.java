package me.jun.displayservice.core.application.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class DisplayRequest {

    @NotNull
    @Positive
    int blogArticlePage;

    @NotNull
    @Positive
    int blogArticleSize;

    @NotNull
    @Positive
    int guestbookPostPage;

    @NotNull
    @Positive
    int guestbookPostSize;
}
