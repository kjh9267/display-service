package me.jun.displayservice.core.presentation;

import me.jun.displayservice.core.application.DisplayService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static me.jun.displayservice.support.DisplayFixture.displayResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DisplayControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private DisplayService displayService;

    @Test
    void retrieveDisplayTest() {
        given(displayService.retrieveDisplay(any()))
                .willReturn(Mono.just(displayResponse()));

        webTestClient.get()
                .uri("/api/display/query?blogArticlePage=0&blogArticleSize=10&guestbookPostPage=0&guestbookPostSize=10")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("articleListResponse").exists()
                .jsonPath("articleListResponse.articleResponses").exists()
                .jsonPath("postListResponse").exists()
                .jsonPath("postListResponse.postResponses").exists()
                .consumeWith(System.out::println);
    }
}
