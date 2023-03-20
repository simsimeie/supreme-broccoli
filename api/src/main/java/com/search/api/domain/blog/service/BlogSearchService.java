package com.search.api.domain.blog.service;


import com.search.api.domain.blog.dto.BlogResDto;
import com.search.api.domain.blog.dto.SearchCondDto;
import com.search.core.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlogSearchService {
    //private final BlogSearchApi kakaoBlogSearchApi;
    private final BlogSearchApiImpl kakaoBlogSearchApi;

    public Mono<ResponseDto<BlogResDto>> search(SearchCondDto searchCond){
        return kakaoBlogSearchApi.search(searchCond);
    }



}
