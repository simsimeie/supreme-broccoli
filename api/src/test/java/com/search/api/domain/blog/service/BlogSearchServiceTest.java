package com.search.api.domain.blog.service;

import com.search.api.domain.blog.constant.SearchCriteria;
import com.search.api.domain.blog.dto.BlogResDto;
import com.search.api.domain.blog.dto.SearchCondDto;
import com.search.core.dto.ResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BlogSearchServiceTest {
    @Autowired
    private BlogSearchService blogSearchService;
    @Test
    @DisplayName("서비스 health check 테스트")
    public void healthCheck001(){
        //given
        SearchCondDto condition = SearchCondDto.of("아이폰14", SearchCriteria.R, 1, 10);
        Mono<ResponseDto<BlogResDto>> search = blogSearchService.search(condition);
        //when
        ResponseDto<BlogResDto> response = search.block();
        //then
        assertEquals(true, response.getOk());
        assertEquals("KB200", response.getCode());
        assertEquals("성공", response.getMessage());
        assertEquals(10, response.getBody().getDocuments().size());
    }

}