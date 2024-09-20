package me.jun.displayservice.core.application.dto;

import lombok.*;

import java.time.Instant;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
@Builder(toBuilder = true)
@Getter
public class PostResponse {

    private Long id;

    private String title;

    private String content;

    private Long writerId;

    private Instant createdAt;

    private Instant updatedAt;
}
