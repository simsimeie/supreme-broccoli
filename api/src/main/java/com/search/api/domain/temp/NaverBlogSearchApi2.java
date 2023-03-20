package com.search.api.domain.temp;

import com.search.api.domain.blog.constant.SearchCriteria;
import com.search.api.domain.blog.dto.SearchCondDto;
import lombok.extern.slf4j.Slf4j;
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
public class NaverBlogSearchApi2 extends BlogSearchApiProvider2 {
    private final WebClient webClient;
    public NaverBlogSearchApi2(
            WebClient webClient
            , ApplicationEventPublisher applicationEventPublisher
    ){
        super(PROVIDER, applicationEventPublisher, null);
        this.webClient = webClient;
    }
    private static final String PROVIDER = "NAVER";
    private static final String ACCURACY = "sim";
    private static final String RECENCY = "date";
    @Value("${api-key.naver.id}")
    private String NAVER_API_ID;
    @Value("${api-key.naver.secret}")
    private String NAVER_API_SECRET;

    @Override
    protected RequestHeadersSpec<?> createRequestSpec(SearchCondDto searchCond) {
        return webClient.get()
                .uri(createUri(searchCond))
                .header("X-Naver-Client-Id", NAVER_API_ID)
                .header("X-Naver-Client-Secret", NAVER_API_SECRET);
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
