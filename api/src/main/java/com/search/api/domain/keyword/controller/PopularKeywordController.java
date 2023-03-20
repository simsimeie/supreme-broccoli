package com.search.api.domain.keyword.controller;

import com.search.api.domain.keyword.service.PopularKeywordService;
import com.search.core.dto.PopularResDto;
import com.search.core.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class PopularKeywordController {
    private final PopularKeywordService popularKeywordService;
    @GetMapping("/popular-keyword")
    public Mono<ResponseDto<List<PopularResDto>>> popularKeyword(){
        return Mono.just(ResponseDto.of(popularKeywordService.popularKeywordTop10()));
    }
}
