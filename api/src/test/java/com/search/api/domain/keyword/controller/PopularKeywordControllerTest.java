package com.search.api.domain.keyword.controller;

import com.search.api.domain.keyword.service.PopularKeywordService;
import com.search.core.dto.PopularResDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;


@SpringBootTest
@AutoConfigureWebTestClient
@AutoConfigureRestDocs
class PopularKeywordControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private PopularKeywordService popularKeywordService;

    @Test
    @DisplayName("Spring rest doc 활용을 위한 테스트")
    public void popularKeywordForDoc(){
        //given
        given(popularKeywordService.popularKeywordTop10()).willReturn(createResponse());

        //when
        //then
        webTestClient.get()
                .uri("/popular-keyword")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.ok").isEqualTo(Boolean.TRUE)
                .jsonPath("$.code").isEqualTo("KB200")
                .jsonPath("$.body").isArray()
                .jsonPath("$.body[0].query").isEqualTo("simsimeie")
                .jsonPath("$.body[0].count").isEqualTo(999L)
                .consumeWith(document("popular-keyword", preprocessResponse(prettyPrint())));

    }

    private List<PopularResDto> createResponse(){
        PopularResDto top1 = new PopularResDto("simsimeie", 999L);
        PopularResDto top2 = new PopularResDto("네이버", 101L);
        PopularResDto top3 = new PopularResDto("카카오", 101L);
        PopularResDto top4 = new PopularResDto("라인", 100L);
        PopularResDto top5 = new PopularResDto("쿠팡", 100L);
        PopularResDto top6 = new PopularResDto("배달의민족", 100L);
        PopularResDto top7 = new PopularResDto("당근마켓", 100L);
        PopularResDto top8 = new PopularResDto("토스", 100L);
        PopularResDto top9 = new PopularResDto("직방", 100L);
        PopularResDto top10 = new PopularResDto("야놀자", 100L);

        List<PopularResDto> popularResDtoList = new ArrayList<>();
        popularResDtoList.add(top1);
        popularResDtoList.add(top2);
        popularResDtoList.add(top3);
        popularResDtoList.add(top4);
        popularResDtoList.add(top5);
        popularResDtoList.add(top6);
        popularResDtoList.add(top7);
        popularResDtoList.add(top8);
        popularResDtoList.add(top9);
        popularResDtoList.add(top10);

        return popularResDtoList;
    }

}