package com.search.api.domain.blog.controller;

import com.search.api.domain.blog.dto.BlogResDto;
import com.search.api.domain.blog.service.BlogSearchService;
import com.search.core.dto.ResponseDto;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Spring rest doc 활용을 위한 테스트")
    public void blogSearchForDoc(){
        //given
        BlogResDto response = getResponse();
        given(blogSearchService.search(any())).willReturn(Mono.just(ResponseDto.of(response)));

        //when
        //then
        webTestClient.get()
                .uri("/blog?query=spring&criteria=A&page=1&size=10")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.ok").isEqualTo(Boolean.TRUE)
                .jsonPath("$.code").isEqualTo("KB200")
                .jsonPath("$.body.documents[0].title").isEqualTo("Blog Search Service")
                .jsonPath("$.body.documents[0].contents").isEqualTo("이 서비스를 통해 블로그를 손쉽게 검색할 수 있습니다.")
                .jsonPath("$.body.documents[0].blogname").isEqualTo("블로그 검색 서비스")
                .jsonPath("$.body.documents[0].url").isEqualTo("https://blog.kakao.com/simsim")
                .jsonPath("$.body.documents[0].datetime").isEqualTo("2023-03-21")
                .consumeWith(document("blog", preprocessResponse(prettyPrint())));

    }

    @Test
    @DisplayName("1000자 초과의 검색어가 들어왔을 때, 코드 KB400로 리턴하는지 테스트")
    public void queryValidationTest001(){
        String ten = "abcdefghij";
        StringBuilder sb = new StringBuilder("1");
        for(int i=0 ; i<100 ; i++) sb.append(ten);

        webTestClient.get().uri("/blog?query="+sb)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.ok").isEqualTo(Boolean.FALSE)
                .jsonPath("$.code").isEqualTo("KB400");
    }
    @Test
    @DisplayName("잘못된 검색기준 값이 들어왔을 때, 코드 KB40001로 리턴하는지 테스트")
    public void criteriaValidationTest001(){
        webTestClient.get().uri("/blog?query=테스트&criteria=B")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.ok").isEqualTo(Boolean.FALSE)
                .jsonPath("$.code").isEqualTo("KB40001");
    }

    @Test
    @DisplayName("1미만의 page 값이 들어왔을 때, 코드 KB400 리턴하는지 테스트")
    public void pageValidationTest001(){
        webTestClient.get().uri("/blog?query=테스트&criteria=A&page=0")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.ok").isEqualTo(Boolean.FALSE)
                .jsonPath("$.code").isEqualTo("KB400");
    }

    @Test
    @DisplayName("50초과의 page 값이 들어왔을 때, 코드 KB400 리턴하는지 테스트")
    public void pageValidationTest002(){
        webTestClient.get().uri("/blog?query=테스트&criteria=A&page=51")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.ok").isEqualTo(Boolean.FALSE)
                .jsonPath("$.code").isEqualTo("KB400");
    }

    @Test
    @DisplayName("1미만의 size 값이 들어왔을 때, 코드 KB400 리턴하는지 테스트")
    public void sizeValidationTest001(){
        webTestClient.get().uri("/blog?query=테스트&criteria=A&page=1&size=0")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.ok").isEqualTo(Boolean.FALSE)
                .jsonPath("$.code").isEqualTo("KB400");
    }

    @Test
    @DisplayName("50초과의 size 값이 들어왔을 때, 코드 KB400 리턴하는지 테스트")
    public void sizeValidationTest002(){
        webTestClient.get().uri("/blog?query=테스트&criteria=A&page=1&size=51")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.ok").isEqualTo(Boolean.FALSE)
                .jsonPath("$.code").isEqualTo("KB400");
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