package com.search.core.repository;

import com.search.core.dto.PopularResDto;
import com.search.core.entity.Keyword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class KeywordRepositoryTest {
    private KeywordRepository keywordRepository;
    private TestEntityManager em;
    public KeywordRepositoryTest(
            @Autowired KeywordRepository keywordRepository,
            @Autowired TestEntityManager entityManager
    ){
        this.keywordRepository = keywordRepository;
        this.em = entityManager;
    }

    @BeforeEach
    public void init(){
        //given
        Keyword keyword1 = Keyword.createKeyword("오늘의 운세", "KAKAO");
        Keyword keyword2 = Keyword.createKeyword("오늘의 운세", "KAKAO");
        Keyword keyword3 = Keyword.createKeyword("오늘의 운세", "KAKAO");
        Keyword keyword4 = Keyword.createKeyword("오늘의 운세", "KAKAO");
        Keyword keyword5 = Keyword.createKeyword("오늘의 운세", "KAKAO");

        Keyword keyword6 = Keyword.createKeyword("오늘의 날씨", "KAKAO");
        Keyword keyword7 = Keyword.createKeyword("오늘의 뉴스", "KAKAO");
        Keyword keyword8 = Keyword.createKeyword("오늘의 로또", "KAKAO");
        Keyword keyword9 = Keyword.createKeyword("오늘의 주가", "KAKAO");
        Keyword keyword10 = Keyword.createKeyword("오늘의 이슈", "KAKAO");


        keywordRepository.save(keyword1);
        keywordRepository.save(keyword2);
        keywordRepository.save(keyword3);
        keywordRepository.save(keyword4);
        keywordRepository.save(keyword5);

        keywordRepository.save(keyword6);
        keywordRepository.save(keyword7);
        keywordRepository.save(keyword8);
        keywordRepository.save(keyword9);
        keywordRepository.save(keyword10);
    }


    @Test
    @DisplayName("검색키워드를 저장할 때, 정상 저장되는지 테스트")
    public void saveKeywordTest001(){
        //given
        Keyword keyword = Keyword.createKeyword("오늘의 운세", "KAKAO");
        //when
        keywordRepository.save(keyword);
        Optional<Keyword> findKeyword = keywordRepository.findById(keyword.getId());
        //then
        assertEquals(findKeyword.get().getId(), keyword.getId());
        assertEquals(findKeyword.get().getQuery(), keyword.getQuery());
        assertEquals(findKeyword.get().getProvider(), keyword.getProvider());
    }

    @Test
    @DisplayName("검색키워드 다회 저장 후, 검색키워드 별 통계쿼리 수행할 때, 정상적인 count 가져오는지 테스트")
    public void popularKeywordTest001(){
        //given
        //when
        Page<PopularResDto> popular = keywordRepository.popular(PageRequest.of(0, 5));

        //then
        assertEquals(6L, popular.getTotalElements());
        assertEquals(2, popular.getTotalPages());
        assertEquals(5, popular.getNumberOfElements());

        assertEquals(5, popular.getContent().get(0).getCount());
        assertEquals("오늘의 운세", popular.getContent().get(0).getQuery());
    }



}