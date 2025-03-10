package me.jun.displayservice.core.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import me.jun.displayservice.core.application.BlogService;
import me.jun.displayservice.core.application.dto.ArticleListResponse;
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

import static me.jun.displayservice.support.BlogFixture.*;
import static me.jun.displayservice.support.DisplayFixture.REDIS_PORT;
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
class BlogServiceImplTest {

    @Autowired
    private BlogService blogServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    private MockWebServer mockWebServer;

    private RedisServer redisServer;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(BLOG_PORT);

        redisServer = new RedisServer(REDIS_PORT);
        redisServer.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
        redisServer.stop();
    }

    @Test
    void retrieveArticleListTest() throws JsonProcessingException {
        ArticleListResponse expected = articleListResponse();
        String content = objectMapper.writeValueAsString(articleListResponse());

        MockResponse mockResponse = new MockResponse()
                .setResponseCode(OK.value())
                .setHeader(CONTENT_TYPE, APPLICATION_JSON)
                .setBody(content);

        mockWebServer.url(BLOG_BASE_URL);
        mockWebServer.enqueue(mockResponse);

        ArticleListResponse articleListResponse = blogServiceImpl.retrieveArticleList(articleRequest()).block();

        assertThat(articleListResponse)
                .isEqualToComparingFieldByField(expected);
    }

    @Test
    void circuitBreakerTest() {
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(BAD_REQUEST.value());

        mockWebServer.url(BLOG_BASE_URL);

        for (int count = 0; count < 100; count++) {
            mockWebServer.enqueue(mockResponse);
            try {
                blogServiceImpl.retrieveArticleList(articleRequest())
                        .block();
            }
            catch (Exception e) {
            }
        }

        mockWebServer.enqueue(mockResponse);
        assertThrows(
                CallNotPermittedException.class,
                () -> blogServiceImpl.retrieveArticleList(articleRequest())
                        .block()
        );
    }
}