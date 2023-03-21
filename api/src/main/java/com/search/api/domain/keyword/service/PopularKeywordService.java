package com.search.api.domain.keyword.service;

import com.search.core.dto.PopularResDto;
import com.search.core.entity.Keyword;
import com.search.core.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PopularKeywordService {
    private final KeywordRepository keywordRepository;

    @Transactional
    public void saveKeywordHistory(String query, String provider){
        Keyword keyword = Keyword.createKeyword(query, provider);
        keywordRepository.save(keyword);
    }
    public List<PopularResDto> popularKeywordTop10(){
        return keywordRepository.popular(PageRequest.of(0,10)).getContent();
    }

}
