package com.search.api.domain.blog.service;

import com.search.api.domain.blog.constant.SearchCriteria;
import com.search.api.domain.blog.dto.BlogResDto;
import com.search.api.domain.blog.dto.SearchCondDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoProvider implements BlogSearchProvider {
    private final WebClient webClient;
    @Value("${api-key.kakao}")
    private String API_KEY;
    private static final String PROVIDER = "KAKAO";
    private static final String ACCURACY = "accuracy";
    private static final String RECENCY = "recency";

    @Override
    public Mono<BlogResDto> createResponseSpec(SearchCondDto searchCond){
        return webClient.get()
                .uri(createUri(searchCond))
                .header("Authorization", API_KEY)
                .retrieve()
                .bodyToMono(BlogResDto.class);
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

    @Override
    public String getProvider() {
        return PROVIDER;
    }
}
