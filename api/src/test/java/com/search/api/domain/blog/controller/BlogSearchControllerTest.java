package com.search.api.domain.blog.controller;

import com.search.api.domain.blog.service.BlogSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BlogSearchControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private BlogSearchService blogSearchService;

    @Test
    public void test01(){

        webTestClient.get().uri("/blog?query=테스트&criteria=A&page=0")
                .exchange()
                .expectStatus().is4xxClientError();
    }

}