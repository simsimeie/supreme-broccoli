package com.search.api.domain.blog.controller;

import com.search.api.domain.blog.dto.BlogResDto;
import com.search.api.domain.blog.service.BlogSearchService;
import com.search.core.dto.ResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@SpringBootTest
@AutoConfigureWebTestClient
@AutoConfigureRestDocs
class BlogSearchControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private BlogSearchService blogSearchService;

    @Test
    public void blogSearchForDoc(){
        BlogResDto response = getResponse();

        given(blogSearchService.search(any())).willReturn(Mono.just(ResponseDto.of(response)));
        webTestClient.get().uri("/blog?query=spring&criteria=A&page=1&size=10")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("blog", preprocessResponse(prettyPrint())));

    }

    @Test
    public void test01(){
        webTestClient.get().uri("/blog?query=테스트&criteria=A&page=0")
                .exchange()
                .expectStatus().is4xxClientError();
    }

    private static BlogResDto getResponse() {
        BlogResDto.Details detail = new BlogResDto.Details();
        List<BlogResDto.Details> detailsList = new ArrayList<>();
        detail.setTitle("Blog Search Service");
        detail.setContents("이 서비스를 통해 블로그를 손쉽게 검색할 수 있습니다.");
        detail.setBlogname("블로그 검색 서비스");
        detail.setUrl("https://blog.kakao.com/simsim");
        detail.setDatetime("2023-03-21");
        detailsList.add(detail);
        BlogResDto kakaoResponse = new BlogResDto();
        kakaoResponse.setDocuments(detailsList);
        return kakaoResponse;
    }

}