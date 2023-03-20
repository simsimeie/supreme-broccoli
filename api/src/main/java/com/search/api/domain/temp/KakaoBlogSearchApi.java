package com.search.api.domain.temp;

import com.search.api.domain.blog.constant.SearchCriteria;
import com.search.api.domain.blog.dto.BlogResDto;
import com.search.api.domain.blog.dto.SearchCondDto;
import com.search.api.domain.blog.service.BlogSearchApi;
import com.search.api.domain.blog.service.KakaoProvider;
import com.search.api.domain.keyword.event.KeywordEventPub;
import com.search.core.constant.ErrorCode;
import com.search.core.dto.ResponseDto;
import com.search.core.exception.KBException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

//@Component
@Slf4j
public class KakaoBlogSearchApi implements BlogSearchApi {
    private final KakaoProvider module;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final Optional<BlogSearchApi> alternativeApi;

    public KakaoBlogSearchApi(
            KakaoProvider kakaoModule
            , ApplicationEventPublisher applicationEventPublisher
            , @Qualifier("naverBlogSearchApi2") BlogSearchApi naverBlogSearchApi
    ){
        this.module = kakaoModule;
        this.applicationEventPublisher = applicationEventPublisher;
        this.alternativeApi = Optional.of(naverBlogSearchApi);
    }


    private static final String PROVIDER = "KAKAO";
    private static final String ACCURACY = "accuracy";
    private static final String RECENCY = "recency";
    @Value("${api-key.kakao}")
    private String API_KEY;

    @Override
    public Mono<ResponseDto<BlogResDto>> search(SearchCondDto searchCond) {
        return module.createResponseSpec(searchCond)
                .doOnSuccess(
                        d -> applicationEventPublisher.publishEvent(
                                KeywordEventPub.of(searchCond.getKeyword(), PROVIDER)
                        )
                )
                .flatMap(d -> Mono.just(ResponseDto.of(d)))
                .doOnError(e -> {
                    log.error("Error occurred while processing the {} API.", PROVIDER);
                    if (e instanceof WebClientResponseException) {
                        WebClientResponseException ex = (WebClientResponseException) e;
                        log.error("HTTP status code : {}, Info : {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
                    }
                })
                .onErrorResume(e -> {
                    log.error("Error");
                    if (alternativeApi.isPresent()) {
                        log.error("Error");
                        return alternativeApi.get().search(searchCond);
                    } else {
                        if(e instanceof WebClientResponseException){
                            return Mono.error(new KBException(ErrorCode.API_ERROR));
                        } else {
                            return Mono.error(new KBException(ErrorCode.INTERNAL_SERVER_ERROR));
                        }
                    }
                });
    }


    protected URI createUri(SearchCondDto searchCond) {
        URI uri = UriComponentsBuilder.fromUriString("https://dapi.kakao.com")
                .path("/v2/search/blog")
                .queryParam("query", searchCond.getKeyword())
                .queryParam("sort", mapToProviderCriteria(searchCond.getCriteria()))
                .queryParam("page", searchCond.getPage())
                .queryParam("size", searchCond.getSize())
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();
        log.info("URI : {}", uri);

        return uri;
    }

    protected String mapToProviderCriteria(SearchCriteria criteria){
        if(criteria == SearchCriteria.R){
            return RECENCY;
        }
        return ACCURACY;
    }
}
