package me.jun.displayservice.core.application;

import me.jun.displayservice.core.application.dto.DisplayResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static me.jun.displayservice.support.BlogFixture.articleListResponse;
import static me.jun.displayservice.support.DisplayFixture.displayRequest;
import static me.jun.displayservice.support.DisplayFixture.displayResponse;
import static me.jun.displayservice.support.GuestbookFixture.postListResponse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("deprecation")
class DisplayServiceTest {

    private DisplayService displayService;

    @Mock
    private BlogService blogServiceImpl;

    @Mock
    private GuestbookService guestbookServiceImpl;

    @BeforeEach
    void setUp() {
        displayService = new DisplayService(
                blogServiceImpl,
                guestbookServiceImpl
        );
    }

    @Test
    void retrieveDisplayTest() {
        DisplayResponse expected = displayResponse();

        given(blogServiceImpl.retrieveArticleList(any()))
                .willReturn(Mono.just(articleListResponse()));

        given(guestbookServiceImpl.retrievePostList(any()))
                .willReturn(Mono.just(postListResponse()));

        assertThat(displayService.retrieveDisplay(Mono.just(displayRequest())).block())
                .isEqualToComparingFieldByField(expected);
    }
}