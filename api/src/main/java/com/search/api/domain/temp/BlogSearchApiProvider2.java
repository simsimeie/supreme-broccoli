package com.search.api.domain.temp;

import com.search.api.domain.blog.dto.BlogResDto;
import com.search.api.domain.blog.dto.SearchCondDto;
import com.search.api.domain.blog.service.BlogSearchApi;
import com.search.core.constant.ErrorCode;
import com.search.core.dto.ResponseDto;
import com.search.api.domain.keyword.event.KeywordEventPub;
import com.search.core.exception.KBException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
@Slf4j
public abstract class BlogSearchApiProvider2 implements BlogSearchApi {
    private final String provider;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final BlogSearchApi alternativeApi;

    protected BlogSearchApiProvider2(
            String provider
            , ApplicationEventPublisher applicationEventPublisher
            , BlogSearchApi alternativeApi) {
        this.provider = provider;
        this.applicationEventPublisher = applicationEventPublisher;
        this.alternativeApi = alternativeApi;
    }

    @Override
    public final Mono<ResponseDto<BlogResDto>> search(SearchCondDto searchCond){
        return createRequestSpec(searchCond)
                .retrieve()
                .bodyToMono(BlogResDto.class)
                .doOnSuccess(
                        d -> applicationEventPublisher.publishEvent(
                                KeywordEventPub.of(searchCond.getKeyword(),provider)
                        )
                )
                .flatMap(d -> Mono.just(ResponseDto.of(d)))
                .doOnError(e -> {
                    log.error("Error occurred while processing the {} API.", provider);
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

    protected abstract RequestHeadersSpec<?> createRequestSpec(SearchCondDto searchCond);

}
