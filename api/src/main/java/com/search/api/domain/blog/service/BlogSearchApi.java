package com.search.api.domain.blog.service;


import com.search.api.domain.blog.dto.BlogResDto;
import com.search.api.domain.blog.dto.SearchCondDto;
import com.search.core.dto.ResponseDto;
import reactor.core.publisher.Mono;

public interface BlogSearchApi {
    Mono<ResponseDto<BlogResDto>> search(SearchCondDto searchCond);
}
