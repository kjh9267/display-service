package me.jun.displayservice.core.application.dto;

import lombok.*;

import java.util.Date;

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

    private Date createdAt;

    private Date updatedAt;
}
