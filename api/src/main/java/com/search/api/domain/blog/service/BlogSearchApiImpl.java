package com.search.api.domain.blog.service;

import com.search.api.domain.blog.dto.BlogResDto;
import com.search.api.domain.blog.dto.SearchCondDto;
import com.search.api.domain.keyword.event.KeywordEventPub;
import com.search.core.constant.ErrorCode;
import com.search.core.dto.ResponseDto;
import com.search.core.exception.KBException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
public class BlogSearchApiImpl implements BlogSearchApi {
    private final BlogSearchProvider provider;
    private final BlogSearchApi alternativeApi;
    private final ApplicationEventPublisher applicationEventPublisher;

    public BlogSearchApiImpl(
            BlogSearchProvider provider
            , BlogSearchApi alternativeApi
            , ApplicationEventPublisher applicationEventPublisher
    ) {
        this.provider = provider;
        this.alternativeApi = alternativeApi;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Mono<ResponseDto<BlogResDto>> search(SearchCondDto searchCond){
        return provider.createResponseSpec(searchCond)
                .doOnSuccess(
                        d -> applicationEventPublisher.publishEvent(
                                KeywordEventPub.of(searchCond.getKeyword(), provider.getProvider())
                        )
                )
                .flatMap(d -> Mono.just(ResponseDto.of(d)))
                .doOnError(e -> {
                    log.error("Error occurred while processing the {} API.", provider.getProvider());
                    if (e instanceof WebClientResponseException) {
                        WebClientResponseException ex = (WebClientResponseException) e;
                        log.error("HTTP status code : {}, Info : {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
                    }
                })
                .onErrorResume(e -> {
                    if (null != alternativeApi) {
                        return alternativeApi.search(searchCond);
                    } else {
                        if(e instanceof WebClientResponseException){
                            return Mono.error(new KBException(ErrorCode.API_ERROR));
                        } else {
                            return Mono.error(new KBException(ErrorCode.INTERNAL_SERVER_ERROR));
                        }
                    }
                });
    }

}
