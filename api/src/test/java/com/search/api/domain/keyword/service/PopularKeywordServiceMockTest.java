package com.search.api.domain.keyword.service;

import com.search.core.dto.PopularResDto;
import com.search.core.repository.KeywordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

@ExtendWith(MockitoExtension.class)
class PopularKeywordServiceMockTest {
    @InjectMocks
    private PopularKeywordService popularKeywordService;
    @Mock
    private KeywordRepository keywordRepository;

    @Test
    @DisplayName("Keyword History 저장할 때, keywordRepository.save 메서드 수행되는지 테스트")
    public void keywordSaveTest001(){
        //given
        //when
        popularKeywordService.saveKeywordHistory("query","KAKAO");
        //then
        then(keywordRepository).should(only()).save(any());
    }

    @Test
    @DisplayName("인기검색어 서비스 테스트")
    public void keywordPopularKeywordTest001(){
        //given
        List<PopularResDto> expectList = new ArrayList<>();
        PopularResDto resDto = new PopularResDto("simsimeie", 1000L);
        expectList.add(resDto);
        given(keywordRepository.popular(any())).willReturn(new PageImpl(expectList));
        //when
        List<PopularResDto> response = popularKeywordService.popularKeywordTop10();
        //then
        assertEquals(expectList.size(),response.size());
        assertEquals(expectList.get(0).getQuery(), response.get(0).getQuery());
        assertEquals(expectList.get(0).getCount(), response.get(0).getCount());
    }

}