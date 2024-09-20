package me.jun.displayservice.core.application.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Builder
@Getter
public class PostListResponse {

    private List<PostResponse> postResponses;

    public static PostListResponse of(List<PostResponse> postResponses) {
        return new PostListResponse(postResponses);
    }
}
