package me.jun.displayservice.core.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jun.displayservice.core.application.DisplayService;
import me.jun.displayservice.core.application.dto.DisplayRequest;
import me.jun.displayservice.core.application.dto.DisplayResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/api/display")
@RequiredArgsConstructor
public class DisplayController {

    private final DisplayService displayService;

    @GetMapping(
            value = "/query",
            produces = APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<DisplayResponse>> retrieveDisplay(
            @RequestParam("blogArticlePage") int blogArticlePage,
            @RequestParam("blogArticleSize") int blogArticleSize,
            @RequestParam("guestbookPostPage") int guestbookPostPage,
            @RequestParam("guestbookPostSize") int guestbookPostSize
    ) {
        return displayService.retrieveDisplay(
                Mono.fromSupplier(
                        () -> DisplayRequest.builder()
                                .blogArticlePage(blogArticlePage)
                                .blogArticleSize(blogArticleSize)
                                .guestbookPostPage(guestbookPostPage)
                                .guestbookPostSize(guestbookPostSize)
                                .build()
                )
                        .log()
                        .doOnError(throwable -> log.error("{}", throwable))
        )
                .log()
                .map(
                        response -> ResponseEntity.ok()
                                .body(response)
                )
                .doOnError(throwable -> log.error("{}", throwable));
    }
}
