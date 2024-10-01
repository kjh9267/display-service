package me.jun.displayservice.core.application.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class PostRequest {

    private int page;

    private int size;

    public static PostRequest of(int page, int size) {
        return PostRequest.builder()
                .page(page)
                .size(size)
                .build();
    }

    @Override
    public String toString() {
        return String.format("%s %s", page, size);
    }
}
