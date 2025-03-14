package me.jun.displayservice.core.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import me.jun.displayservice.core.application.GuestbookService;
import me.jun.displayservice.core.application.dto.PostListResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import redis.embedded.RedisServer;

import java.io.IOException;

import static me.jun.displayservice.support.DisplayFixture.REDIS_PORT;
import static me.jun.displayservice.support.GuestbookFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ActiveProfiles("test")
@SpringBootTest(properties = "spring.cloud.config.enabled=false")
@SuppressWarnings("deprecation")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GuestbookServiceImplTest {

    @Autowired
    private GuestbookService guestbookServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    private MockWebServer mockWebServer;

    private RedisServer redisServer;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(GUESTBOOK_PORT);
        redisServer = new RedisServer(REDIS_PORT);
        redisServer.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
        redisServer.stop();
    }

    @Test
    void retrievePostListTest() throws JsonProcessingException {
        PostListResponse expected = postListResponse();
        String content = objectMapper.writeValueAsString(postListResponse());

        MockResponse mockResponse = new MockResponse()
                .setResponseCode(OK.value())
                .setHeader(CONTENT_TYPE, APPLICATION_JSON)
                .setBody(content);

        mockWebServer.url(GUESTBOOK_BASE_URL);
        mockWebServer.enqueue(mockResponse);

        PostListResponse postListResponse = guestbookServiceImpl.retrievePostList(postRequest()).block();

        assertThat(postListResponse)
                .isEqualToComparingFieldByField(expected);
    }

    @Test
    void circuitBreakerTest() {
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(BAD_REQUEST.value());

        mockWebServer.url(GUESTBOOK_BASE_URL);

        for (int count = 0; count < 100; count++) {
            mockWebServer.enqueue(mockResponse);
            try {
                guestbookServiceImpl.retrievePostList(postRequest())
                        .block();
            }
            catch (Exception e) {
            }
        }

        mockWebServer.enqueue(mockResponse);
        assertThrows(
                CallNotPermittedException.class,
                () -> guestbookServiceImpl.retrievePostList(postRequest())
                        .block()
        );
    }
}