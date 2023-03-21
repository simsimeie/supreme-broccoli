package com.search.api.domain.blog.controller;


import com.search.api.domain.blog.constant.SearchCriteria;
import com.search.api.domain.blog.dto.BlogResDto;
import com.search.api.domain.blog.dto.SearchCondDto;
import com.search.api.domain.blog.service.BlogSearchService;
import com.search.core.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class BlogSearchController {
    private final BlogSearchService blogSearchService;
    @GetMapping("/blog")
    public Mono<ResponseDto<BlogResDto>> blog(
            @Size(max=1000, message = "{query.limit}") String query
            , @RequestParam(defaultValue = "A") String criteria
            , @Min(value = 1, message = "{page.min.limit}") @Max(value = 50, message = "{page.max.limit}")
              @RequestParam(defaultValue = "1") Integer page
            , @Min(value = 1, message = "{size.min.limit}") @Max(value = 50, message = "{size.max.limit}")
              @RequestParam(defaultValue = "10") Integer size
    ){
        SearchCriteria searchCriteria = SearchCriteria.transform(criteria);
        return blogSearchService.search(SearchCondDto.of(query.strip(), searchCriteria, page, size));
    }

}
