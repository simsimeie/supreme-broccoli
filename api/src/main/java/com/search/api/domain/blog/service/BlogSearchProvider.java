package com.search.api.domain.blog.service;

import com.search.api.domain.blog.dto.BlogResDto;
import com.search.api.domain.blog.dto.SearchCondDto;
import reactor.core.publisher.Mono;

public interface BlogSearchProvider {
    Mono<BlogResDto> createResponseSpec(SearchCondDto searchCond);
    String getProvider();
}
