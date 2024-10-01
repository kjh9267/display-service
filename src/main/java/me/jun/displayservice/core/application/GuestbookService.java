package me.jun.displayservice.core.application;

import me.jun.displayservice.core.application.dto.PostListResponse;
import me.jun.displayservice.core.application.dto.PostRequest;

public interface GuestbookService {

    PostListResponse retrievePostList(PostRequest requestMono);
}
