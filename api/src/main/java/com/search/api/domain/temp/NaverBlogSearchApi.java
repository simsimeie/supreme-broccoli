package com.search.api.domain.temp;

import com.search.api.domain.blog.constant.SearchCriteria;
import com.search.api.domain.blog.dto.BlogResDto;
import com.search.api.domain.blog.dto.SearchCondDto;
import com.search.api.domain.blog.service.BlogSearchApi;
import com.search.api.domain.keyword.event.KeywordEventPub;
import com.search.core.constant.ErrorCode;
import com.search.core.dto.ResponseDto;
import com.search.core.exception.KBException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
public class NaverBlogSearchApi implements BlogSearchApi {
    private final WebClient webClient;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final Optional<BlogSearchApi> alternativeApi;

    public NaverBlogSearchApi(
            WebClient webClient
            , ApplicationEventPublisher applicationEventPublisher
    ){
        this.webClient = webClient;
        this.applicationEventPublisher = applicationEventPublisher;
        this.alternativeApi = Optional.empty();
    }
    
    private static final String PROVIDER = "NAVER";
    private static final String ACCURACY = "sim";
    private static final String RECENCY = "date";
    @Value("${api-key.naver.id}")
    private String NAVER_API_ID;
    @Value("${api-key.naver.secret}")
    private String NAVER_API_SECRET;

    @Override
    public Mono<ResponseDto<BlogResDto>> search(SearchCondDto searchCond) {
        return webClient.get()
                .uri(createUri(searchCond))
                .header("X-Naver-Client-Id", NAVER_API_ID)
                .header("X-Naver-Client-Secret", NAVER_API_SECRET)
                .retrieve()
                .bodyToMono(BlogResDto.class)
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
                    if (alternativeApi.isPresent()) {
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
    
    private URI createUri(SearchCondDto searchCond) {
        URI uri = UriComponentsBuilder.fromUriString("https://openapi.naver.com/v1/search/blog.json")
                .queryParam("query", searchCond.getKeyword())
                .queryParam("sort", mapToProviderCriteria(searchCond.getCriteria()))
                .queryParam("start", searchCond.getPage())
                .queryParam("display", searchCond.getSize())
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
