package me.jun.displayservice.core.application;

import me.jun.displayservice.core.application.dto.PostListResponse;
import reactor.core.publisher.Mono;

public interface GuestbookService {

    Mono<PostListResponse> retrievePostList(int page, int size);
}
