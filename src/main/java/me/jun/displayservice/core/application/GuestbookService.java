package me.jun.displayservice.core.application;

import me.jun.displayservice.core.application.dto.PostListResponse;
import me.jun.displayservice.core.application.dto.PostRequest;
import reactor.core.publisher.Mono;

public interface GuestbookService {

    Mono<PostListResponse> retrievePostList(PostRequest requestMono);
}
