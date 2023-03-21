package com.search.core.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KeywordTest {

    @Test
    @DisplayName("주어진 Input으로 Keyword Entity 생성할 때, 중간에 정보의 변경 없이 Keyword Entity 생성되는지 테스트")
    public void createKeywordEntityTest001(){
        String query = "오늘의 장소";
        String provider = "KAKAO";
        Keyword keyword = Keyword.createKeyword(query, provider);
        assertEquals(query, keyword.getQuery());
        assertEquals(provider, keyword.getProvider());
    }
}