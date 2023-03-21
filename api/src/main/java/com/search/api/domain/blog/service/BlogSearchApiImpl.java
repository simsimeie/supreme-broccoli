package com.search.api.domain.blog.service;

import com.search.api.domain.blog.dto.BlogResDto;
import com.search.api.domain.blog.dto.SearchCondDto;
import com.search.api.domain.keyword.event.KeywordEventPub;
import com.search.core.constant.Code;
import com.search.core.dto.ResponseDto;
import com.search.core.exception.KBException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;


@Slf4j
public class BlogSearchApiImpl implements BlogSearchApi {
    private final BlogSearchApiProvider provider;
    private final BlogSearchApi alternativeApi;
    private final ApplicationEventPublisher keywordEventPublisher;

    public BlogSearchApiImpl(
            BlogSearchApiProvider provider
            , BlogSearchApi alternativeApi
            , ApplicationEventPublisher applicationEventPublisher
    ) {
        this.provider = provider;
        this.alternativeApi = alternativeApi;
        this.keywordEventPublisher = applicationEventPublisher;
    }

    @Override
    public Mono<ResponseDto<BlogResDto>> search(SearchCondDto searchCond){
        String keyword = searchCond.getKeyword();

        return provider.createResponseSpec(searchCond)
                .doOnSuccess(publishKeywordEvent(keyword))
                .flatMap(handleResponse())
                .doOnError(captureException())
                .onErrorResume(handleException(searchCond));
    }

    private Consumer<BlogResDto> publishKeywordEvent(String keyword) {
        return blogResDto -> keywordEventPublisher.publishEvent(
                KeywordEventPub.of(keyword, provider.getProvider()));
    }

    private Function<BlogResDto, Mono<ResponseDto<BlogResDto>>> handleResponse() {
        return blogResDto -> {
            if (blogResDto.getDocuments().isEmpty()) {
                return Mono.error(new KBException(Code.SUCCESS_BUT_NO_DATA));
            } else {
                return Mono.just(ResponseDto.of(blogResDto));
            }
        };
    }

    private Consumer<Throwable> captureException() {
        return e -> {
            if (e instanceof KBException && ((KBException) e).getCode() == Code.SUCCESS_BUT_NO_DATA) {
                log.error("No Data in {} API.", provider.getProvider());
            } else {
                log.error("Error occurred while processing the {} API.", provider.getProvider());
            }

            if (e instanceof WebClientResponseException) {
                WebClientResponseException ex = (WebClientResponseException) e;
                log.error("HTTP status code : {}, Info : {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
            }
        };
    }

    private Function<Throwable, Mono<ResponseDto<BlogResDto>>> handleException(SearchCondDto searchCond) {
        return e -> {
            if (null != alternativeApi) {
                // TODO: 결과가 없으면 대체 API로 시도할지?
                if (e instanceof KBException && ((KBException) e).getCode() == Code.SUCCESS_BUT_NO_DATA) {
                    return Mono.error(e);
                }
                return alternativeApi.search(searchCond);
            } else {
                if (e instanceof WebClientResponseException) {
                    return Mono.error(new KBException(Code.API_ERROR));
                } else {
                    return Mono.error(new KBException(Code.INTERNAL_SERVER_ERROR));
                }
            }
        };
    }






}
