package com.search.api.domain.blog.service;

import com.search.api.domain.blog.constant.SearchCriteria;
import com.search.api.domain.blog.dto.BlogResDto;
import com.search.api.domain.blog.dto.SearchCondDto;
import com.search.core.constant.ErrorCode;
import com.search.core.dto.ResponseDto;
import com.search.core.exception.KBException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class BlogSearchApiImplTest {

    @InjectMocks
    private BlogSearchApiImpl kakaoBlogSearchApiImpl;
    @Mock
    private BlogSearchProvider kakaoProvider;
    @Mock
    private BlogSearchApi naverBlogSearchApi;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Test
    @DisplayName("카카오와 네이버가 모두 블로그 검색 API에서 에러가 발생할 때, KBException 던지는지 테스트")
    public void kakaoAndNaverFailOverTest001(){
        //given
        SearchCondDto test = SearchCondDto.of("test", SearchCriteria.A, 1, 10);
        given(kakaoProvider.createResponseSpec(any())).willReturn(Mono.error(new Exception()));
        given(kakaoProvider.getProvider()).willReturn("KAKAO");
        given(naverBlogSearchApi.search(any())).willReturn(Mono.error(new KBException(ErrorCode.API_ERROR)));

        //then
        KBException kbException = assertThrows(KBException.class, () -> {
            //when
            Mono<ResponseDto<BlogResDto>> response = kakaoBlogSearchApiImpl.search(test);
            response.block();
        });
        assertEquals(ErrorCode.API_ERROR, kbException.getErrorCode());

    }

    @Test
    @DisplayName("카카오 블로그 검색 API에서 에러가 발생할 때, 네이버 블로그 검색 API 활용해서 데이터 가져오는지 테스트")
    public void kakaoFailOverTest001(){
        //given
        SearchCondDto test = SearchCondDto.of("test", SearchCriteria.A, 1, 10);

        BlogResDto naverResponse = getNaverResponse();

        given(kakaoProvider.createResponseSpec(any())).willReturn(Mono.error(new Exception()));
        given(kakaoProvider.getProvider()).willReturn("KAKAO");
        given(naverBlogSearchApi.search(any())).willReturn(Mono.just(ResponseDto.of(naverResponse)));

        //when
        //then
        StepVerifier.create(kakaoBlogSearchApiImpl.search(test))
                .assertNext(d-> {
                    assertEquals(true, d.getOk());
                    assertEquals("KB200", d.getCode());
                    assertEquals(d.getBody().getDocuments().get(0).getTitle(), "Naver blog api");
                    assertEquals(d.getBody().getDocuments().get(0).getContents(), "test");
                    assertEquals(d.getBody().getDocuments().get(0).getBlogname(), "test");
                    assertEquals(d.getBody().getDocuments().get(0).getUrl(), "https://blog.naver.com");
                    assertEquals(d.getBody().getDocuments().get(0).getDatetime(), "2023-03-21");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("카카오 블로그 검색 API에서 조회 결과가 없을 때, KBException (SUCCESS_BUT_NO_DATA) 발생하는지 테스트")
    public void kakaoNoDataTest001(){
        //given
        SearchCondDto test = SearchCondDto.of("test", SearchCriteria.A, 1, 10);

        BlogResDto emptyResponse = getEmptyResponse();

        given(kakaoProvider.createResponseSpec(any())).willReturn(Mono.just(emptyResponse));
        given(kakaoProvider.getProvider()).willReturn("KAKAO");

        //when
        //then
        KBException kbException = assertThrows(KBException.class, () -> {
            Mono<ResponseDto<BlogResDto>> response = kakaoBlogSearchApiImpl.search(test);
            response.block();
        });
        assertEquals(ErrorCode.SUCCESS_BUT_NO_DATA, kbException.getErrorCode());
    }


    @Test
    @DisplayName("카카오 블로그 검색 API에 문제가 없을 때, 카카오 블로그 검색 API 활용해서 데이터 가져오는지 테스트")
    public void kakaoSuccessTest001(){
        //given
        SearchCondDto test = SearchCondDto.of("test", SearchCriteria.A, 1, 10);

        BlogResDto kakaoResponse = getKakaoResponse();

        given(kakaoProvider.createResponseSpec(any())).willReturn(Mono.just(kakaoResponse));
        given(kakaoProvider.getProvider()).willReturn("KAKAO");
        //when
        //then
        StepVerifier.create(kakaoBlogSearchApiImpl.search(test))
                .assertNext(d-> {
                    assertEquals(true, d.getOk());
                    assertEquals("KB200", d.getCode());
                    assertEquals(d.getBody().getDocuments().get(0).getTitle(), "Kakao blog api");
                    assertEquals(d.getBody().getDocuments().get(0).getContents(), "test");
                    assertEquals(d.getBody().getDocuments().get(0).getBlogname(), "test");
                    assertEquals(d.getBody().getDocuments().get(0).getUrl(), "https://blog.kakao.com");
                    assertEquals(d.getBody().getDocuments().get(0).getDatetime(), "2023-03-21");
                })
                .verifyComplete();
        then(eventPublisher).should(only()).publishEvent(any(Object.class));
    }

    private static BlogResDto getEmptyResponse(){
        BlogResDto blogResDto = new BlogResDto();
        blogResDto.setDocuments(new ArrayList<>());
        return blogResDto;
    }

    private static BlogResDto getKakaoResponse() {
        BlogResDto.Details detail = new BlogResDto.Details();
        List<BlogResDto.Details> detailsList = new ArrayList<>();
        detail.setTitle("Kakao blog api");
        detail.setContents("test");
        detail.setBlogname("test");
        detail.setUrl("https://blog.kakao.com");
        detail.setDatetime("2023-03-21");
        detailsList.add(detail);
        BlogResDto kakaoResponse = new BlogResDto();
        kakaoResponse.setDocuments(detailsList);
        return kakaoResponse;
    }


    private static BlogResDto getNaverResponse() {
        BlogResDto.Details detail = new BlogResDto.Details();
        List<BlogResDto.Details> detailsList = new ArrayList<>();
        detail.setTitle("Naver blog api");
        detail.setContents("test");
        detail.setBlogname("test");
        detail.setUrl("https://blog.naver.com");
        detail.setDatetime("2023-03-21");
        detailsList.add(detail);
        BlogResDto naverResponse = new BlogResDto();
        naverResponse.setDocuments(detailsList);
        return naverResponse;
    }


}