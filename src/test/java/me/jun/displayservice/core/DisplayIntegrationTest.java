package me.jun.displayservice.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static me.jun.displayservice.support.BlogFixture.*;
import static me.jun.displayservice.support.GuestbookFixture.*;
import static org.hamcrest.Matchers.hasKey;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DisplayIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private MockWebServer blogMockWebServer;

    private MockWebServer guestbookMockWebServer;

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    @BeforeEach
    void setUp() throws IOException {
        blogMockWebServer = new MockWebServer();
        blogMockWebServer.start(BLOG_PORT);
        guestbookMockWebServer = new MockWebServer();
        guestbookMockWebServer.start(GUESTBOOK_PORT);
    }

    @AfterEach
    void tearDown() throws IOException {
        blogMockWebServer.shutdown();
        guestbookMockWebServer.shutdown();
    }

    @Test
    void displayTest() throws JsonProcessingException {
        retrieveDisplay(0, 10, 0, 10);
    }

    private void retrieveDisplay(
            int blogArticlePage,
            int blogArticleSize,
            int guestbookPostPage,
            int guestbookPostSize
    ) throws JsonProcessingException {
        String blogContent = objectMapper.writeValueAsString(articleListResponse());
        String guestbookContent = objectMapper.writeValueAsString(postListResponse());

        MockResponse blogMockResponse = new MockResponse()
                .setResponseCode(OK.value())
                .setHeader(CONTENT_TYPE, APPLICATION_JSON)
                .setBody(blogContent);

        MockResponse guestbookMockResponse = new MockResponse()
                .setResponseCode(OK.value())
                .setHeader(CONTENT_TYPE, APPLICATION_JSON)
                .setBody(guestbookContent);

        blogMockWebServer.url(BLOG_BASE_URL);
        blogMockWebServer.enqueue(blogMockResponse);

        guestbookMockWebServer.url(GUESTBOOK_BASE_URL);
        guestbookMockWebServer.enqueue(guestbookMockResponse);

        String response = given()
                .log().all()
                .port(port)
                .accept(APPLICATION_JSON_VALUE)
                .queryParam("blogArticlePage", blogArticlePage)
                .queryParam("blogArticleSize", blogArticleSize)
                .queryParam("guestbookPostPage", guestbookPostPage)
                .queryParam("guestbookPostSize", guestbookPostSize)

                .when()
                .get("/api/display/query")

                .then()
                .statusCode(OK.value())
                .body("$", x -> hasKey("articleListResponse"))
                .body("$", x -> hasKey("postListResponse"))
                .body("articleListResponse", x -> hasKey("articleResponses"))
                .body("postListResponse", x -> hasKey("postResponses"))
                .extract()
                .asString();

        JsonElement element = JsonParser.parseString(response);
        System.out.println(gson.toJson(element));
    }
}
