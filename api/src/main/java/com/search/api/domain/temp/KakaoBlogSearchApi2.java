package com.search.api.domain.temp;

import com.search.api.domain.blog.constant.SearchCriteria;
import com.search.api.domain.blog.dto.SearchCondDto;
import com.search.api.domain.blog.service.BlogSearchApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class KakaoBlogSearchApi2 extends BlogSearchApiProvider2 {
    private final WebClient webClient;
    public KakaoBlogSearchApi2(
            WebClient webClient
            , ApplicationEventPublisher applicationEventPublisher
            , @Qualifier("naverBlogSearchApi2") BlogSearchApi alternativeApi
    ){
        super(PROVIDER, applicationEventPublisher, alternativeApi);
        this.webClient = webClient;
    }
    private static final String PROVIDER = "KAKAO";
    private static final String ACCURACY = "accuracy";
    private static final String RECENCY = "recency";
    @Value("${api-key.kakao}")
    private String API_KEY;

    @Override
    protected RequestHeadersSpec<?> createRequestSpec(SearchCondDto searchCond) {
        RequestHeadersSpec<?> authorization = webClient.get()
                .uri(createUri(searchCond))
                .header("Authorization", API_KEY);
        return authorization;
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
